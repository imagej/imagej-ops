
package net.imagej.ops.mask.maskHigh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.stream.LongStream;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.LongType;

import org.junit.Test;

/**
 * Tests for {@link DefaultMaskHigh}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class DefaultMaskHighTest extends AbstractOpTest {

	@Test
	public void testCompute2() throws Exception {
		final LongType value = new LongType(6L);
		// Create a 4x4x4 test image with values from 0 to 7
		final Img<LongType> img = ArrayImgs.longs(4, 4, 4);
		final Iterator<Long> longIterator = LongStream.iterate(0, i -> (i + 1) % 8)
			.iterator();
		img.cursor().forEachRemaining(e -> e.set(longIterator.next()));

		@SuppressWarnings("unchecked")
		List<LongType> result = (List<LongType>) ops.run(DefaultMaskHigh.class,
			img, value);

		assertEquals("Wrong number of elements in result iterable", 16, result
			.size());
		assertTrue("All elements refer the same object (shallow copy?)", result
			.stream().distinct().count() > 1);
		assertTrue("Wrong values in result iterable", result.stream().allMatch(
			e -> e.get() >= value.get()));
	}
}
