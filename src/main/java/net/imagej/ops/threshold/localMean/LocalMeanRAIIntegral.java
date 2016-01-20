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

package net.imagej.ops.threshold.localMean;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.integral.IntegralImg;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.converter.Converter;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * LocalThresholdMethod using mean.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.LocalMean.class, priority = Priority.HIGH_PRIORITY)
public class LocalMeanRAIIntegral<T extends RealType<T> & NativeType<T>>
	extends AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<BitType>>
	implements Ops.Threshold.LocalMean
{
	
	@Parameter
	private RectangleShape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds;
	
	@Parameter(required = false)
	private IntegralImg<T, DoubleType> integralImg;
	
	@Parameter
	private double c;

	@Override
	public void initialize() {	}

	@Override
	public void compute1(final RandomAccessibleInterval<T> input, final RandomAccessibleInterval<BitType> output)
	{
		// Create IntegralImg from input
		integralImg = new IntegralImg<T, DoubleType>(input, new DoubleType(), new RealDoubleConverter<T>());    
		
		// integralImg will be larger by one pixel in each dimension than input due to the computation of the integral image
		RandomAccessibleInterval<DoubleType> img = null;
		if ( integralImg.process() )
		{
			img = integralImg.getResult();
		}
		
		// Remove 0s from integralImg by shifting its interval by +1
		long[] min = new long[ input.numDimensions() ];
        long[] max = new long[ input.numDimensions() ];
		
        for ( int d = 0; d < input.numDimensions(); ++d )
        {
            min[ d ] = input.min( d ) + 1;
            max[ d ] = input.max( d ) + 1;
        }
 
        // Define the Interval on the infinite random accessibles
        FinalInterval interval = new FinalInterval( min, max );
       
        RandomAccessibleInterval< DoubleType > extendedImg = Views.offsetInterval( Views.extendBorder( img ), interval );
        
     	// Random access for input and output
     	RandomAccess<BitType> outputRandomAccess = output.randomAccess();
     	RandomAccess<T> inputRandomAccess = input.randomAccess();
     	
		Cursor<Neighborhood<DoubleType>> cursor = shape.neighborhoodsSafe(extendedImg).cursor();
		
		// Iterate neighborhoods
		while ( cursor.hasNext() )
		{
			// Based on the location compute the values A, B, C, and D
			// (computation according to https://en.wikipedia.org/wiki/Summed_area_table)
			Neighborhood<DoubleType> neighborhood = cursor.next();
			
			long[] neighborhoodPosition = new long[ neighborhood.numDimensions() ];
			neighborhood.localize(neighborhoodPosition);
			
			// Extend neighborhood's minimum (see comments on computation of A, B, and C)
			long[] neighborhoodMinimumDecreased = new long[ neighborhood.numDimensions() ];
			long[] neighborhoodMaximum = new long[ neighborhood.numDimensions() ];
			
	        for ( int d = 0; d < neighborhood.numDimensions(); ++d )
	        {
	        	neighborhoodMinimumDecreased[ d ] = neighborhood.min( d ) - 1;
	        	neighborhoodMaximum[ d ] = neighborhood.max( d );
	        }

	        FinalInterval neighborhoodInterval = new FinalInterval( neighborhoodMinimumDecreased, neighborhoodMaximum );
			
			// Define RAI on neighborhood's interval
			RandomAccess<DoubleType> extendedImgRA = Views.interval(extendedImg, neighborhoodInterval).randomAccess();
			
			// A
			// Shift minimum position by (-1, -1) to get the correct value for computations
			long[] valueAPosition = new long[]{ neighborhood.min(0) - 1, neighborhood.min(1) - 1 };
			extendedImgRA.setPosition(valueAPosition);
			DoubleType valueA = extendedImgRA.get().copy();
			
			// B
			// Shift minimum position by (0, -1) to get the correct value for computations
			long[] valueBPosition = new long[]{ neighborhood.max(0), neighborhood.min(1) - 1 };
			extendedImgRA.setPosition(valueBPosition);
			DoubleType valueB = extendedImgRA.get().copy();
			
			// C
			// Shift minimum position by (-1, 0) to get the correct value for computations
			long[] valueCPosition = new long[]{ neighborhood.min(0) - 1, neighborhood.max(1) };
			extendedImgRA.setPosition(valueCPosition);
			DoubleType valueC = extendedImgRA.get().copy();
			
			// D
			long[] valueDPosition = new long[]{ neighborhood.max(0), neighborhood.max(1) };
			extendedImgRA.setPosition(valueDPosition);
			DoubleType valueD = extendedImgRA.get().copy();
			
			// Compute A - B - C + D
			DoubleType sum = extendedImgRA.get().createVariable();
			sum.setZero();
			sum.add(valueA);
			sum.sub(valueB);
			sum.sub(valueC);
			sum.add(valueD);
			
			// Absolute difference between minima
			long[] neighborhoodMinimum = new long[ neighborhood.numDimensions() ];
			neighborhood.min( neighborhoodMinimum );
			
			// The neighborhoodMaximum is computed before
			
			long[] inputMinimum = new long[ extendedImg.numDimensions() ];
			extendedImg.min( inputMinimum );
			
			long[] inputMaximum = new long[ extendedImg.numDimensions() ];
			extendedImg.max( inputMaximum );
			
			int area = 1;
			for ( int d = 0; d < input.numDimensions(); d++ )
			{
				if (neighborhoodMinimum[ d ] <= inputMinimum[ d ] && neighborhoodMaximum[ d ] <= inputMaximum[ d ])
				{
					area *= Math.abs((neighborhoodMaximum[ d ] + 1) - inputMinimum[ d ]);
					continue;
				}
				
				if (neighborhoodMinimum[ d ] >= inputMinimum[ d ] && neighborhoodMaximum[ d ] <= inputMaximum[ d ])
				{
					area *= Math.abs((neighborhoodMaximum[ d ] + 1) - neighborhoodMinimum[ d ]);
					continue;
				}
				
				if (neighborhoodMinimum[ d ] >= inputMinimum[ d ] && neighborhoodMaximum[ d ] >= inputMaximum[ d ])
				{
					area *= Math.abs((inputMaximum[ d ] + 1) - neighborhoodMinimum[ d ]);
					continue;
				}
				
				// FIXME Missing case that implies that the RectangleShape is bigger than the image itself
			}
			
			// Compute mean by dividing the sum divided by the number of elements
			sum.div(new DoubleType(area));
			
			// Subtract the contrast
			sum.sub(new DoubleType(c));
			
			// Set value
			inputRandomAccess.setPosition(neighborhoodPosition);
			T inputPixel = inputRandomAccess.get();

			Converter<T, DoubleType> conv = new RealDoubleConverter<T>();
			DoubleType inputPixelAsDoubleType = new DoubleType();
			conv.convert(inputPixel, inputPixelAsDoubleType);
			
			outputRandomAccess.setPosition(neighborhood);
			outputRandomAccess.get().set(inputPixelAsDoubleType.compareTo(sum) > 0);
		}
	}

}
