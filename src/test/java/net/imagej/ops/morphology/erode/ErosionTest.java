
package net.imagej.ops.morphology.erode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.morphology.Erosion;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.HorizontalLineShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link net.imagej.ops.Ops.Morphology.Erode}
 * 
 * @author Leon Yang
 */
public class ErosionTest extends AbstractOpTest {

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
	public void testSingleErode() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(DefaultErode.class,
			Img.class, in, shape, false);
		final Img<ByteType> out2 = Erosion.erode(in, shape, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testSingleErodeBitType() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<BitType> out1 = (Img<BitType>) ops.run(DefaultErode.class,
			Img.class, bitIn, shape, false);
		final Img<BitType> out2 = Erosion.erode(bitIn, shape, 1);
		final Cursor<BitType> c1 = out1.cursor();
		final Cursor<BitType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testSingleErodeFull() {
		final Shape shape = new DiamondShape(1);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(DefaultErode.class,
			Img.class, in, shape, true);
		final Img<ByteType> out2 = Erosion.erodeFull(in, shape, 1);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

//	@Test
	public void testListErode() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		final Img<ByteType> out2 = in.copy();
		Erosion.erode(Views.extendValue(in, new ByteType((byte) -128)), out2,
			shapes, 1);
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListErode.class, IterableInterval.class, in, shapes, false);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}

	@Test
	public void testListErodeFull() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		final Img<ByteType> out2 = Erosion.erodeFull(in, shapes, 1);
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListErode.class, IterableInterval.class, in, shapes, true);
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();
		while (c1.hasNext())
			assertEquals(c1.next().get(), c2.next().get());
	}
}
