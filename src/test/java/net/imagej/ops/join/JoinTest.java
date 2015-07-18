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

package net.imagej.ops.join;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.BufferFactory;
import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.map.MapOp;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

public class JoinTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Op inplaceOp;
	private Op functionalOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		out = generateByteTestImg(false, dims);
		inplaceOp = ops.op(MapOp.class, Img.class, new AddOneInplace());
		functionalOp =
			ops.op(MapOp.class, Img.class, Img.class, new AddOneFunctional());
	}

	@Test
	public void testInplaceJoin() {
		final Op op =
			ops.op(DefaultJoinInplaceAndInplace.class, in, inplaceOp, inplaceOp);
		op.run();

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testFunctionInplaceJoin() {
		final Op op =
			ops.op(DefaultJoinFunctionAndInplace.class, out, in, functionalOp,
				inplaceOp);
		op.run();

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testInplaceFunctionJoin() {
		final Op op =
			ops.op(DefaultJoinInplaceAndFunction.class, out, in, inplaceOp,
				functionalOp);
		op.run();

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testFunctionAndFunctionJoin() {

		final BufferFactory<Img<ByteType>, Img<ByteType>> bufferFactory =
			new BufferFactory<Img<ByteType>, Img<ByteType>>() {

				@Override
				public Img<ByteType> createBuffer(final Img<ByteType> input) {
					return input.factory().create(input,
						input.firstElement().createVariable());
				}
			};

		ops.join(out, in, functionalOp, functionalOp, bufferFactory);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testJoinFunctions() {

		final List<Function<Img<ByteType>, Img<ByteType>>> functions =
			new ArrayList<Function<Img<ByteType>, Img<ByteType>>>();

		for (int i = 0; i < 5; i++) {
			functions.add(new AddOneFunctionalImg());
		}

		final BufferFactory<Img<ByteType>, Img<ByteType>> bufferFactory =
			new BufferFactory<Img<ByteType>, Img<ByteType>>() {

				@Override
				public Img<ByteType> createBuffer(final Img<ByteType> input) {
					return input.factory().create(input,
						input.firstElement().createVariable());
				}
			};

		ops.join(out, in, functions, bufferFactory);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			assertEquals(5, c.next().get());
		}
	}

	// Helper classes
	class AddOneInplace extends AbstractInplaceFunction<ByteType> {

		@Override
		public ByteType compute(final ByteType arg) {
			arg.inc();
			return arg;
		}
	}

	class AddOneFunctional extends AbstractStrictFunction<ByteType, ByteType> {

		@Override
		public void compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
		}
	}

	class AddOneFunctionalImg extends
		AbstractStrictFunction<Img<ByteType>, Img<ByteType>>
	{

		@Override
		public void compute(final Img<ByteType> input,
			final Img<ByteType> output)
		{
			ops.run(MapOp.class, output, input, new AddOneFunctional());
		}
	}

}
