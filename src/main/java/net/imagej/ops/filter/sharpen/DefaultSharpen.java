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
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;
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

	final double[][] kernel = { { -1, -1, -1 }, { -1, 12, -1 }, { -1, -1, -1 } };

	RandomAccessibleInterval<ByteType> kernelRAI;

	/**
	 * The sum of all of the kernel values
	 */
	double scale = 4;

	UnaryFunctionOp<double[][], RandomAccessibleInterval<T>> kernelCreator;
	UnaryComputerOp<RandomAccessible<T>, RandomAccessibleInterval<DoubleType>> convolveOp;
	UnaryInplaceOp<IterableInterval<DoubleType>, IterableInterval<DoubleType>> addConstOp;
	UnaryInplaceOp<IterableInterval<DoubleType>, IterableInterval<DoubleType>> divConstOp;
	UnaryComputerOp<DoubleType, T> clipTypesOp;
	UnaryComputerOp<IterableInterval<DoubleType>, IterableInterval<T>> convertOp;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		T inType = Util.getTypeFromInterval(in());

		// convolution kernel
		kernelRAI = ops().create().kernel(kernel, new ByteType());
		
		IterableInterval<DoubleType> dummyDoubleType = ops().create().img(in(), new DoubleType());
		
		convolveOp = (UnaryComputerOp) Computers.unary(ops(),
			Ops.Filter.Convolve.class, RandomAccessibleInterval.class,
			RandomAccessible.class, kernelRAI);
		addConstOp = (UnaryInplaceOp) Inplaces.unary(ops(), Ops.Math.Add.class,
			dummyDoubleType, new DoubleType(scale / 2));
		divConstOp = (UnaryInplaceOp) Inplaces.unary(ops(), Ops.Math.Divide.class,
			dummyDoubleType, new DoubleType(scale));
		clipTypesOp = (UnaryComputerOp) Computers.unary(ops(),
			Ops.Convert.Clip.class, new DoubleType(), inType);
		convertOp = Computers.unary(ops(),
			Ops.Convert.ImageType.class, Views.iterable(in()), dummyDoubleType, clipTypesOp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> calculate(
		final RandomAccessibleInterval<T> input)
	{
		// intermediate image to hold the convolution data. Depending on the input
		// type we have to be able create an intermediate of wide enough type.
		final RandomAccessibleInterval<DoubleType> intermediate = ops().create().img(in(), new DoubleType());
		// output image
		final RandomAccessibleInterval<T> output = (RandomAccessibleInterval<T>) ops().create().img(in());

		if(input.numDimensions() > 2) {
			throw new IllegalArgumentException("Input has too few dimensions! Only 2+ dimensional images allowed!");
		}

		// compute the sharpening on 2D slices of the image
		final long[] planeDims = new long[input.numDimensions() - 2];
		for (int i = 0; i < planeDims.length; i++)
			planeDims[i] = input.dimension(i + 2);
		final Extents extents = new Extents(planeDims);
		final Position planePos = extents.createPosition();
		if (planeDims.length == 0) {
			computePlanar(planePos, input, intermediate);
		}
		else {
			while (planePos.hasNext()) {
				planePos.fwd();
				computePlanar(planePos, input, intermediate);
			}

		}

		T inputType = Util.getTypeFromInterval(input);
		IterableInterval<DoubleType> iterableIntermediate = Views.iterable(
			intermediate);

		// divide by the scale, if integerType input also add scale / 2.
		if (inputType instanceof IntegerType) addConstOp.mutate(
			iterableIntermediate);
		divConstOp.mutate(iterableIntermediate);
		
		//convert the result back to the input type.
		convertOp.compute(iterableIntermediate, Views.iterable(output));

		return output;
	}

	private void computePlanar(final Position planePos,
		final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<DoubleType> intermediate)
	{
		// create 2D slice in the case of a (N>2)-dimensional image.
		RandomAccessible<T> slicedInput = Views.extendMirrorSingle(input);
		RandomAccessibleInterval<DoubleType> slicedIntermediate = intermediate;
		for (int i = planePos.numDimensions() - 1; i >= 0; i--) {
			slicedInput = Views.hyperSlice(slicedInput, input.numDimensions() - 1 - i,
				planePos.getLongPosition(i));
			slicedIntermediate = Views.hyperSlice(slicedIntermediate, intermediate
				.numDimensions() - 1 - i, planePos.getLongPosition(i));
		}

		convolveOp.compute(slicedInput, slicedIntermediate);
	}
}
