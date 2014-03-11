
package imagej.ops.join;

import imagej.ops.AbstractFunction;
import imagej.ops.AbstractInplaceFunction;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.map.FunctionMap;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

public class JoinTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Op inplaceOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		inplaceOp = ops.op(FunctionMap.class, in, new AddOneInplace());
//		buffer = generateByteTestImg(false, dims);
//		out = generateByteTestImg(false, dims);
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
