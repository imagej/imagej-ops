
package imagej.ops.tests;

import static org.junit.Assert.assertTrue;
import imagej.module.Module;
import imagej.ops.UnaryFunction;
import imagej.ops.map.Mapper;
import imagej.ops.map.MapperII;
import imagej.ops.map.ThreadedMapper;
import imagej.ops.map.ThreadedMapperII;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing multi threaded implementation ({@link ThreadedMapper} and
 * {@link ThreadedMapperII}) of the mappers. Assumption: Naive Implementation of
 * {@link Mapper} works fine.
 * 
 * @author Christian Dietz
 */
public class ThreadedMapperTests extends AbstractOpTest {

	private Img<ByteType> in;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 10, 10);
	}

	@Test
	public void testMultiThreadedMapper() {

		final Img<ByteType> outNaive = generateByteTestImg(false, 10, 10);
		final Img<ByteType> outThreaded = generateByteTestImg(false, 10, 10);
		final Img<ByteType> outThreadedII = generateByteTestImg(false, 10, 10);

		final Module naiveMapper = ops.asModule(new MapperII<ByteType, ByteType>());
		naiveMapper.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		naiveMapper.setInput("in", in);
		naiveMapper.setInput("out", outNaive);

		naiveMapper.run();

		final Module threadedMapper =
			ops.asModule(new MapperII<ByteType, ByteType>());
		threadedMapper.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		threadedMapper.setInput("in", in);
		threadedMapper.setInput("out", outThreaded);

		threadedMapper.run();

		final Module threadedMapperII =
			ops.asModule(new MapperII<ByteType, ByteType>());
		threadedMapperII.setInput("func", new DummyPixelOp<ByteType, ByteType>());
		threadedMapperII.setInput("in", in);
		threadedMapperII.setInput("out", outThreadedII);

		threadedMapperII.run();

		final Cursor<ByteType> cursor1 = outNaive.cursor();
		final Cursor<ByteType> cursor2 = outThreaded.cursor();
		final Cursor<ByteType> cursor3 = outThreadedII.cursor();

		// test for consistency as we know that outNaive works.
		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			cursor3.fwd();

			assertTrue(cursor1.get().get() == cursor2.get().get());
			assertTrue(cursor1.get().get() == cursor3.get().get());
		}
	}

	private class DummyPixelOp<T extends RealType<T>, V extends RealType<V>>
		extends UnaryFunction<T, V>
	{

		double constant = -5;

		@Override
		public V compute(final T input, final V output) {
			output.setReal(input.getRealDouble() + constant);
			return output;
		}

		@Override
		public UnaryFunction<T, V> copy() {
			return new DummyPixelOp<T, V>();
		}
	}
}
