/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
   this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.image.equation;

import java.util.function.DoubleBinaryOperator;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * An equation operation which computes image values from x and y coordinates
 * using a binary lambda. The op calculates f(x,y). For example to compute
 * f(x,y)=x^2 + y^2 call:
 * {@code ops.image().equation(image2, (x, y) -> Math.pow(x, 2)
 * + Math.pow(y, 2)); }
 *
 * @author Brian Northan
 */
@Plugin(type = Ops.Image.Equation.class)
public class DefaultXYEquation<T extends RealType<T>> extends
	AbstractUnaryComputerOp<DoubleBinaryOperator, IterableInterval<T>> implements
	DoubleBinaryEquationOp<T>
{

	private UnaryComputerOp<UnaryFunctionOp<long[], Double>, IterableInterval<T>> equation;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();
		equation = (UnaryComputerOp) Computers.unary(ops(),
			DefaultCoordinatesEquation.class, IterableInterval.class,
			UnaryFunctionOp.class);
	}

	@Override
	public void compute(final DoubleBinaryOperator lambda,
		final IterableInterval<T> output)
	{

		// create an op that calls the binary operator with the first two
		// coordinates
		final UnaryFunctionOp<long[], Double> op =
			new AbstractUnaryFunctionOp<long[], Double>()
		{

				@Override
				public Double calculate(final long[] coords) {
					return lambda.applyAsDouble(coords[0], coords[1]);
				}

			};

		equation.compute(op, output);

	}
}
