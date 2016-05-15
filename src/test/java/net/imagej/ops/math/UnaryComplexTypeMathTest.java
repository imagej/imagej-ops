
package net.imagej.ops.math;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.math.UnaryComplexTypeMath.ComplexExp;
import net.imagej.ops.math.exp.ComplexExpMap;
import net.imglib2.FinalDimensions;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.complex.ComplexDoubleType;

import org.junit.Test;

public class UnaryComplexTypeMathTest extends AbstractOpTest {

	@Test
	public void testExpComplex() {
		final ComplexDoubleType in = new ComplexDoubleType(1, 1);
		final ComplexDoubleType out = new ComplexDoubleType();
		ops.run(ComplexExp.class, out, in);

		assertEquals(out.getRealDouble(), 1.4687, 0.0001);
		assertEquals(out.getImaginaryDouble(), 2.2874, 0.0001);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testExpComplexMap() {

		Img<ComplexDoubleType> in = ops.create().img(new FinalDimensions(2, 4),
			new ComplexDoubleType());

		for (ComplexDoubleType c : in) {
			c.setReal(1);
			c.setImaginary(1);
		}

		Img<ComplexDoubleType> out = (Img<ComplexDoubleType>) ops.run(
			ComplexExpMap.class, in);

		for (ComplexDoubleType c : out) {
			assertEquals(c.getRealDouble(), 1.4687, 0.0001);
			assertEquals(c.getImaginaryDouble(), 2.2874, 0.0001);
		}

	}

}
