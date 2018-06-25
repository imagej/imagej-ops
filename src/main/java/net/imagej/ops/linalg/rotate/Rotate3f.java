
package net.imagej.ops.linalg.rotate;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI1;

import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.scijava.plugin.Plugin;

/**
 * Rotates the vector by the quaternion.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.LinAlg.Rotate.class)
public class Rotate3f extends
	AbstractBinaryHybridCFI1<Vector3f, Quaternionfc, Vector3f> implements
	Ops.LinAlg.Rotate
{

	@Override
	public void compute(final Vector3f v, final Quaternionfc q,
		final Vector3f vDot)
	{
		vDot.set(v);
		vDot.rotate(q);
	}

	@Override
	public Vector3f createOutput(final Vector3f v, final Quaternionfc q) {
		return new Vector3f();
	}

	@Override
	public void mutate1(final Vector3f v, final Quaternionfc q) {
		v.rotate(q);
	}
}
