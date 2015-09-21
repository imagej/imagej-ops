package net.imagej.ops.descriptor3d;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.Centroid;
import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;

/**
 * This {@link Op} computes the centroid of a {@link IterableRegion} (Label).
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 * @param <B> a Boolean Type
 */
@Plugin(type = Op.class, name = Descriptor3D.Centroid.NAME)
public class DefaultCentroid<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, double[]> implements Centroid {

	@Override
	public double[] compute(IterableRegion<B> input) {
		int numDimensions = input.numDimensions();
		double[] output = new double[numDimensions];
		Cursor<B> c = input.localizingCursor();
		while (c.hasNext()) {
			c.fwd();
			double[] pos = new double[numDimensions];
			c.localize(pos);
			for (int i = 0; i < output.length; i++) {
				output[i] += pos[i];
			}
		}

		for (int i = 0; i < output.length; i++) {
			output[i] = output[i] / (double)input.size();
		}
		
		return output;
	}

}
