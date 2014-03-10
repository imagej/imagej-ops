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
