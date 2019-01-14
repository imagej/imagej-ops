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

package net.imagej.ops.filter.sharpen;

import net.imagej.Extents;
import net.imagej.Position;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhoodFactory;
import net.imglib2.algorithm.neighborhood.RectangleShape.NeighborhoodsAccessible;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * Op rendition of the SharpenDataValues plugin written by Barry Dezonia
 *
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.Sharpen.class)
public class DefaultSharpen<T extends RealType<T>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Filter.Sharpen
{

	final double[] kernel = { -1, -1, -1, -1, 12, -1, -1, -1, -1 };
	double scale;

	@Override
	public RandomAccessibleInterval<T> calculate(
		final RandomAccessibleInterval<T> input)
	{
		final RandomAccessibleInterval<T> output = ops().copy().rai(input);

		if(input.numDimensions() > 2) {
			throw new IllegalArgumentException("Input has too few dimensions! Only 2+ dimensional images allowed!");
		}

		final long[] planeDims = new long[input.numDimensions() - 2];
		for (int i = 0; i < planeDims.length; i++)
			planeDims[i] = input.dimension(i + 2);
		final Extents extents = new Extents(planeDims);
		final Position planePos = extents.createPosition();
		if (planeDims.length == 0) {
			computePlanar(planePos, input, output);
		}
		else {
			while (planePos.hasNext()) {
				planePos.fwd();
				computePlanar(planePos, input, output);
			}

		}
		return output;
	}

	private void computePlanar(final Position planePos,
		final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		// TODO can we just set scale to 4?
		scale = 0;
		for (final double d : kernel)
			scale += d;

		final T type = Util.getTypeFromInterval(input);

		final long[] imageDims = new long[input.numDimensions()];
		input.dimensions(imageDims);

		// create all objects needed for NeighborhoodsAccessible
		RandomAccessibleInterval<T> slicedInput = ops().copy().rai(input);
		for (int i = planePos.numDimensions() - 1; i >= 0; i--) {
			slicedInput = Views.hyperSlice(slicedInput, input.numDimensions() - 1 - i,
				planePos.getLongPosition(i));
		}

		final RandomAccessible<T> refactoredInput = Views.extendMirrorSingle(
			slicedInput);
		final RectangleNeighborhoodFactory<T> factory = RectangleNeighborhood
			.factory();
		final FinalInterval neighborhoodSpan = new FinalInterval(new long[] { -1,
			-1 }, new long[] { 1, 1 });

		final NeighborhoodsAccessible<T> neighborhoods =
			new NeighborhoodsAccessible<>(refactoredInput, neighborhoodSpan, factory);

		// create cursors and random accesses for loop.
		final Cursor<T> cursor = Views.iterable(input).localizingCursor();
		final RandomAccess<T> outputRA = output.randomAccess();
		for (int i = 0; i < planePos.numDimensions(); i++) {
			outputRA.setPosition(planePos.getLongPosition(i), i + 2);
		}
		final RandomAccess<Neighborhood<T>> neighborhoodsRA = neighborhoods
			.randomAccess();

		int algorithmIndex = 0;
		double sum;
		final double[] n = new double[9];
		while (cursor.hasNext()) {
			cursor.fwd();
			if (cursor.getLongPosition(0) == 14 && cursor.getLongPosition(1) == 0)
				System.out.println("Hit 14");
			neighborhoodsRA.setPosition(cursor);
			final Neighborhood<T> current = neighborhoodsRA.get();
			final Cursor<T> neighborhoodCursor = current.cursor();

			algorithmIndex = 0;
			sum = 0;
			while (algorithmIndex < n.length) {
				neighborhoodCursor.fwd();
				n[algorithmIndex++] = neighborhoodCursor.get().getRealDouble();
			}

			for (int i = 0; i < kernel.length; i++) {
				sum += kernel[i] * n[i];
			}
			
			//find the value for the output
			double value;
			if(type instanceof IntegerType) {
				value = (sum + scale / 2) / scale;
			}
			else {
				value = sum / scale;
			}

			outputRA.setPosition(cursor.getLongPosition(0), 0);
			outputRA.setPosition(cursor.getLongPosition(1), 1);
			if (value > type.getMaxValue()) value = type.getMaxValue();
			if (value < type.getMinValue()) value = type.getMinValue();
			outputRA.get().setReal(value);
		}
	}
}
