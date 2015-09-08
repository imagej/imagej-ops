/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.image.project;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Stats.Sum;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest extends AbstractOpTest {

	private final int PROJECTION_DIM = 2;

	private Img<UnsignedByteType> in;
	private Img<UnsignedByteType> out1;
	private Img<UnsignedByteType> out2;
	private Op op;

	@Before
	public void initImg() {
		in = generateUnsignedByteTestImg(false, 10, 10, 10);

		final RandomAccess<UnsignedByteType> randomAccess = in.randomAccess();

		// at each x,y,z fill with x+y
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					randomAccess.setPosition(new long[] { x, y, z });
					randomAccess.get().setReal(x + y);
				}
			}
		}

		out1 = generateUnsignedByteTestImg(false, 10, 10);
		out2 = generateUnsignedByteTestImg(false, 10, 10);

		op = ops.op(Sum.class, RealType.class, out1);
	}

	@Test
	public void testProjector() {
		ops.image().project(out1, in, op, PROJECTION_DIM);
		ops.image().project(out2, in, op, PROJECTION_DIM);

		final RandomAccess<UnsignedByteType> out1RandomAccess = out1
				.randomAccess();
		final RandomAccess<UnsignedByteType> out2RandomAccess = out2
				.randomAccess();

		// at each x,y position the sum projection should be (x+y) *size(z)
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				out1RandomAccess.setPosition(new long[] { x, y });
				out2RandomAccess.setPosition(new long[] { x, y });

				assertEquals(out1RandomAccess.get().get(),
						in.dimension(PROJECTION_DIM) * (x + y));
			}
		}

	}
}
