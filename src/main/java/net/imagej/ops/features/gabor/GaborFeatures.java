package net.imagej.ops.features.gabor;

import java.util.ArrayList;
import java.util.List;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imagej.ops.math.RealMath.Abs;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.Views;

/**
 * Creates a vector with length (m*n*u*v)/(d1*d2). This vector is the Gabor
 * feature vector of an m by n image. u is the number of scales and v is the
 * number of orientations in 'gaborArray'.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
@Plugin(type = Ops.Gabor.Feature.class)
public class GaborFeatures extends AbstractOp implements Ops.Gabor.Feature {

	@Parameter(type = ItemIO.OUTPUT, description = "A column vector with length (m*n*u*v)/(d1*d2). This vector is the Gabor feature vector of an m by n image. u is the number of scales and v is the number of orientations in 'gaborArray'.")
	private double[] featureVector;

	@Parameter(type = ItemIO.INPUT, description = "img: The input image ")
	private Img<DoubleType> img;

	@Parameter(type = ItemIO.INPUT, description = "gaborArray: Gabor filters bank created by the function gaborFilterBank")
	private List<List<Img<DoubleType>>> gaborArray;

	@Parameter(type = ItemIO.INPUT, description = "d1: The factor of downsampling along rows.")
	private int d1;

	@Parameter(type = ItemIO.INPUT, description = "d2: The factor of downsampling along columns.")
	private int d2;

	@Override
	public void run() {

		List<Double> featureVector = new ArrayList<Double>();

		int k = 0;

		for (int u = 0; u < gaborArray.size(); u++) {
			for (int v = 0; v < gaborArray.get(u).size(); v++) {

				k++;

				// apply gabor filter
				Img<DoubleType> convolve = (Img<DoubleType>) ops().filter().convolve(img, gaborArray.get(u).get(v));

				// to absolute values
				Cursor<DoubleType> convCurs = convolve.cursor();
				while (convCurs.hasNext()) {
					convCurs.fwd();
					convCurs.get().setReal(Math.abs(convCurs.get().getRealDouble()));
				}

				// downsample
				SubsampleView<DoubleType> subsample = Views.subsample(convolve, d1, d2);

				IterableInterval<DoubleType> result = Views.flatIterable(Views.interval(subsample,
						new FinalInterval(convolve.dimension(0) / d1, convolve.dimension(1) / d2 + 1)));

				DoubleType std = ops().stats().stdDev(result);
				DoubleType mean = ops().stats().mean(result);

				Cursor<DoubleType> cursor = result.cursor();
				while (cursor.hasNext()) {
					DoubleType next = cursor.next();

					featureVector.add((next.getRealDouble() - mean.getRealDouble()) / std.getRealDouble());
				}
			}
		}

		this.featureVector = new double[featureVector.size()];
		for (int i = 0; i < featureVector.size(); i++) {
			this.featureVector[i] = featureVector.get(i);
		}
	}

}
