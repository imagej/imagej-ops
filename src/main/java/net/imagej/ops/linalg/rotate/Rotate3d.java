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

package net.imagej.ops.linalg.rotate;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCFI1;

import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.scijava.plugin.Plugin;

/**
 * Rotates the vector by the quaternion.
 *
 * @author Richard Domander (Royal Veterinary College, London)
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
