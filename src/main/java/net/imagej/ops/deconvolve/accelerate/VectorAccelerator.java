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

package net.imagej.ops.deconvolve.accelerate;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Vector Accelerator implements acceleration scheme described in Acceleration
 * of iterative image restoration algorithms David S.C. Biggs and Mark Andrews
 * Applied Optics, Vol. 36, Issue 8, pp. 1766-1775 (1997)
 * 
 * @author bnorthan
 * @param <T>
 */
@Plugin(type = Ops.Deconvolve.Accelerate.class,
	priority = Priority.NORMAL)
public class VectorAccelerator<T extends RealType<T> & NativeType<T>> extends
	AbstractUnaryInplaceOp<RandomAccessibleInterval<T>> implements
	Ops.Deconvolve.Accelerate
{

	Img<T> xkm1_previous = null;
	Img<T> yk_prediction = null;
	Img<T> hk_vector = null;

	Img<T> gk;
	Img<T> gkm1;

	private UnaryFunctionOp<Dimensions, Img<T>> create;

	double accelerationFactor = 0.0f;

	ArrayImgFactory<T> factory;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		factory = new ArrayImgFactory<>();

		create = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, Dimensions.class, Util.getTypeFromInterval(out()), factory);
	}

	@Override
	public void mutate(RandomAccessibleInterval<T> yk_iterated) {
		accelerate(yk_iterated);
	}

	public void initialize(RandomAccessibleInterval<T> yk_iterated) {
		if (yk_prediction == null) {

			long[] temp = new long[yk_iterated.numDimensions()];
			yk_iterated.dimensions(temp);

			FinalDimensions dims = new FinalDimensions(temp);

			yk_prediction = create.calculate(dims);
			xkm1_previous = create.calculate(dims);
			yk_prediction = create.calculate(dims);
			gk = create.calculate(dims);
			hk_vector = create.calculate(dims);

		}

	}

	public void accelerate(RandomAccessibleInterval<T> yk_iterated) {

		// use the iterated prediction and the previous value of the prediction
		// to calculate the acceleration factor
		if (yk_prediction != null) {

			accelerationFactor = computeAccelerationFactor(yk_iterated);

			if ((accelerationFactor < 0)) {
				gkm1 = null;
				accelerationFactor = 0.0;
			}

			if ((accelerationFactor > 1.0f)) {
				accelerationFactor = 1.0f;
			}
		}

		// current estimate for x is yk_iterated
		RandomAccessibleInterval<T> xk_estimate = yk_iterated;

		// calculate the change vector between x and x previous
		if (accelerationFactor > 0) {
			Subtract(xk_estimate, xkm1_previous, hk_vector);

			// make the next prediction
			yk_prediction = AddAndScale(xk_estimate, hk_vector,
				(float) accelerationFactor);
		}
		else {

			// TODO: Revisit where initialization should be done
			initialize(yk_iterated);

			Copy(xk_estimate, yk_prediction);
		}

		// make a copy of the estimate to use as previous next time
		Copy(xk_estimate, xkm1_previous);

		// HACK: TODO: look over how to transfer the memory
		Copy(yk_prediction, yk_iterated);
	}

	double computeAccelerationFactor(RandomAccessibleInterval<T> yk_iterated) {
		// gk=StaticFunctions.Subtract(yk_iterated, yk_prediction);
		Subtract(yk_iterated, yk_prediction, gk);

		if (gkm1 != null) {
			double numerator = DotProduct(gk, gkm1);
			double denominator = DotProduct(gkm1, gkm1);

			gkm1 = gk.copy();
			return numerator / denominator;

		}

		gkm1 = gk.copy();

		return 0.0;

	}

	/*
	 * multiply inputOutput by input and place the result in input
	 */
	public double DotProduct(final Img<T> image1, final Img<T> image2) {
		final Cursor<T> cursorImage1 = image1.cursor();
		final Cursor<T> cursorImage2 = image2.cursor();

		double dotProduct = 0.0d;

		while (cursorImage1.hasNext()) {
			cursorImage1.fwd();
			cursorImage2.fwd();

			float val1 = cursorImage1.get().getRealFloat();
			float val2 = cursorImage2.get().getRealFloat();

			dotProduct += val1 * val2;
		}

		return dotProduct;
	}

	// TODO replace with Op
	// copy a into b
	protected void Copy(RandomAccessibleInterval<T> a,
		RandomAccessibleInterval<T> b)
	{

		final Cursor<T> cursorA = Views.iterable(a).cursor();
		final Cursor<T> cursorB = Views.iterable(b).cursor();

		while (cursorA.hasNext()) {
			cursorA.fwd();
			cursorB.fwd();

			cursorB.get().set(cursorA.get());
		}
	}

	// TODO replace with op.
	protected void Subtract(RandomAccessibleInterval<T> a,
		RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output)
	{

		final Cursor<T> cursorA = Views.iterable(a).cursor();
		final Cursor<T> cursorInput = Views.iterable(input).cursor();
		final Cursor<T> cursorOutput = Views.iterable(output).cursor();

		while (cursorA.hasNext()) {
			cursorA.fwd();
			cursorInput.fwd();
			cursorOutput.fwd();

			cursorOutput.get().set(cursorA.get());
			cursorOutput.get().sub(cursorInput.get());
		}
	}

	// TODO: replace with op
	public Img<T> AddAndScale(final RandomAccessibleInterval<T> img1,
		final Img<T> img2, final float a)
	{
		Img<T> out = create.calculate(img1);

		final Cursor<T> cursor1 = Views.iterable(img1).cursor();
		final Cursor<T> cursor2 = img2.cursor();
		final Cursor<T> cursorOut = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			cursorOut.fwd();

			float val1 = cursor1.get().getRealFloat();
			float val2 = cursor2.get().getRealFloat();

			float val3 = Math.max(val1 + a * val2, 0.0001f);

			cursorOut.get().setReal(val3);
		}

		return out;
	}
}
