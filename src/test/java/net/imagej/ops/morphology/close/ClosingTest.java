
package net.imagej.ops.morphology.close;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.morphology.Closing;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.HorizontalLineShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link net.imagej.ops.Ops.Morphology.Close}
 * 
 * @author Leon Yang
 */
public class ClosingTest extends AbstractOpTest {

	private Img<ByteType> in;

	@Before
	public void initialize() {
		in = generateByteArrayTestImg(true, 10, 10);
	}

	@Test
	public void testSingleClose() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(ListClose.class,
			Img.class, in, shape);
		final Img<ByteType> out2 = Closing.close(in, shape, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testListClose() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListClose.class, IterableInterval.class, in, shapes);
		final Img<ByteType> out2 = Closing.close(in, shapes, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}
}
