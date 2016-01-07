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

package net.imagej.ops.create;

import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.img.DefaultCreateImg;
import net.imagej.ops.special.BinaryFunctionOp;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;

public class DefaultCreateTest extends AbstractOpTest {

	@Test
	public void testDefaultCreate() {

		BinaryFunctionOp<Dimensions, FloatType, Img<FloatType>> op =
			(BinaryFunctionOp) Functions.binary(ops, DefaultCreateImg.class,
				Img.class, Dimensions.class, new FloatType());

		// test compute1 with a byte image as input... the Byte image is only used
		// to
		// get the dimensions and we should get a Float Img back
		Img<?> test1 = op.compute1(ArrayImgs.bytes(new long[] { 10, 10 }));

		// test the binary op as a unary op
		Img<?> test2 = testBinaryAsUnary(op);

		assert (test1.firstElement() instanceof FloatType);
		assert (test2.firstElement() instanceof FloatType);
	}

	Img<FloatType> testBinaryAsUnary(
		UnaryFunctionOp<Dimensions, Img<FloatType>> op)
	{
		return op.compute1(ArrayImgs.bytes(new long[] { 10, 10 }));
	}
}
