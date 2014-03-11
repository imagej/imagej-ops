
package imagej.ops.map.view;

import static org.junit.Assert.assertEquals;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.arithmetic.add.AddConstantToNumericType;
import imagej.ops.map.MapII2View;
import imagej.ops.map.MapRA2View;
import imagej.ops.map.MapRAI2View;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

public class MapViewTest extends AbstractOpTest {

	private Img<ByteType> in;

	private Op op;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		op =
			ops.op(AddConstantToNumericType.class, null, NumericType.class,
				new ByteType((byte) 10));
	}

	@Test
	public void testRandomAccessibleView() {
		@SuppressWarnings("unchecked")
		final RandomAccessible<ByteType> res =
			(RandomAccessible<ByteType>) ops.run(MapRA2View.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable =
			Views.iterable(Views.interval(res, in)).cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}

	}

	@Test
	public void testRandomAccessibleIntervalView() {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<ByteType> res =
			(RandomAccessibleInterval<ByteType>) ops.run(MapRAI2View.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable = Views.iterable(res).cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}

	}

	@Test
	public void testIterableIntervalView() {
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> res =
			(IterableInterval<ByteType>) ops.run(MapII2View.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable = res.cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}
	}
}
