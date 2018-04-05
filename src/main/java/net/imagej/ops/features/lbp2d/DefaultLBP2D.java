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

package net.imagej.ops.features.lbp2d;

import java.util.ArrayList;
import java.util.Iterator;

import net.imagej.ops.Ops;
import net.imagej.ops.image.histogram.HistogramCreate;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of 2d local binary patterns
 * 
 * @author Andreas Graumann (University of Konstanz)
 * @param <I>
 */
@Plugin(type = Ops.LBP.LBP2D.class, label = "2d Local Binary Pattern")
public class DefaultLBP2D<I extends RealType<I>> extends AbstractLBP2DFeature<I>
{

	@Parameter(required = true)
	private int distance = 1;

	@Parameter(required = true)
	private int histogramSize = 256;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<ArrayList, Histogram1d> histOp;

	@Override
	public void initialize() {
		histOp = Functions.unary(ops(), HistogramCreate.class, Histogram1d.class,
			ArrayList.class, histogramSize);
	}

	@Override
	public ArrayList<LongType> createOutput(RandomAccessibleInterval<I> input) {
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(RandomAccessibleInterval<I> input,
		ArrayList<LongType> output)
	{
		ArrayList<LongType> numberList = new ArrayList<>();
		RandomAccess<I> raInput = Views.extendZero(input).randomAccess();
		final Cursor<I> cInput = Views.flatIterable(input).cursor();
		final ClockwiseDistanceNeighborhoodIterator<I> cNeigh =
			new ClockwiseDistanceNeighborhoodIterator<>(raInput, distance);

		while (cInput.hasNext()) {
			cInput.next();
			double centerValue = cInput.get().getRealDouble();

			int resultBinaryValue = 0;

			cNeigh.reset();
			while (cNeigh.hasNext()) {
				double nValue = cNeigh.next().getRealDouble();
				int pos = cNeigh.getIndex();
				if (nValue >= centerValue) {
					resultBinaryValue |= (1 << pos);
				}
			}
			numberList.add(new LongType(resultBinaryValue));
		}

		Histogram1d<Integer> hist = histOp.calculate(numberList);
		Iterator<LongType> c = hist.iterator();
		while (c.hasNext()) {
			output.add(new LongType(c.next().get()));
		}

	}

}
