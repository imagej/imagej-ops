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

package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.image.integral.IntegralCursor;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.Interval;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.view.composite.Composite;

/**
 * {@link Op} to calculate the {@code stats.mean} from an integral image using a
 * specialized {@code Cursor} implementation.
 *
 * @param <I> input type
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Stats.IntegralMean.class)
public class IntegralMean<I extends RealType<I>> extends
	AbstractBinaryComputerOp<RectangleNeighborhood<Composite<I>>, Interval, DoubleType>
	implements Ops.Stats.IntegralMean
{

	@Override
	public void compute2(RectangleNeighborhood<Composite<I>> input1, Interval input2,
		DoubleType output)
	{
		// computation according to	https://en.wikipedia.org/wiki/Summed_area_table
		final IntegralCursor<Composite<I>> cursor = new IntegralCursor<>(input1);
		int dimensions = input1.numDimensions();
		
		// Compute \sum (-1)^{dim - ||cornerVector||_{1}} * I(x^{cornerVector})
		final DoubleType sum = new DoubleType();
		sum.setZero();

		// Convert from input to return type
		final Converter<I, DoubleType> conv = new RealDoubleConverter<>();
		final DoubleType valueAsDoubleType = new DoubleType();
		
		while ( cursor.hasNext() )
		{
			final I value = cursor.next().get(0).copy();
			conv.convert(value, valueAsDoubleType);
			
			// Obtain the cursor position encoded as corner vector
			int cornerInteger = cursor.getCornerRepresentation();
			
			// Determine if the value has to be added (factor==1) or subtracted (factor==-1)
			DoubleType factor = new DoubleType(Math.pow(-1.0d, dimensions - IntegralMean.norm(cornerInteger)));
			valueAsDoubleType.mul(factor);
			
			sum.add(valueAsDoubleType);
		}

		// Compute overlap
		int area = IntegralMean.overlap(Intervals.expand(input1, -1l), input2);

		// Compute mean by dividing the sum divided by the number of elements
		sum.div(new DoubleType(area));

		output.set(sum);
	}

	/**
	 * Compute the overlap between to intervals.
	 * 
	 * @param interval1
	 * @param interval2
	 * @return area/volume/etc
	 */
	public static int overlap(Interval interval1, Interval interval2)
	{
		Interval intersection = Intervals.intersect(interval1, interval2);
		return (int) Intervals.numElements(intersection);
	}

	/**
	 * Computes L1 norm of the position of an {@code IntegralCursor}. Computation
	 * is based on determining the number of 1 bits in the position.
	 * 
	 * @param cornerPosition position vector of an {@code IntegralCursor} encoded
	 *          as integer
	 * @return L1 norm of the position
	 */
	public static int norm(int cornerPosition) {
		return Integer.bitCount(cornerPosition);
	}
	
}
