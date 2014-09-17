/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.project;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.statistics.Sum;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.meta.Axes;
import net.imglib2.meta.AxisType;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest extends AbstractOpTest {

	private final int PROJECTION_DIM = 2;

	private Img<ByteType> in;

	private ProjectMethod<ByteType, DoubleType> method;

	@SuppressWarnings("unchecked")
	@Before
	public void initImg() {
		in = generateByteTestImg(false, 10, 10, 10);

		// fill in with ones
		final Cursor<ByteType> cursor = in.cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.get().set((byte) 1);
		}

		Sum op = ops.op(Sum.class, DoubleType.class, Img.class);

		method = (ProjectMethod<ByteType, DoubleType>) ops.op(
				ProjectMethod.class, DoubleType.class, Img.class, op);
	}

	@Test
	public void testProjectorImg() {
		@SuppressWarnings("unchecked")
		Img<DoubleType> out = (Img<DoubleType>) ops.run(Project.class,
				Img.class, in, method, PROJECTION_DIM, new DoubleType());

		final Cursor<DoubleType> out1Cursor = out.cursor();
		while (out1Cursor.hasNext()) {
			out1Cursor.fwd();
			assertEquals(out1Cursor.get().get(), in.dimension(PROJECTION_DIM),
					0);
		}
	}

	@Test
	public void testProjectorImgPlus() {
		ImgPlus<ByteType> inImgPlus = new ImgPlus<ByteType>(in);
		AxisType axis = inImgPlus.axis(PROJECTION_DIM).type();

		@SuppressWarnings("unchecked")
		ImgPlus<DoubleType> out = (ImgPlus<DoubleType>) ops.run(Project.class,
				ImgPlus.class, inImgPlus, method, axis, new DoubleType());

		final Cursor<DoubleType> out1Cursor = out.cursor();
		while (out1Cursor.hasNext()) {
			out1Cursor.fwd();
			assertEquals(out1Cursor.get().get(),
					inImgPlus.dimension(PROJECTION_DIM), 0);
		}
	}
}
