/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.linalg;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.linalg.rotate.Rotate3d;
import net.imagej.ops.linalg.rotate.Rotate3f;

import org.joml.AxisAngle4d;
import org.joml.AxisAngle4f;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.scijava.plugin.Plugin;

/**
 * The linear algebra namespace has ops for vectors and matrices.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Namespace.class)
public class LinAlgNamespace extends AbstractNamespace {

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate(final Vector3f v, final Quaternionfc q) {
		return (Vector3f) ops().run(Rotate3f.class, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate(final Vector3f out, final Vector3f v,
		final Quaternionfc q)
	{
		return (Vector3f) ops().run(Rotate3f.class, out, v, q);
	}

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

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate(final Vector3f v, final AxisAngle4f axisAngle) {
		final Quaternionfc q = new Quaternionf(axisAngle);
		return (Vector3f) ops().run(Rotate3f.class, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate(final Vector3d out, final Vector3d v,
		final AxisAngle4d axisAngle)
	{
		final Quaterniondc q = new Quaterniond(axisAngle);
		return (Vector3d) ops().run(Rotate3d.class, out, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate(final Vector3f out, final Vector3f v,
		final AxisAngle4f axisAngle)
	{
		final Quaternionfc q = new Quaternionf(axisAngle);
		return (Vector3f) ops().run(Rotate3f.class, out, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate1(final Vector3d v, final Quaterniondc q) {
		return (Vector3d) ops().run(Rotate3d.class, v, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate1(final Vector3f v, final Quaternionfc q) {
		return (Vector3f) ops().run(Rotate3f.class, v, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3d.class)
	public Vector3d rotate1(final Vector3d v, final AxisAngle4d axisAngle) {
		final Quaterniondc q = new Quaterniond(axisAngle);
		return (Vector3d) ops().run(Rotate3d.class, v, v, q);
	}

	@OpMethod(op = net.imagej.ops.linalg.rotate.Rotate3f.class)
	public Vector3f rotate1(final Vector3f v, final AxisAngle4f axisAngle) {
		final Quaternionfc q = new Quaternionf(axisAngle);
		return (Vector3f) ops().run(Rotate3f.class, v, v, q);
	}

	@Override
	public String getName() {
		return "linalg";
	}
}
