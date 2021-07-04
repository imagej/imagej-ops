
package net.imagej.ops.filter.addUniformNoise;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link AddUniformNoiseRealType}.
 *
 * @author Gabriel Selzer
 */
public class AddUniformNoiseTest extends AbstractOpTest {

	/**
	 * Regression test for floating point values
	 */
	@Test
	public void realTypeRegressionTest() {
		UnaryComputerOp<DoubleType, DoubleType> noiseFunc = Computers.unary(ops,
			Ops.Filter.AddUniformNoise.class, DoubleType.class, DoubleType.class, 0d,
			2d, 0xabcdef1234567890L);

		double[] actual = new double[9];
		DoubleType temp = new DoubleType();
		for (int i = 0; i < actual.length; i++) {
			noiseFunc.compute(new DoubleType(254), temp);
			actual[i] = temp.getRealDouble();
		}

		double[] expected = { 254.10073053454738, 255.88056371733813,
			255.15987104925694, 255.32176185269049, 254.92408976012726,
			255.04150474558148, 255.4924837511821, 254.66400205149753,
			254.45238896293924 };
		Assert.assertArrayEquals(expected, actual, 1e-6);
	}

	/**
	 * Ensures that the Op wraps correctly to the minimum of the data type when
	 * clampOutput is set to false
	 */
	@Test
	public void wrappingUpperEndRegressionTest() {
		UnaryComputerOp<UnsignedByteType, UnsignedByteType> noiseFunc = Computers
			.unary(ops, Ops.Filter.AddUniformNoise.class, UnsignedByteType.class,
				UnsignedByteType.class, 0l, 3l, false, 0xabcdef1234567890L);

		int[] actual = new int[9];
		UnsignedByteType temp = new UnsignedByteType();
		for (int i = 0; i < actual.length; i++) {
			noiseFunc.compute(new UnsignedByteType(254), temp);
			actual[i] = temp.get();
		}

		int[] expected = { 0, 0, 1, 0, 255, 254, 0, 0, 0 };
		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Ensures that the Op wraps correctly to the maximum of the data type when
	 * clampOutput is set to false
	 */
	@Test
	public void wrappingLowerEndRegressionTest() {
		UnaryComputerOp<UnsignedByteType, UnsignedByteType> noiseFunc = Computers
			.unary(ops, Ops.Filter.AddUniformNoise.class, UnsignedByteType.class,
				UnsignedByteType.class, -3l, 0l, false, 0xabcdef1234567890L);

		int[] actual = new int[9];
		UnsignedByteType temp = new UnsignedByteType();
		for (int i = 0; i < actual.length; i++) {
			noiseFunc.compute(new UnsignedByteType(0), temp);
			actual[i] = temp.get();
		}

		int[] expected = { 255, 255, 0, 255, 254, 253, 255, 255, 255 };
		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Ensures that the Op clamps to the minimum of the data type then clampOutput
	 * is set to true
	 */
	@Test
	public void clampingLowerEndRegressionTest() {
		UnaryComputerOp<UnsignedByteType, UnsignedByteType> noiseFunc = Computers
			.unary(ops, Ops.Filter.AddUniformNoise.class, UnsignedByteType.class,
				UnsignedByteType.class, -3l, 0l, 0xabcdef1234567890L);

		int[] actual = new int[9];
		UnsignedByteType temp = new UnsignedByteType();
		for (int i = 0; i < actual.length; i++) {
			noiseFunc.compute(new UnsignedByteType(0), temp);
			actual[i] = temp.get();
		}

		int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Assert.assertArrayEquals(expected, actual);
	}

}
