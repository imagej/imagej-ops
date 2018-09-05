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

package net.imagej.ops.stats.regression.leastSquares;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import net.imagej.ops.AbstractOpTest;

import org.joml.Matrix4d;
import org.joml.Matrix4dc;
import org.joml.Vector3d;
import org.junit.Test;

/**
 * Tests for {@link Quadric}.
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class QuadricTest extends AbstractOpTest {

	private static final double alpha = Math.cos(Math.PI / 4.0);
	private static final List<Vector3d> unitSpherePoints = Stream.of(new Vector3d(
		1, 0, 0), new Vector3d(-1, 0, 0), new Vector3d(0, 1, 0), new Vector3d(0, -1,
			0), new Vector3d(0, 0, 1), new Vector3d(0, 0, -1), new Vector3d(alpha,
				alpha, 0), new Vector3d(-alpha, alpha, 0), new Vector3d(alpha, -alpha,
					0), new Vector3d(-alpha, -alpha, 0), new Vector3d(0, alpha, alpha),
		new Vector3d(0, -alpha, alpha), new Vector3d(0, alpha, -alpha),
		new Vector3d(0, -alpha, -alpha), new Vector3d(alpha, 0, alpha),
		new Vector3d(alpha, 0, -alpha), new Vector3d(-alpha, 0, alpha),
		new Vector3d(-alpha, 0, -alpha)).collect(toList());

	@Test
	public void testEquation() {
		final Matrix4dc solution = (Matrix4dc) ops.run(Quadric.class,
			unitSpherePoints);
		final double a = solution.m00();
		final double b = solution.m11();
		final double c = solution.m22();
		final double d = solution.m01();
		final double e = solution.m02();
		final double f = solution.m12();
		final double g = solution.m03();
		final double h = solution.m13();
		final double i = solution.m23();

		for (final Vector3d p : unitSpherePoints) {
			final double polynomial = a * p.x * p.x + b * p.y * p.y + c * p.z * p.z +
				2 * d * p.x * p.y + 2 * e * p.x * p.z + 2 * f * p.y * p.z + 2 * g *
					p.x + 2 * h * p.y + 2 * i * p.z;
			assertEquals("The matrix does not solve the polynomial equation", 1.0,
				polynomial, 1e-12);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatchingFailsIfTooFewPoints() {
		final int nPoints = Math.max(0, Quadric.MIN_DATA - 1);
		final List<Vector3d> points = Stream.generate(Vector3d::new).limit(nPoints)
			.collect(toList());

		ops.run(Quadric.class, points);
	}

	@Test
	public void testMatrixElements() {
		final Matrix4dc solution = (Matrix4dc) ops.run(Quadric.class,
			unitSpherePoints);

		assertEquals("The matrix element is incorrect", 1.0, solution.m00(), 1e-12);
		assertEquals("The matrix element is incorrect", 1.0, solution.m11(), 1e-12);
		assertEquals("The matrix element is incorrect", 1.0, solution.m22(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m01(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m02(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m03(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m12(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m13(), 1e-12);
		assertEquals("The matrix element is incorrect", 0.0, solution.m23(), 1e-12);
		assertEquals("The matrix element is incorrect", -1.0, solution.m33(),
			1e-12);
		final Matrix4d transposed = new Matrix4d();
		solution.transpose(transposed);
		assertEquals("Matrix is not symmetric", solution, transposed);
	}
}
