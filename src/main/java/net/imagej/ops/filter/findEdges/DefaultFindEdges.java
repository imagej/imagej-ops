/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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

package net.imagej.ops.filter.findEdges;

import net.imagej.Extents;
import net.imagej.Position;
import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
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
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op rendition of the Find Edges core plugin.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Filter.FindEdges.class)
public class DefaultFindEdges<T extends RealType<T>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Filter.FindEdges, Contingent
{

	@Parameter
	private OpService ops;

	@Override
	public RandomAccessibleInterval<T> calculate(
		RandomAccessibleInterval<T> input)
	{
		RandomAccessibleInterval<T> output = ops().copy().rai(input);

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
		RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output)
	{

		final T type = Util.getTypeFromInterval(input);

		final long[] imageDims = new long[input.numDimensions()];
		input.dimensions(imageDims);

		// create all objects needed for NeighborhoodsAccessible
		RandomAccessibleInterval<T> slicedInput = ops.copy().rai(input);
		for (int i = planePos.numDimensions() - 1; i >= 0; i--) {
			slicedInput = Views.hyperSlice(slicedInput, input.numDimensions() - 1 - i,
				planePos.getLongPosition(i));
		}

		final RandomAccessible<T> refactoredInput = Views.extendMirrorSingle(
			slicedInput);
		RectangleNeighborhoodFactory<T> factory = RectangleNeighborhood.factory();
		FinalInterval neighborhoodSpan = new FinalInterval(new long[] { -1, -1 },
			new long[] { 1, 1 });

		NeighborhoodsAccessible<T> neighborhoods = new NeighborhoodsAccessible<>(
			refactoredInput, neighborhoodSpan, factory);

		// create cursors and random accesses for loop.
		Cursor<T> cursor = Views.iterable(input).localizingCursor();
		RandomAccess<T> outputRA = output.randomAccess();
		for (int i = 0; i < planePos.numDimensions(); i++) {
			outputRA.setPosition(planePos.getLongPosition(i), i + 2);
		}
		RandomAccess<Neighborhood<T>> neighborhoodsRA = neighborhoods
			.randomAccess();

		int algorithmIndex = 0;
		double[] n = new double[9];
		while (cursor.hasNext()) {
			cursor.fwd();
			neighborhoodsRA.setPosition(cursor);
			Neighborhood<T> current = neighborhoodsRA.get();
			Cursor<T> neighborhoodCursor = current.cursor();

			// algorithm taken from the imglib2-algorithm FindEdges plugin
			algorithmIndex = 0;
			while (algorithmIndex < n.length) {
				neighborhoodCursor.fwd();
				n[algorithmIndex++] = neighborhoodCursor.get().getRealDouble();
			}

			// calculate sums
			double sum1 = n[0] + 2 * n[1] + n[2] - n[6] - 2 * n[7] - n[8];

			double sum2 = n[0] + 2 * n[3] + n[6] - n[2] - 2 * n[5] - n[8];

			double value = Math.sqrt(sum1 * sum1 + sum2 * sum2);

			outputRA.setPosition(cursor.getLongPosition(0), 0);
			outputRA.setPosition(cursor.getLongPosition(1), 1);
			if (value > type.getMaxValue()) value = type.getMaxValue();
			if (value < type.getMinValue()) value = type.getMinValue();
			outputRA.get().setReal(value);
		}

	}

	@Override
	public boolean conforms() {
		return in().numDimensions() > 1;

	}

}
