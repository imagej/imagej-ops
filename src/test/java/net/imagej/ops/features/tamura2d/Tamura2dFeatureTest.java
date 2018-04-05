/*
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
package net.imagej.ops.features.tamura2d;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * 
 * Test for {@Link Tamura2d}-Features
 * 
 * @author Andreas Graumann (University of Konstanz)
 *
 */
public class Tamura2dFeatureTest extends AbstractFeatureTest {

	@Test
	public void testContrastFeature() {
		assertEquals(Ops.Tamura.Contrast.NAME, 63.7185, ((RealType<?>) ops.run(
			DefaultContrastFeature.class, random)).getRealDouble(), 1e-3);
	}

	@Test
	public void testDirectionalityFeature() {
		assertEquals(Ops.Tamura.Directionality.NAME, 0.007819, ((RealType<?>) ops
			.run(DefaultDirectionalityFeature.class, random, 16)).getRealDouble(),
			1e-3);
	}

	@Test
	public void testCoarsenessFeature() {
		assertEquals(Ops.Tamura.Coarseness.NAME, 43.614, ((RealType<?>) ops.run(
			DefaultCoarsenessFeature.class, random)).getRealDouble(), 1e-3);

		// NB: according to the implementation, this 2x2 image should have exactly 0
		// coarseness.
		byte[] arr = new byte[] {0, -1, 0, 0};
		Img<ByteType> in = ArrayImgs.bytes(arr, 2, 2);
		assertEquals(Ops.Tamura.Coarseness.NAME, 0.0, ((RealType<?>) ops.run(
			DefaultCoarsenessFeature.class, in)).getRealDouble(), 0.0);
	}

}
