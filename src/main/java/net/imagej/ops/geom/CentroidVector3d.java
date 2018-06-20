
package net.imagej.ops.geom;

import java.util.Iterator;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;

import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.scijava.plugin.Plugin;

/**
 * Calculates the centroid (geometrical centre) of the vectors
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Geometric.Centroid.class)
public class CentroidVector3d extends
	AbstractUnaryFunctionOp<Iterable<Vector3dc>, Vector3d> implements
	Ops.Geometric.Centroid
{

	@Override
	public Vector3d calculate(final Iterable<Vector3dc> input) {
		final Iterator<Vector3dc> iterator = input.iterator();
		final Vector3d sum = new Vector3d();
		long count = 0;
		while (iterator.hasNext()) {
			final Vector3dc v = iterator.next();
			sum.add(v);
			count++;
		}
		sum.div(count);
		return sum;
	}
}
