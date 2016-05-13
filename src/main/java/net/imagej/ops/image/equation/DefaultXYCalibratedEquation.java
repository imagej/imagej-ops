
package net.imagej.ops.image.equation;

import java.util.function.DoubleBinaryOperator;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * A "calibrated equation" operation which computes image values from x and y
 * coordinates using a binary lambda.
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Image.Equation.class)
public class DefaultXYCalibratedEquation<T extends RealType<T>> extends
	AbstractCalibratedEquation<DoubleBinaryOperator, T> implements
	XYCalibratedEquationOp<T>
{

	private UnaryComputerOp<UnaryFunctionOp<double[], Double>, IterableInterval<T>> equation;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();
		equation = (UnaryComputerOp) Computers.unary(ops(),
			DefaultCalibratedEquation.class, IterableInterval.class,
			UnaryFunctionOp.class, getOrigin(), getCalibration());
	}

	@Override
	public void compute1(DoubleBinaryOperator lambda,
		final IterableInterval<T> output)
	{

		UnaryFunctionOp<double[], Double> op =
			new AbstractUnaryFunctionOp<double[], Double>()
		{

				@Override
				public Double compute1(double[] coords) {
					return lambda.applyAsDouble(coords[0], coords[1]);
				}

			};

		equation.compute1(op, output);

	}
}
