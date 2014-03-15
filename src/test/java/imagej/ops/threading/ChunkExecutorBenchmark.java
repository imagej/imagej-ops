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
package imagej.ops.threading;

import imagej.module.Module;
import imagej.ops.benchmark.AbstractOpBenchmark;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

public class ChunkExecutorBenchmark extends AbstractOpBenchmark {

	private int numRuns;

	public void init() {
		numRuns = 30;
	}

	public void run100MbTest() {		
		Img<ByteType> in = generateByteTestImg(true, 10240, 10240);
		Img<ByteType> out = generateByteTestImg(false, 10240, 10240);
		
		final Module module = ops.module(RunDefaultChunkExecutor.class, out, in);

		benchmarkAndPrint("Default Chunker 100", module, numRuns);
	}
	
	public void run100MbInterleaved() {
		Img<ByteType> in = generateByteTestImg(true, 10240, 10240);
		Img<ByteType> out = generateByteTestImg(false, 10240, 10240);
		
		final Module module = ops.module(RunInterleavedChunkExecutor.class, out, in);

		benchmarkAndPrint("Interleaved Chunker 100", module, numRuns);
	}

	public void run1MbTest() {		
		Img<ByteType> in = generateByteTestImg(true, 1024, 1024);
		Img<ByteType> out = generateByteTestImg(false, 1024, 1024);
		
		final Module module = ops.module(RunDefaultChunkExecutor.class, out, in);

		benchmarkAndPrint("Default Chunker 1", module, numRuns);
	}
	
	public void run1MbInterleaved() {
		Img<ByteType> in = generateByteTestImg(true, 1024, 1024);
		Img<ByteType> out = generateByteTestImg(false, 1024, 1024);
		
		final Module module = ops.module(RunInterleavedChunkExecutor.class, out, in);

		benchmarkAndPrint("Interleaved Chunker 1", module, numRuns);
	}

	//with arrays
	
	public void run100MbArrayTest() {		
		Byte[] in = new Byte[ 10240 * 10240];
		Byte[] out = new Byte[ 10240 * 10240];
		
		final Module module = ops.module(RunDefaultChunkExecutorArray.class, out, in);

		benchmarkAndPrint("Default Chunker array 100", module, numRuns);
	}
	
	public void run100MbArrayInterleavedTest() {
		Byte[] in = new Byte[ 10240 * 10240];
		Byte[] out = new Byte[ 10240 * 10240];
		
		final Module module = ops.module(RunInterleavedChunkExecutorArray.class, out, in);

		benchmarkAndPrint("Interleaved Chunker array 100", module, numRuns);
	}

	public void run1MbArrayTest() {		
		Byte[] in = new Byte[ 1024 * 1024];
		Byte[] out = new Byte[ 1024 * 1024];
		
		final Module module = ops.module(RunDefaultChunkExecutorArray.class, out, in);

		benchmarkAndPrint("Default Chunker array 1", module, numRuns);
	}
	
	public void run1MbArrayInterleavedTest() {
		Byte[] in = new Byte[ 1024 * 1024];
		Byte[] out = new Byte[ 1024 * 1024];
		
		final Module module = ops.module(RunInterleavedChunkExecutorArray.class, out, in);

		benchmarkAndPrint("Interleaved Chunker array 1", module, numRuns);
	}

	
	// run the benchmarks
	public static void main(final String[] args) {
		final ChunkExecutorBenchmark benchmark = new ChunkExecutorBenchmark();

		benchmark.setUp();
		benchmark.init();

		benchmark.run100MbTest();
		benchmark.run100MbInterleaved();
		
		benchmark.run1MbTest();
		benchmark.run1MbInterleaved();
		//with array
		benchmark.run100MbArrayTest();
		benchmark.run100MbArrayInterleavedTest();
		
		benchmark.run1MbArrayTest();
		benchmark.run1MbArrayInterleavedTest();
		
		
		benchmark.cleanUp();
	}
}
