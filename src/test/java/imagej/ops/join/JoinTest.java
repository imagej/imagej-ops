
package imagej.ops.join;

import imagej.ops.AbstractFunction;
import imagej.ops.AbstractInplaceFunction;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.map.Map;
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
		inplaceOp = ops.op(Map.class, Img.class, new AddOneInplace());
		functionalOp =
			ops.op(Map.class, Img.class, Img.class, new AddOneFunctional());
	}

	@Test
	public void testInplaceJoin() {
		final Op op = ops.op(InplaceJoin.class, in, inplaceOp, inplaceOp);
		op.run();

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testFunctionInplaceJoin() {
		final Op op =
			ops.op(FunctionInplaceJoin.class, out, in, functionalOp, inplaceOp);
		op.run();

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(2, c.next().get());
		}
	}

	@Test
	public void testInplaceFunctionJoin() {
		final Op op =
			ops.op(InplaceFunctionJoin.class, out, in, inplaceOp, functionalOp);
		op.run();

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(2, c.next().get());
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
