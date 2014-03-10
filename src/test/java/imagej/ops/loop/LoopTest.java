
package imagej.ops.loop;

import imagej.ops.AbstractFunction;
import imagej.ops.AbstractInplaceFunction;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.map.Mapper;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing functional and inplace loops
 * 
 * @author Christian Dietz
 */
public class LoopTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Img<ByteType> buffer;

	private int numIterations;
	private Op functionalOp;
	private Op inplaceOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		buffer = generateByteTestImg(false, dims);
		out = generateByteTestImg(false, dims);
		numIterations = 10;
		functionalOp = ops.op(Mapper.class, out, in, new AddOneFunctional());
		inplaceOp = ops.op(Mapper.class, in, new AddOneInplace());
	}

	@Test
	public void testInplace() {
		ops.run(InplaceLoop.class, in, inplaceOp, numIterations);

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalEven() {
		ops.run(FunctionLoop.class, out, in, functionalOp, buffer, numIterations);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalOdd() {
		ops.run(FunctionLoop.class, out, in, functionalOp, buffer,
			numIterations - 1);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations - 1, c.next().get());
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

	class AddOneFunctional extends AbstractFunction<ByteType, ByteType> {

		@Override
		public ByteType compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
			return output;
		}
	}
}
