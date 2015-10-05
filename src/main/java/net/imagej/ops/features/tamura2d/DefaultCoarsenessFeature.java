/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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
package net.imagej.ops.features.tamura2d;

import java.util.ArrayList;
import java.util.HashMap;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Tamura;
import net.imagej.ops.Ops.Tamura.Coarseness;
import net.imagej.ops.filter.mean.MeanFilterOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import com.sun.jdi.ByteType;

/**
 * 
 * Implementation of Tamura's Coarseness feature
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = Tamura.Coarseness.class, label = "Tamura 2D: Coarseness", name = Tamura.Coarseness.NAME)
public class DefaultCoarsenessFeature<I extends RealType<I>, O extends RealType<O>>
		extends AbstractTamuraFeature<I, O> implements Coarseness {

	@Parameter
	protected OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public void compute(final RandomAccessibleInterval<I> input, final O output) {
		HashMap<Integer, Img<I>> meanImages = new HashMap<Integer, Img<I>>();

		// get mean images
		for (int i = 1; i <= 5; i++) {
			meanImages.put(i, mean(input, i));
		}

		ArrayList<Double> maxDifferences = sizedLeadDiffValues(input,
				meanImages);

		double out = 0.0;
		for (Double i : maxDifferences) {
			out += i;
		}

		out /= maxDifferences.size();

		output.set((O) new DoubleType(out));
	}

	/**
	 * 
	 * For every point calculate differences between the not overlapping
	 * neighborhoods on opposite sides of the point in horizontal and vertical
	 * direction. At each point take the highest difference value when
	 * considering all directions together.
	 * 
	 * @param input
	 *            Input image
	 * @param meanImages
	 *            Mean images
	 * @return Array containing all leadding difference values
	 */
	private ArrayList<Double> sizedLeadDiffValues(
			final RandomAccessibleInterval<I> input,
			final HashMap<Integer, Img<I>> meanImages) {

		long[] pos = new long[input.numDimensions()];
		long[] dim = new long[input.numDimensions()];
		input.dimensions(dim);

		ArrayList<Double> maxDifferences = new ArrayList<Double>();
		Cursor<I> cursor = meanImages.get(1).cursor();

		while (cursor.hasNext()) {

			cursor.next();

			double max = Double.MIN_VALUE;

			for (int i = 1; i <= 5; i++) {

				RandomAccess<I> ra1 = meanImages.get(i).randomAccess();
				RandomAccess<I> ra2 = meanImages.get(i).randomAccess();

				for (int d = 0; d < input.numDimensions(); d++) {

					cursor.localize(pos);

					if ((pos[d] + 2 * i + 1) < dim[d]) {

						ra1.setPosition(pos);
						double val1 = ra1.get().getRealDouble();

						pos[d] += 2 * i + 1;
						ra2.setPosition(pos);
						double val2 = ra2.get().getRealDouble();

						double diff = Math.abs(val2 - val1);
						max = diff >= max ? diff : max;
					}
				}
			}

			maxDifferences.add(max);
		}
		return maxDifferences;
	}

	/**
	 * Apply mean filter with given size of reactangle shape
	 * 
	 * @param input
	 *            Input image
	 * @param i
	 *            Size of rectangle shape
	 * @return Filered mean image
	 */
	@SuppressWarnings("unchecked")
	private Img<I> mean(final RandomAccessibleInterval<I> input, final int i) {

		long[] dims = new long[input.numDimensions()];
		input.dimensions(dims);

		final byte[] array = new byte[(int) Intervals
				.numElements(new FinalInterval(dims))];
		Img<I> meanImg = (Img<I>) ArrayImgs.unsignedBytes(array, dims);

		OutOfBoundsMirrorFactory<ByteType, Img<ByteType>> oobFactory = new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(
				Boundary.SINGLE);

		ops.run(MeanFilterOp.class, meanImg, input,
				new RectangleShape(i, true), oobFactory);

		return meanImg;
	}

}
