
package net.imagej.ops.linalg.rotate;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI1;

import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.scijava.plugin.Plugin;

/**
 * Rotates the vector by the quaternion.
 *
 * @author Richard Domander
 */
@Plugin(type = Ops.LinAlg.Rotate.class)
public class Rotate3d extends
	AbstractBinaryHybridCFI1<Vector3d, Quaterniondc, Vector3d> implements
	Ops.LinAlg.Rotate
{

	@Override
	public void compute(final Vector3d v, final Quaterniondc q,
		final Vector3d vDot)
	{
		vDot.set(v);
		vDot.rotate(q);
	}

	@Override
	public Vector3d createOutput(final Vector3d input1,
		final Quaterniondc input2)
	{
		return new Vector3d();
	}

	@Override
	public void mutate1(final Vector3d v, final Quaterniondc q) {
		v.rotate(q);
	}
}
