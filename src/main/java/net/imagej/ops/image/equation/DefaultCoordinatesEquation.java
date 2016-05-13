/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.image.equation;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * An equation operation which computes image values from coordinates using
 * another op. The op will compute f(c[0], [1], ... c[n]) where c represents the
 * coordinates
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Image.Equation.class)
public class DefaultCoordinatesEquation<T extends RealType<T>, N extends Number>
	extends
	AbstractUnaryComputerOp<UnaryFunctionOp<long[], N>, IterableInterval<T>>
	implements CoordinatesEquationOp<T, N>
{

	@Override
	public void compute(final UnaryFunctionOp<long[], N> op,
		final IterableInterval<T> output)
	{

		final Cursor<T> c = output.localizingCursor();
		final long[] pos = new long[output.numDimensions()];

		// loop through all pixels, set the current position and call the op
		while (c.hasNext()) {
			c.fwd();
			c.localize(pos);

			for (int i = 0; i < output.numDimensions(); i++) {

				op.calculate(pos);

				c.get().setReal(op.calculate(pos).floatValue());

			}

		}
	}

}
