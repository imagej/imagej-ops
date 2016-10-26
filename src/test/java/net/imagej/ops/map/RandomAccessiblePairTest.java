package net.imagej.ops.map;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;

import org.junit.Test;

public class RandomAccessiblePairTest extends AbstractOpTest {

	@Test
	public void test() {
		// Extend input with Views.extend
		Img<ByteType> in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		OutOfBoundsFactory<ByteType, RandomAccessibleInterval<ByteType>> outOfBoundsFactory =
			new OutOfBoundsBorderFactory<>();
		RandomAccessible<ByteType> extendedIn =
//				Views.interval(Views.extend(in, outOfBoundsFactory), in); // FAILS
				Views.extend(in, outOfBoundsFactory); // WORKS

		// Create RandomAccessiblePair
		final RandomAccessible<Neighborhood<ByteType>> safe = new RectangleShape(1,
			false).neighborhoodsRandomAccessibleSafe(extendedIn);
		RandomAccessible<Pair<Neighborhood<ByteType>, ByteType>> pair = Views.pair(
			safe, extendedIn);

		// Set position out of bounds
		RandomAccess<Pair<Neighborhood<ByteType>, ByteType>> randomAccess = pair
			.randomAccess();
		randomAccess.setPosition(new int[] { -2, -2 });

		// Get value from Neighborhood via RandomAccessiblePair
		Pair<Neighborhood<ByteType>, ByteType> pair2 = randomAccess.get();
		Neighborhood<ByteType> neighborhood = pair2.getA();
		Cursor<ByteType> cursor = neighborhood.cursor();
		cursor.next().getRealDouble();
	}

}
