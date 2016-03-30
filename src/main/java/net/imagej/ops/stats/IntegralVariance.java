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
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.Composite;

/**
 * {@link Op} to calculate the {@code stats.variance} from an integral image using a
 * specialized {@code Cursor} implementation.
 *
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> input type
 */
@Plugin(type = Ops.Stats.IntegralVariance.class)
public class IntegralVariance<I extends RealType<I>> extends
	AbstractBinaryComputerOp<RectangleNeighborhood<Composite<I>>, Interval, DoubleType>
	implements Ops.Stats.IntegralVariance
{

	@Override
	public void compute2(RectangleNeighborhood<Composite<I>> input1, Interval input2,
		DoubleType output)
	{
		// computation according to	https://en.wikipedia.org/wiki/Summed_area_table
		final IntegralCursor<Composite<I>> cursorS1 = new IntegralCursor<>(input1);
		int dimensions = input1.numDimensions();
		
		// Compute \sum (-1)^{dim - ||cornerVector||_{1}} * I(x^{cornerVector})
		final DoubleType sum1 = new DoubleType();
		sum1.setZero();

		// Convert from input to return type
		final Converter<I, DoubleType> conv = new RealDoubleConverter<>();
		
		// Compute \sum (-1)^{dim - ||cornerVector||_{1}} * I(x^{cornerVector})
		final DoubleType sum2 = new DoubleType();
		sum2.setZero();
		
		while ( cursorS1.hasNext() )
		{
			final Composite<I> compositeValue = cursorS1.next();
			final I value1 = compositeValue.get(0).copy();
			final DoubleType value1AsDoubleType = new DoubleType();
			conv.convert(value1, value1AsDoubleType);
			
			// Obtain the cursor position encoded as corner vector
			int cornerInteger1 = cursorS1.getCornerRepresentation();
			
			// Determine if the value has to be added (factor==1) or subtracted (factor==-1)
			DoubleType factor = new DoubleType(Math.pow(-1.0d, dimensions - norm(cornerInteger1)));
			value1AsDoubleType.mul(factor);
			
			sum1.add(value1AsDoubleType);
			
			final I value2 = compositeValue.get(1).copy();
			final DoubleType value2AsDoubleType = new DoubleType();
			conv.convert(value2, value2AsDoubleType);

			// Determine if the value has to be added (factor==1) or subtracted
			// (factor==-1)
			value2AsDoubleType.mul(factor);

			sum2.add(value2AsDoubleType);
		}

		// Compute overlap
		int area = overlap(correctNeighborhoodInterval(input1), input2);

		sum1.mul(sum1);
		sum1.div(new DoubleType(area));
		
		sum2.sub(sum1);
		sum2.div(new DoubleType(area));

		output.set(sum2);
	}

	/**
	 * TODO Documentation
	 * FIXME Move to central place?
	 * 
	 * @param interval1
	 * @param interval2
	 * @return area/volume/?
	 */
	private int overlap(Interval interval1, Interval interval2)
	{
		assert(interval1.numDimensions() == interval2.numDimensions());		
		int area = 1;
		
		for (int d = 0; d < interval1.numDimensions(); d++)
		{
			long upperLimit = Math.min(interval1.max(d), interval2.max(d));
			long lowerLimit = Math.max(interval1.min(d), interval2.min(d));
			
			area *= (upperLimit + 1) - lowerLimit;
		}
		
		return area;
	}

	/**
	 * TODO Documentation
	 * 
	 * @param neighborhood
	 * @return Blubb
	 */
	private Interval correctNeighborhoodInterval(Interval neighborhood) {	
		final long[] neighborhoodMinimum = new long[neighborhood.numDimensions()];
		neighborhood.min(neighborhoodMinimum);
		final long[] neighborhoodMaximum = new long[neighborhood.numDimensions()];
		neighborhood.max(neighborhoodMaximum);
		
		for (int d = 0; d < neighborhood.numDimensions(); d++)
		{
			neighborhoodMinimum[d] = neighborhoodMinimum[d] + 1;		
			neighborhoodMaximum[d] = neighborhoodMaximum[d] - 1;
		}
		
		return new FinalInterval(neighborhoodMinimum, neighborhoodMaximum);
	}
	
	/**
	 * Computes L1 norm of the position of an {@code IntegralCursor}. Computation
	 * is based on determining the number of 1 bits in the position.
	 * 
	 * @param cornerPosition position vector of an {@code IntegralCursor} encoded
	 *          as integer
	 * @return L1 norm of the position
	 */
	private int norm(int cornerPosition) {
		return Integer.bitCount(cornerPosition);
	}
	
}
