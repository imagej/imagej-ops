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

package net.imagej.ops.stats;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.image.integral.IntegralCursor;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.view.composite.Composite;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code stats.variance} from an integral image
 * using a specialized {@code Cursor} implementation.
 *
 * @param <I> input type
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Stats.IntegralVariance.class)
public class IntegralVariance<I extends RealType<I>> extends
	AbstractUnaryComputerOp<RectangleNeighborhood<Composite<I>>, DoubleType>
	implements Ops.Stats.IntegralVariance
{

	@Override
	public void compute(final RectangleNeighborhood<Composite<I>> input,
		final DoubleType output)
	{
		// computation according to
		// https://en.wikipedia.org/wiki/Summed_area_table
		final IntegralCursor<Composite<I>> cursorS1 = new IntegralCursor<>(input);
		final int dimensions = input.numDimensions();

		// Compute \sum (-1)^{dim - ||cornerVector||_{1}} * I(x^{cornerVector})
		final DoubleType sum1 = new DoubleType();
		sum1.setZero();

		// Convert from input to return type
		final Converter<I, DoubleType> conv = new RealDoubleConverter<>();

		// Compute \sum (-1)^{dim - ||cornerVector||_{1}} * I(x^{cornerVector})
		final DoubleType sum2 = new DoubleType();
		sum2.setZero();

		final DoubleType valueAsDoubleType = new DoubleType();

		while (cursorS1.hasNext()) {
			final Composite<I> compositeValue = cursorS1.next();
			final I value1 = compositeValue.get(0).copy();
			conv.convert(value1, valueAsDoubleType);

			// Obtain the cursor position encoded as corner vector
			final int cornerInteger1 = cursorS1.getCornerRepresentation();

			// Determine if the value has to be added (factor==1) or subtracted
			// (factor==-1)
			final DoubleType factor = new DoubleType(Math.pow(-1.0d, dimensions -
				IntegralMean.norm(cornerInteger1)));
			valueAsDoubleType.mul(factor);

			sum1.add(valueAsDoubleType);

			final I value2 = compositeValue.get(1).copy();
			conv.convert(value2, valueAsDoubleType);

			// Determine if the value has to be added (factor==1) or subtracted
			// (factor==-1)
			valueAsDoubleType.mul(factor);

			sum2.add(valueAsDoubleType);
		}

		final int area = (int) Intervals.numElements(Intervals.expand(input, -1l));

		valueAsDoubleType.set(area); // NB: Reuse available DoubleType
		sum1.mul(sum1);
		sum1.div(valueAsDoubleType); // NB

		sum2.sub(sum1);
		valueAsDoubleType.sub(new DoubleType(1)); // NB
		sum2.div(valueAsDoubleType); // NB

		output.set(sum2);
	}

}
