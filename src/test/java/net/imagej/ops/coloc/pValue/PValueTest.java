
package net.imagej.ops.coloc.pValue;

import static org.junit.Assert.assertEquals;

import java.util.function.BiFunction;

import net.imagej.ops.coloc.ColocalisationTest;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;

/**
 * Tests {@link PValue}.
 *
 * @author Ellen T Arena
 */
public class PValueTest extends ColocalisationTest {

	/*
	 * Tests 
	 */
	@Test
	public void testPValuePerfectColoc() {
		assertColoc(0.0, 1, 0, 0, 0, 0, 0, 0);
	}
	@Test
	public void testPValueNoColoc() {
		assertColoc(1.0, 0, 1, 2, 3, 4, 5);
	}
	@Test
	public void testPValueSomeColoc() {
		assertColoc(0.6, 0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
	}

/**
 * Function is called once with original images. Thereafter, 
 * each call is with a shuffled version of the first image.
 * @param expectedPValue
 * @param result
 */
	private void assertColoc(double expectedPValue, double... result) {
		Img<FloatType> ch1 = ArrayImgs.floats(1); // NB: Images will be ignored.
		Img<FloatType> ch2 = ch1;

		// Mock the underlying op.
		final int[] count = {0};
		BinaryFunctionOp<Iterable<FloatType>, Iterable<FloatType>, Double> op = op((input1, input2) -> {
			return result[count[0]++];
		});
		Double actualPValue = ops.coloc().pValue(ch1, ch2, op, result.length - 1);
		assertEquals(expectedPValue, actualPValue, 0.0);
	}

	// -- Utility methods --

	private static <I1, I2, O> BinaryFunctionOp<I1, I2, O> op(final BiFunction<I1, I2, O> function) {		
		return new AbstractBinaryFunctionOp<I1, I2, O>() {
	
			@Override
			public O calculate(final I1 input1, final I2 input2) {
				return function.apply(input1, input2);
			}
		};
	}
}
