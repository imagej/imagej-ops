
package imagej.ops.tests;

import static org.junit.Assert.assertEquals;
import imagej.ops.Op;
import imagej.ops.projectors.DefaultProjector;
import imagej.ops.projectors.parallel.DefaultProjectorP;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

public class ProjectorTest extends AbstractOpTest {

	private final int PROJECTION_DIM = 2;

	private Img<ByteType> in;
	private Img<ByteType> out1;
	private Img<ByteType> out2;
	private Op op;

	@Before
	public void initImg() {
		in = generateByteTestImg(false, 10, 10, 10);

		// fill in with ones
		final Cursor<ByteType> cursor = in.cursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.get().set((byte) 1);
		}

		out1 = generateByteTestImg(false, 10, 10);
		out2 = generateByteTestImg(false, 10, 10);

		op = ops.op("sum", null, null);
	}

	@Test
	public void testProjector() {
		ops.run(DefaultProjector.class, out1, in, op, PROJECTION_DIM);
		ops.run(DefaultProjectorP.class, out2, in, op, PROJECTION_DIM);

		// test
		final Cursor<ByteType> out1Cursor = out1.cursor();
		final Cursor<ByteType> out2Cursor = out2.cursor();

		while (out1Cursor.hasNext()) {
			out1Cursor.fwd();
			out2Cursor.fwd();

			assertEquals(out1Cursor.get().get(), in.dimension(PROJECTION_DIM));
			assertEquals(out2Cursor.get().get(), in.dimension(PROJECTION_DIM));
		}
	}
}
