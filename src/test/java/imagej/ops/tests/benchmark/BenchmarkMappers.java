
package imagej.ops.tests.benchmark;

import imagej.module.Module;
import imagej.ops.Function;
import imagej.ops.map.Mapper;
import imagej.ops.map.MapperII;
import imagej.ops.map.ThreadedMapper;
import imagej.ops.map.ThreadedMapperII;
import imagej.ops.tests.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Benchmarking various implementations of mappers. Benchmarked since now:
 * {@link Mapper}, {@link MapperII}, {@link ThreadedMapper},
 * {@link ThreadedMapperII}
 * 
 * @author Christian Dietz
 */
public class BenchmarkMappers extends AbstractOpTest {

	private Img<ByteType> in;
	private int numRuns;
	private Img<ByteType> out;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 10000, 10000);
		out = generateByteTestImg(false, 10000, 10000);
		numRuns = 10;
	}

	@Test
	public void pixelWiseTestMapper() {
		final Module asModule = ops.module(new Mapper<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[Mapper] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestMapperII() {
		final Module asModule = ops.module(new MapperII<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[MapperII] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestThreadedMapper() {
		final Module asModule =
			ops.module(new ThreadedMapper<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[ThreadedMapper] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	@Test
	public void pixelWiseTestThreadedMapperII() {
		final Module asModule =
			ops.module(new ThreadedMapperII<ByteType, ByteType>());

		asModule.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		asModule.setInput("in", in);
		asModule.setInput("out", out);

		System.out.println("[ThreadedMapperII] Runtime " +
			asMilliSeconds(bestOf(asModule, numRuns)) + "!");
	}

	private double asMilliSeconds(final long nanoTime) {
		return nanoTime / 1000.0d / 1000.d;
	}

	public class DummyPixelOp<T extends RealType<T>, V extends RealType<V>>
		extends Function<T, V>
	{

		double constant = -5;

		@Override
		public V compute(final T input, final V output) {
			output.setReal(input.getRealDouble() + constant);
			return output;
		}

		@Override
		public Function<T, V> copy() {
			return new DummyPixelOp<T, V>();
		}
	}
}
