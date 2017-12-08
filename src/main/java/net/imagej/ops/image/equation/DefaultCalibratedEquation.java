/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * An "calibrated equation" operation which computes image values from real
 * coordinates using an op.
 * 
 * @author Brian Northan
 */
@Plugin(type = Ops.Image.Equation.class)
public class DefaultCalibratedEquation<T extends RealType<T>> extends
	AbstractCalibratedEquation<UnaryFunctionOp<double[], Double>, T> implements
	CalibratedEquationOp<T>
{

	@Override
	public void compute1(final UnaryFunctionOp<double[], Double> op,
		final IterableInterval<T> output)
	{

		final Cursor<T> c = output.localizingCursor();
		final long[] pos = new long[output.numDimensions()];
		final double[] realCoordinates = new double[output.numDimensions()];

		while (c.hasNext()) {
			c.fwd();
			c.localize(pos);

			for (int i = 0; i < output.numDimensions(); i++) {
				realCoordinates[i] = getOrigin()[i] + getCalibration()[i] * pos[i];

				c.get().setReal(op.compute1(realCoordinates));

			}

		}
	}

}
