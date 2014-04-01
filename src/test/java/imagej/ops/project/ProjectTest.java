/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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

package imagej.ops.project;

import static org.junit.Assert.assertEquals;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.descriptors.statistics.Sum;
import imagej.ops.project.parallel.DefaultProjectP;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest extends AbstractOpTest {

	private final int PROJECTION_DIM = 2;

	private Img<ByteType> in;
	private Img<ByteType> out1;
	private Img<ByteType> out2;
	private Op op;

	@Before
	public void initImg() {
		in = generateByteTestImg(false, 10, 10, 10);

		// fill in with ones
		final Cursor<ByteType> cursor = in.cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.get().set((byte) 1);
		}

		out1 = generateByteTestImg(false, 10, 10);
		out2 = generateByteTestImg(false, 10, 10);

		op = ops.op("sum", null, Sum.class);
	}

	@Test
	public void testProjector() {
		ops.run(ProjectRAI2II.class, out1, in, op, PROJECTION_DIM);
		ops.run(DefaultProjectP.class, out2, in, op, PROJECTION_DIM);

		// test
		final Cursor<ByteType> out1Cursor = out1.cursor();
		final Cursor<ByteType> out2Cursor = out2.cursor();

		while (out1Cursor.hasNext()) {
			out1Cursor.fwd();
			out2Cursor.fwd();

			assertEquals(out1Cursor.get().get(), in.dimension(PROJECTION_DIM));
			assertEquals(out2Cursor.get().get(), in.dimension(PROJECTION_DIM));
		}
	}
}
