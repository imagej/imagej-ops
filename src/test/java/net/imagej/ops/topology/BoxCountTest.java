
package net.imagej.ops.topology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests for the {@link BoxCount} op
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class BoxCountTest extends AbstractOpTest {

	private static final Long MAX_SIZE = 16L;
	private static final Long MIN_SIZE = 2L;
	private static final double SCALING = 2.0;
	private static final long ITERATIONS = 4;
	private static final long DIMENSIONS = 2;
	private static final long[] TEST_DIMS = LongStream.generate(() -> MAX_SIZE)
		.limit(DIMENSIONS).toArray();
	private static final double[] EXPECTED_SIZES = DoubleStream.iterate(MAX_SIZE,
		d -> d / SCALING).map(d -> -Math.log(d)).limit(ITERATIONS).toArray();

	@Test
	public void testAllBackground() throws Exception {
		// SETUP
		final double expectedCount = Math.log(0.0);
		final Img<BitType> img = ArrayImgs.bits(TEST_DIMS);

		// EXECUTE
		final List<ValuePair<DoubleType, DoubleType>> points = ops.topology()
			.boxCount(img, MAX_SIZE, MIN_SIZE, SCALING);

		// VERIFY
		assertNotNull(points);
		assertEquals(ITERATIONS, points.size());
		for (int i = 0; i < ITERATIONS; i++) {
			assertEquals(expectedCount, points.get(i).a.get(), 1e-12);
			assertEquals(EXPECTED_SIZES[i], points.get(i).b.get(), 1e-12);
		}
	}

	@Test
	public void testAllForeground() {
		// SETUP
		final double scalingPow = DoubleStream.generate(() -> SCALING).limit(
			DIMENSIONS).reduce((i, j) -> i * j).orElse(0);
		final double[] expectedCounts = DoubleStream.iterate(1.0, i -> i *
			scalingPow).map(Math::log).limit(ITERATIONS).toArray();
		final Img<BitType> img = ArrayImgs.bits(TEST_DIMS);
		img.forEach(BitType::setOne);

		// EXECUTE
		final List<ValuePair<DoubleType, DoubleType>> points = ops.topology()
			.boxCount(img, MAX_SIZE, MIN_SIZE, SCALING);

		// VERIFY
		for (int i = 0; i < ITERATIONS; i++) {
			assertEquals(expectedCounts[i], points.get(i).a.get(), 1e-12);
			assertEquals(EXPECTED_SIZES[i], points.get(i).b.get(), 1e-12);
		}
	}

	@Test
	public void testHyperCube() {
		// SETUP
		final double[] expectedSizes = DoubleStream.of(4, 2, 1).map(i -> -Math.log(
			i)).toArray();
		final double[] expectedCounts = DoubleStream.of(1, 16, 16).map(Math::log)
			.toArray();
		final Img<BitType> img = ArrayImgs.bits(4, 4, 4, 4);
		final IntervalView<BitType> hyperView = Views.offsetInterval(img,
			new long[] { 1, 1, 1, 1 }, new long[] { 2, 2, 2, 2 });
		hyperView.forEach(BitType::setOne);

		// EXECUTE
		final List<ValuePair<DoubleType, DoubleType>> points = ops.topology()
			.boxCount(img, 4L, 1L, 2.0);

		// VERIFY
		for (int i = 0; i < expectedSizes.length; i++) {
			assertEquals(expectedSizes[i], points.get(i).b.get(), 1e-12);
			assertEquals(expectedCounts[i], points.get(i).a.get(), 1e-12);
		}
	}

	/**
	 * Test box counting with a hyper cube and one grid translation (should find a
	 * better fit than in @see {@link #testHyperCube()})
	 */
	@Test
	public void testHyperCubeTranslations() {
		final double[] expectedSizes = DoubleStream.of(4, 2, 1).map(i -> -Math.log(
			i)).toArray();
		final double[] expectedCounts = DoubleStream.of(1, 1, 16).map(Math::log)
			.toArray();
		final Img<BitType> img = ArrayImgs.bits(4, 4, 4, 4);
		final IntervalView<BitType> hyperView = Views.offsetInterval(img,
			new long[] { 1, 1, 1, 1 }, new long[] { 2, 2, 2, 2 });
		hyperView.forEach(BitType::setOne);

		// EXECUTE
		final List<ValuePair<DoubleType, DoubleType>> points = ops.topology()
			.boxCount(img, 4L, 1L, 2.0, 1L);

		// VERIFY
		for (int i = 0; i < expectedSizes.length; i++) {
			assertEquals(expectedSizes[i], points.get(i).b.get(), 1e-12);
			assertEquals(expectedCounts[i], points.get(i).a.get(), 1e-12);
		}
	}

	@Test
	public void testOneVoxel() {
		// SETUP
		final PrimitiveIterator.OfDouble sizes = DoubleStream.of(9, 3, 1).map(
			i -> -Math.log(i)).iterator();
		final PrimitiveIterator.OfDouble counts = DoubleStream.of(1, 1, 1).map(
			Math::log).iterator();
		final Img<BitType> img = ArrayImgs.bits(9, 9, 9);
		final RandomAccess<BitType> access = img.randomAccess();
		access.setPosition(new long[] { 4, 4, 4 });
		access.get().setOne();

		// EXECUTE
		final List<ValuePair<DoubleType, DoubleType>> points = ops.topology()
			.boxCount(img, 9L, 3L, 3.0);

		// VERIFY
		points.forEach(p -> {
			assertEquals(p.a.get(), counts.next(), 1e-12);
			assertEquals(p.b.get(), sizes.next(), 1e-12);
		});
	}
}
