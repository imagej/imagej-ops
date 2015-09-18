package net.imagej.ops.descriptor3d;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
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
@Plugin(type = Op.class, name = "centroid3d")
public class DefaultCentroid3D<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, Vector3D> implements Centroid {

	@Override
	public Vector3D compute(IterableRegion<B> input) {
		Cursor<Void> c = input.localizingCursor();
		double x = 0;
		double y = 0;
		double z = 0;
		while (c.hasNext()) {
			c.fwd();
			double[] pos = new double[3];
			c.localize(pos);
			x += pos[0];
			y += pos[1];
			z += pos[2];
		}

		Vector3D output = new Vector3D(x / input.size(), y/input.size(), z/input.size());
		return output;
	}

}
