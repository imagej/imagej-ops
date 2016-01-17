package net.imagej.ops.features.gabor;

import java.util.ArrayList;
import java.util.List;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Creates a u * v Gabor filters of size M x N.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 *
 */
@Plugin(type = Ops.Gabor.FilterBank.class)
public class GaborFilterBank extends AbstractOp implements Contingent, Ops.Gabor.FilterBank {

	@Parameter(type = ItemIO.OUTPUT, description = "u * v sized array of 2-D Gabor filters of size M x N")
	private List<List<Img<DoubleType>>> out;

	@Parameter(type = ItemIO.INPUT, description = "u: No. of scales (usually set to 5)")
	private int u = 5;

	@Parameter(type = ItemIO.INPUT, description = "v: No. of orientations (usually set to 8)")
	private int v = 8;

	@Parameter(type = ItemIO.INPUT, description = "m : No. of rows in a 2-D Gabor filter (an odd integer number, usually set to 39)")
	private long m = 39;

	@Parameter(type = ItemIO.INPUT, description = "n : No. of rows in a 2-D Gabor filter (an odd integer number, usually set to 39)")
	private long n = 39;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		UnaryFunctionOp<FinalInterval, Img> createImgFunc = Functions.unary(ops(), CreateImgFromInterval.class,
				Img.class, new FinalInterval(0, 0), new DoubleType(), new ArrayImgFactory<DoubleType>());
		FinalInterval imgSize = new FinalInterval(m, n);

		out = new ArrayList<>();
		for (int u = 0; u < this.u; u++) {
			out.add(new ArrayList<>(v));
		}

		double fmax = 0.25;
		double gama = Math.sqrt(2);
		double eta = Math.sqrt(2);

		for (int i = 1; i <= u; i++) {

			double fu = fmax / Math.pow(Math.sqrt(2), (i - 1));
			double alpha = fu / gama;
			double beta = fu / eta;

			for (int j = 1; j <= v; j++) {

				double tetav = ((j - 1) / v) * Math.PI;

				// m x n matrix
				Img<DoubleType> gFilter = createImgFunc.compute1(imgSize);
				Cursor<DoubleType> localizingCursor = gFilter.localizingCursor();
				while (localizingCursor.hasNext()) {
					localizingCursor.fwd();

					double x = localizingCursor.getDoublePosition(0);
					double y = localizingCursor.getDoublePosition(1);

					double xprime = (x - ((m + 1) / 2)) * Math.cos(tetav) + (y - ((n + 1) / 2)) * Math.sin(tetav);
					double yprime = -(x - ((m + 1) / 2)) * Math.sin(tetav) + (y - ((n + 1) / 2)) * Math.cos(tetav);

					double val = (Math.pow(fu, 2) / (Math.PI * gama * eta))
							* Math.exp(-((Math.pow(alpha, 2)) * (Math.pow(xprime, 2))
									+ (Math.pow(beta, 2)) * (Math.pow(yprime, 2))))
							* Math.exp(1 * i * 2 * Math.PI * fu * xprime);

					localizingCursor.get().set(val);
				}

				out.get(i - 1).add(gFilter);
			}
		}
	}

	@Override
	public boolean conforms() {
		return m % 2 != 0 && n % 2 != 0;
	}
}
