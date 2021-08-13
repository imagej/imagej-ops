/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ developers.
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

package net.imagej.ops.geom;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.geom.geom3d.mesh.BitTypeVertexInterpolator;
import net.imagej.ops.geom.geom3d.mesh.DefaultVertexInterpolator;

import org.junit.Test;

/**
 * This class tests the {@link BitTypeVertexInterpolator}.
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
public class DefaultVertexInterpolatorTest extends AbstractOpTest {

	@Test
	public void vertexInterpolator_025_v2_Test() {
		int[] p1 = new int[] { 0, 0, 0 };
		int[] p2 = new int[] { 10, 0, 10 };
		double v1 = 0;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2,
			v1, v2, 0.25);
		assertEquals(2.5, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(2.5, res[2], 1e-10);
	}

	@Test
	public void vertexInterpolator_1_v2_Test() {
		int[] p1 = new int[] { 0, 0, 0 };
		int[] p2 = new int[] { 10, 0, 10 };
		double v1 = 0;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2,
			v1, v2, 1);
		assertEquals(10, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(10, res[2], 1e-10);
	}

	@Test
	public void vertexInterpolator_1_v1_Test() {
		int[] p1 = new int[] { 0, 0, 0 };
		int[] p2 = new int[] { 10, 0, 10 };
		double v1 = 1;
		double v2 = 0;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2,
			v1, v2, 1);
		assertEquals(0, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(0, res[2], 1e-10);
	}

	@Test
	public void vertexInterpolator_1_equalValues_Test() {
		int[] p1 = new int[] { 0, 0, 0 };
		int[] p2 = new int[] { 10, 0, 10 };
		double v1 = 1;
		double v2 = 1;
		double[] res = (double[]) ops.run(DefaultVertexInterpolator.class, p1, p2,
			v1, v2, 1);
		assertEquals(0, res[0], 1e-10);
		assertEquals(0, res[1], 1e-10);
		assertEquals(0, res[2], 1e-10);
	}
}
