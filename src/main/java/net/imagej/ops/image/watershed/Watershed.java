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
package net.imagej.ops.image.watershed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.IntervalIndexer;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * The Watershed algorithm segments and labels a grayscale image analogous to a
 * heightmap. In short, a drop of water following the gradient of an image flows
 * along a path to finally reach a local minimum.
 *
 * Lee Vincent, Pierre Soille, Watersheds in digital spaces: An efficient
 * algorithm based on immersion simulations, IEEE Trans. Pattern Anal. Machine
 * Intell., 13(6) 583-598 (1991)
 *
 * Input is a grayscale image with arbitrary number of dimensions, defining the
 * heightmap. It needs to be defined whether a neighborhood with eight- or
 * four-connectivity (respective to 2D) is used. A binary image can be set as
 * mask which defines the area where computation shall be done.
 *
 * Output is a labeling of the different catchment basins.
 *
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.Watershed.class)
public class Watershed<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, ImgLabeling<Integer, IntType>>
		implements Ops.Image.Watershed, Contingent {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, ImgLabeling> createOp;

	@Parameter(required = true)
	private boolean eightConnectivity;

	@Parameter(required = false)
	private RandomAccessibleInterval<B> mask;

	@Override
	public void compute(final RandomAccessibleInterval<T> in, final ImgLabeling<Integer, IntType> out) {
		final RandomAccess<T> raIn = in.randomAccess();
		RandomAccess<B> raMask = null;
		if (mask != null) {
			raMask = mask.randomAccess();
		}
		final int[] pos = new int[in.numDimensions()];
		// stores the size of each dimension
		final int[] dimensSizes = new int[in.numDimensions()];

		// calculates the number of points in the n-d space
		int numPixels = 1;
		for (int i = 0; i < in.numDimensions(); i++) {
			dimensSizes[i] = (int) in.dimension(i);
			numPixels *= dimensSizes[i];
		}

		// the pixels indices are stored in an array, which is sorted depending
		// on the pixel values
		final List<Integer> imiList = new ArrayList<>();

		if (mask != null) {
			final Cursor<B> cursorMask = Views.flatIterable(mask).cursor();
			while (cursorMask.hasNext()) {
				cursorMask.fwd();
				if (cursorMask.get().get()) {
					cursorMask.localize(pos);
					imiList.add(IntervalIndexer.positionToIndex(pos, dimensSizes));
				}
			}
		} else {
			for (int i = 0; i < numPixels; i++) {
				imiList.add(i);
			}
		}
		final Integer[] imi = imiList.toArray(new Integer[imiList.size()]);

		// lab and dist store the values calculated after each phase
		final RandomAccessibleInterval<IntType> lab = ops().create().img(in, new IntType());
		final RandomAccess<IntType> raLab = lab.randomAccess();
		final RandomAccessibleInterval<IntType> dist = ops().create().img(in, new IntType());
		final RandomAccess<IntType> raDist = dist.randomAccess();

		// initial values
		final int MASK = -2;
		final int WSHED = 0;
		final int INIT = -1;
		for (final IntType pixel : Views.flatIterable(lab)) {
			pixel.set(INIT);
		}
		int current_label = 0;
		int current_dist;
		final ArrayList<Integer> fifo = new ArrayList<>();

		// RandomAccess for Neighborhoods
		final Shape shape;
		if (eightConnectivity) {
			shape = new RectangleShape(1, true);
		} else {
			shape = new DiamondShape(1);
		}
		final RandomAccessible<Neighborhood<T>> neighborhoods = shape.neighborhoodsRandomAccessible(in);
		final RandomAccess<Neighborhood<T>> raNeighbor = neighborhoods.randomAccess();

		/*
		 * Sort the pixels of imi in the increasing order of their grey value
		 * (only the pixel indices are stored)
		 */
		Arrays.sort(imi, new Comparator<Integer>() {
			@Override
			public int compare(final Integer o1, final Integer o2) {
				IntervalIndexer.indexToPosition(o1, dimensSizes, pos);
				raIn.setPosition(pos);
				final float value = raIn.get().getRealFloat();
				IntervalIndexer.indexToPosition(o2, dimensSizes, pos);
				raIn.setPosition(pos);
				return Float.compare(value, raIn.get().getRealFloat());
			}
		});

		/*
		 * Start flooding
		 */
		for (int j = 0; j < imi.length; j++) {
			IntervalIndexer.indexToPosition(imi[j], dimensSizes, pos);
			raIn.setPosition(pos);
			final float actualH = raIn.get().getRealFloat();
			int i = j;
			while (Float.compare(actualH, raIn.get().getRealFloat()) == 0) {
				final int p = imi[i];
				IntervalIndexer.indexToPosition(p, dimensSizes, pos);
				raIn.setPosition(pos);
				raLab.setPosition(pos);
				raLab.get().set(MASK);
				raNeighbor.setPosition(pos);
				final Cursor<T> neighborHood = raNeighbor.get().cursor();

				while (neighborHood.hasNext()) {
					neighborHood.fwd();
					if (Intervals.contains(in, neighborHood)) {
						raLab.setPosition(neighborHood);
						final int f = raLab.get().get();
						if ((f > 0) || (f == WSHED)) {
							raDist.setPosition(pos);
							raDist.get().set(1);
							fifo.add(p);
							break;
						}
					}
				}
				i++;
				if (i == imi.length) {
					break;
				}
				IntervalIndexer.indexToPosition(imi[i], dimensSizes, pos);
				raIn.setPosition(pos);
			}

			current_dist = 1;
			fifo.add(-1); // add fictitious pixel
			while (true) {
				int p = fifo.remove(0);
				if (p == -1) {
					if (fifo.isEmpty()) {
						break;
					}
					fifo.add(-1);
					current_dist++;
					p = fifo.remove(0);
				}

				IntervalIndexer.indexToPosition(p, dimensSizes, pos);

				raNeighbor.setPosition(pos);
				final Cursor<T> neighborHood = raNeighbor.get().cursor();

				raLab.setPosition(pos);
				int labp = raLab.get().get();

				final int[] posNeighbor = new int[neighborHood.numDimensions()];
				while (neighborHood.hasNext()) {
					neighborHood.fwd();
					neighborHood.localize(posNeighbor);
					if (Intervals.contains(in, neighborHood)) {
						raLab.setPosition(posNeighbor);
						raDist.setPosition(posNeighbor);
						final int labq = raLab.get().get();
						final int distq = raDist.get().get();
						if ((distq < current_dist) && ((labq > 0) || (labq == WSHED))) {
							// i.e. q belongs to an already labeled basin or to
							// the watersheds
							if (labq > 0) {
								if ((labp == MASK) || (labp == WSHED)) {
									labp = labq;
								} else {
									if (labp != labq) {
										labp = WSHED;
									}
								}
							} else {
								if (labp == MASK) {
									labp = WSHED;
								}
							}
							raLab.setPosition(pos);
							raLab.get().set(labp);
						} else {
							if ((labq == MASK) && (distq == 0)) {
								raDist.setPosition(posNeighbor);
								raDist.get().set(current_dist + 1);
								fifo.add(IntervalIndexer.positionToIndex(posNeighbor, dimensSizes));
							}
						}
					}
				}
			}

			// checks if new minima have been discovered
			IntervalIndexer.indexToPosition(imi[j], dimensSizes, pos);
			raIn.setPosition(pos);
			i = j;
			while (Float.compare(actualH, raIn.get().getRealFloat()) == 0) {
				final int p = imi[i];
				IntervalIndexer.indexToPosition(p, dimensSizes, pos);
				// the distance associated with p is reseted to 0
				raDist.setPosition(pos);
				raDist.get().set(0);
				raLab.setPosition(pos);

				if (raLab.get().get() == MASK) {
					current_label++;
					fifo.add(p);
					raLab.get().set(current_label);
					while (!fifo.isEmpty()) {
						final int q = fifo.remove(0);
						IntervalIndexer.indexToPosition(q, dimensSizes, pos);
						raNeighbor.setPosition(pos);
						final Cursor<T> neighborHood = raNeighbor.get().cursor();

						final int[] posNeighbor = new int[neighborHood.numDimensions()];
						while (neighborHood.hasNext()) {
							neighborHood.fwd();
							neighborHood.localize(posNeighbor);
							if (Intervals.contains(in, neighborHood)) {
								final int r = IntervalIndexer.positionToIndex(posNeighbor, dimensSizes);
								raLab.setPosition(posNeighbor);
								if (raLab.get().get() == MASK) {
									fifo.add(r);
									raLab.get().set(current_label);
								}
							}
						}
					}
				}
				i++;
				if (i == imi.length) {
					break;
				}
				IntervalIndexer.indexToPosition(imi[i], dimensSizes, pos);
				raIn.setPosition(pos);
			}
			j = i - 1;
		}

		/*
		 * Set Output
		 */
		final Cursor<LabelingType<Integer>> cursorOut = out.cursor();
		while (cursorOut.hasNext()) {
			cursorOut.fwd();
			boolean maskValue = true;
			if (mask != null) {
				raMask.setPosition(cursorOut);
				if (!raMask.get().get()) {
					maskValue = false;
				}
			}
			raLab.setPosition(cursorOut);
			if (!maskValue) {
				cursorOut.get().clear();
			} else {
				if (raLab.get().get() == WSHED) {
					raNeighbor.setPosition(cursorOut);
					final Cursor<T> neighborHood = raNeighbor.get().cursor();
					int newLab = 0;
					while (neighborHood.hasNext()) {
						neighborHood.fwd();
						raLab.setPosition(neighborHood);
						if (Intervals.contains(in, neighborHood)) {
							newLab = raLab.get().get();
							if (newLab != 0) {
								break;
							}
						}

					}
					if (newLab <= 0) {
						cursorOut.get().clear();
					} else {
						cursorOut.get().add(newLab);
					}
				} else {
					cursorOut.get().add(raLab.get().get());
				}
			}
		}
	}

	@Override
	public boolean conforms() {
		boolean conformed = true;
		if (mask != null) {
			if (mask.numDimensions() != in().numDimensions())
				conformed = false;
			else {
				for (int i = 0; i < mask.numDimensions(); i++) {
					if (mask.dimension(i) != in().dimension(i))
						conformed = false;
				}
			}
		}
		return conformed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImgLabeling<Integer, IntType> createOutput(final RandomAccessibleInterval<T> in) {
		return createOp.calculate(new FinalInterval(in));
	}

	@Override
	public void initialize() {
		createOp = Functions.unary(ops(), CreateImgLabelingFromInterval.class, ImgLabeling.class,
				new FinalInterval(in()));
	}
}
