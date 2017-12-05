
package net.imagej.ops.mask.maskRange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.junit.Test;

/**
 * Tests for {@link DefaultMaskRange}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class DefaultMaskRangeTest extends AbstractOpTest {

	@Test
	public void testCompute2() throws Exception {
		LongType min = new LongType(2L);
		LongType max = new LongType(5L);
		final Pair limits = new ValuePair<>(min, max);
		// Create a 2x2x2x2x2 test image with values from 0 to 7
		final Img<LongType> img = ArrayImgs.longs(2, 2, 2, 2, 2);
		final Iterator<Long> iterator = LongStream.iterate(0, i -> (i + 1) % 8)
			.iterator();
		img.cursor().forEachRemaining(e -> e.set(iterator.next()));

		@SuppressWarnings("unchecked")
		final List<LongType> result = (List<LongType>) ops.run(
			DefaultMaskRange.class, img, limits);

		assertEquals("Wrong number of elements in the result iterable", 16, result
			.size());
		assertTrue("All elements refer the same object (shallow copy?)", result
			.stream().distinct().count() > 1);
		assertTrue("Wrong values in the result iterable", result.stream().allMatch(
			e -> e.compareTo(min) >= 0 && e.compareTo(max) <= 0));
	}
}
