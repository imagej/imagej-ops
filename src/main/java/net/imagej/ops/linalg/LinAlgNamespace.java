
package net.imagej.ops.linalg;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.linalg.rotate.Rotate3d;

import org.joml.AxisAngle4d;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.scijava.plugin.Plugin;

/**
 * The linear algebra namespace has ops for vectors and matrices.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Namespace.class)
public class LinAlgNamespace extends AbstractNamespace {

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate(final Vector3d v, final Quaterniondc q) {
		return (Vector3d) ops().run(Rotate3d.class, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate(final Vector3d out, final Vector3d v,
		final Quaterniondc q)
	{
		return (Vector3d) ops().run(Rotate3d.class, out, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate(final Vector3d v, final AxisAngle4d axisAngle) {
		final Quaterniondc q = new Quaterniond(axisAngle);
		return (Vector3d) ops().run(Rotate3d.class, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate(final Vector3d out, final Vector3d v,
		final AxisAngle4d axisAngle)
	{
		final Quaterniondc q = new Quaterniond(axisAngle);
		return (Vector3d) ops().run(Rotate3d.class, out, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate1(final Vector3d v, final Quaterniondc q) {
		return (Vector3d) ops().run(Rotate3d.class, v, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate1(final Vector3d v, final AxisAngle4d axisAngle) {
		final Quaterniondc q = new Quaterniond(axisAngle);
		return (Vector3d) ops().run(Rotate3d.class, v, v, q);
	}

	@Override
	public String getName() {
		return "linalg";
	}
}
