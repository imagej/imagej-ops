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

package imagej.ops.tests.benchmark;

import imagej.module.Module;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import add.AddConstantToArrayByteImage;
import add.AddConstantToImageFunctional;
import add.AddConstantToImageInPlace;

/**
 * @author Christian Dietz
 */
public class AddOpBenchmark extends AbstractOpBenchmark {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private int numRuns;

	public void initImg() {
		in = generateByteTestImg(true, 5000, 5000);
		out = generateByteTestImg(false, 5000, 5000);
		numRuns = 20;
	}

	public void usingAddConstantWithMapper() {
		final Module module =
			ops.module("map", in, ops.op("addconstant", null, null, new ByteType(
				(byte) 10)), out);

		benchmarkAndPrint("Best Mapping AddConstant", module, numRuns);
	}

	public void usingAddConstantWithInplaceMapping() {
		final Module module =
			ops.module("map", in, ops.op("addconstant", null, null, new ByteType(
				(byte) 10)));

		benchmarkAndPrint("Best Inplace Mapping AddConstant", module, numRuns);
	}

	public void usingAddConstantInPlace() {
		final Module module =
			ops.module(new AddConstantToImageInPlace<ByteType>(), in, new ByteType(
				(byte) 10));
		benchmarkAndPrint("Inplace AddConstant", module, numRuns);
	}

	public void usingAddConstantFunctional() {
		final Module module =
			ops.module(new AddConstantToImageFunctional<ByteType>(), in, out,
				new ByteType((byte) 10));
		benchmarkAndPrint("Functional AddConstant", module, numRuns);
	}

	public void usingOptimizedAddConstant() {
		final Module module =
			ops.module(new AddConstantToArrayByteImage(), in, (byte) 10);
		benchmarkAndPrint("ArrayByteImage Optimized AddConstant", module, numRuns);
	}

	// run the benchmarks
	public static void main(final String[] args) {
		final AddOpBenchmark mappersBenchmark = new AddOpBenchmark();

		mappersBenchmark.setUp();
		mappersBenchmark.initImg();

		mappersBenchmark.usingOptimizedAddConstant();
		mappersBenchmark.usingAddConstantWithMapper();
		mappersBenchmark.usingAddConstantWithInplaceMapping();
		mappersBenchmark.usingAddConstantInPlace();
		mappersBenchmark.usingAddConstantFunctional();

		mappersBenchmark.cleanUp();
	}
}
