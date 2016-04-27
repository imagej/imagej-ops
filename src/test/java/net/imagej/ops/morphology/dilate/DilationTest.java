
package net.imagej.ops.morphology.dilate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.morphology.Dilation;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.HorizontalLineShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link net.imagej.ops.Ops.Morphology.Dilate}
 * 
 * @author Leon Yang
 */
public class DilationTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<BitType> bitIn;

	@Before
	public void initialize() {
		in = generateByteArrayTestImg(true, 10, 10);
		bitIn = ArrayImgs.bits(10, 10);
		final Random rnd = new Random(0x123456789caffee1L);
		for (BitType px : bitIn)
			px.set(rnd.nextBoolean());
	}

	@Test
	public void testSingleDilate() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(DefaultDilate.class,
			Img.class, in, shape, false);
		final Img<ByteType> out2 = Dilation.dilate(in, shape, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testSingleDilateBitType() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<BitType> out1 = (Img<BitType>) ops.run(DefaultDilate.class,
			Img.class, bitIn, shape, false);
		final Img<BitType> out2 = Dilation.dilate(bitIn, shape, 1);
		final Cursor<BitType> c1 = out1.cursor();
		final Cursor<BitType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testSingleDilateFull() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(DefaultDilate.class,
			Img.class, in, shape, true);
		final Img<ByteType> out2 = Dilation.dilateFull(in, shape, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testListDilate() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListDilate.class, IterableInterval.class, in, shapes, false);
		final Img<ByteType> out2 = Dilation.dilate(in, shapes, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testListDilateFull() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListDilate.class, IterableInterval.class, in, shapes, true);
		final Img<ByteType> out2 = Dilation.dilateFull(in, shapes, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}
}
