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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import net.imagej.ops.AbstractOpTest;

import org.joml.AxisAngle4d;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.junit.Test;

/**
 * Tests for {@link Rotate3d}.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class Rotate3dTest extends AbstractOpTest {

	private static final Quaterniondc IDENTITY = new Quaterniond(1, 0, 0, 0);

	@Test
	public void testAxisAngle() {
		final Vector3d xAxis = new Vector3d(1, 0, 0);
		final Vector3d in = new Vector3d(xAxis);
		final AxisAngle4d axisAngle = new AxisAngle4d(Math.PI / 2.0, 0, 0, 1);
		final Vector3d expected = xAxis.rotate(new Quaterniond(axisAngle));

		final Vector3d result = ops.linalg().rotate(in, axisAngle);

		assertEquals("Rotation is incorrect", expected, result);
	}

	@Test
	public void testCalculate() {
		final Vector3d xAxis = new Vector3d(1, 0, 0);
		final Vector3d in = new Vector3d(xAxis);

		final Vector3d result = ops.linalg().rotate(in, IDENTITY);

		assertNotSame("Op should create a new object for output", in, result);
		assertEquals("Rotation is incorrect", xAxis, result);
	}

	@Test
	public void testCompute() {
		final Vector3d origin = new Vector3d();
		final Vector3d xAxis = new Vector3d(1, 0, 0);
		final Vector3d in = new Vector3d(xAxis);
		final Vector3d out = new Vector3d(origin);

		final Vector3d result = ops.linalg().rotate(out, in, IDENTITY);

		assertSame("Op should not create a new object for output", out, result);
		assertEquals("Rotation is incorrect", xAxis, out);
	}

	@Test
	public void testMutate() {
		final Vector3d xAxis = new Vector3d(1, 0, 0);
		final Vector3d in = new Vector3d(xAxis);
		final Quaterniond q = new Quaterniond(new AxisAngle4d(Math.PI / 2.0, 0, 0,
			1));
		final Vector3d expected = xAxis.rotate(q);

		final Vector3d result = ops.linalg().rotate1(in, q);

		assertSame("Mutate should operate on the input object", in, result);
		assertEquals("Rotation is incorrect", expected, result);
	}
}
