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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
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
 * <p>
 * Lee Vincent, Pierre Soille, Watersheds in digital spaces: An efficient
 * algorithm based on immersion simulations, IEEE Trans. Pattern Anal. Machine
 * Intell., 13(6) 583-598 (1991)
 *</p>
 * <p>
 * Input is a grayscale image with arbitrary number of dimensions, defining the
 * heightmap, and labeling image defining where the seeds, i.e. the minima are.
 * It needs to be defined whether a neighborhood with eight- or
 * four-connectivity (respective to 2D) is used. A binary image can be set as
 * mask which defines the area where computation shall be done.
 * </p>
 * <p>
 * Output is a labeling of the different catchment basins.
 * </p>
 * @author Simon Schmid (University of Konstanz)
 * 
 * TODO Javadoc for types
 */
@Plugin(type = Ops.Image.Watershed.class)
public class WatershedSeeded<B extends BooleanType<B>, T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, ImgLabeling<Integer, IntType>>
		implements Ops.Image.Watershed, Contingent {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<FinalInterval, ImgLabeling> createOp;

	@Parameter(required = true)
	private ImgLabeling<Integer, IntType> seeds;

	@Parameter(required = true)
	private boolean eightConnectivity;

	@Parameter(required = true)
	private boolean withWatersheds;

	@Parameter(required = false)
	private RandomAccessibleInterval<B> mask;

	/** Default label for watersheds */
	private final static int WSHED = -1;

	/** Default dummy label */
	private final static int DUMMY = -2;

	@Override
	public void compute(final RandomAccessibleInterval<T> in, final ImgLabeling<Integer, IntType> out) {
		RandomAccess<B> raMask = null;
		if (mask != null) {
			raMask = mask.randomAccess();
		}
		// RandomAccess for Neighborhoods
		final Shape shape;
		if (eightConnectivity) {
			shape = new RectangleShape(1, true);
		} else {
			shape = new DiamondShape(1);
		}
		final RandomAccessible<Neighborhood<T>> neighborhoods = shape.neighborhoodsRandomAccessible(in);
		final RandomAccess<Neighborhood<T>> raNeighborhoods = neighborhoods.randomAccess();

		// TODO What is the reasoning behind the currentLabeling?
		final ImgLabeling<Integer, IntType> currentLabeling = ops().create().imgLabeling(in);
		final RandomAccess<LabelingType<Integer>> raCurrentLabeling = currentLabeling.randomAccess();

		final RandomAccess<LabelingType<Integer>> raOut = out.randomAccess();
		final RandomAccess<T> raIn = in.randomAccess();

		/*
		 * start by loading up a list with the seeded pixels
		 */
		// TODO Why was the PriorityQueue used in the MorphoLibJ implementation?
		final List<Long> pq = new ArrayList<>();

		// Only iterate seeds that are not excluded by the mask
		IterableRegion<B> maskRegions = Regions.iterable(mask);
		IterableInterval<LabelingType<Integer>> seedsMasked = Regions.sample(maskRegions, seeds);
		final Cursor<LabelingType<Integer>> cursorSeeds = seedsMasked.localizingCursor();

		/*
		 * carries over the seeding points to the new label and adds them to the
		 * pixel priority queue
		 */
		while (cursorSeeds.hasNext()) {
			final Set<Integer> l = cursorSeeds.next();
			if (l.isEmpty()) {
				continue;
			}

			// Overwrite label in output with the seed label
			raOut.setPosition(cursorSeeds);
			final LabelingType<Integer> tDest = raOut.get();
			tDest.clear();
			tDest.add(l.iterator().next()); // FIXME I guess having having seed pixels/voxels with multiple labels should end the computation?

			// Add to queue
			pq.add(IntervalIndexer.positionToIndex(cursorSeeds, out));

			// Write seed label into raCurrentLabeling
			raCurrentLabeling.setPosition(cursorSeeds);
			raCurrentLabeling.get().addAll(tDest);
		}

		/*
		 * pop the head of the priority queue, label and push all unlabeled
		 * connected pixels.
		 */
		// label to mark the watersheds
		final LabelingType<Integer> watersheds = out.firstElement().createVariable();
		watersheds.add(WSHED);

		// dummy to mark nodes as visited
		final LabelingType<Integer> dummy = out.firstElement().createVariable();
		dummy.add(DUMMY);

		// iterate the queue
		while (!pq.isEmpty()) {
			IntervalIndexer.indexToPosition(pq.remove(0), currentLabeling, raCurrentLabeling);
			raIn.setPosition(raCurrentLabeling); // use raIn to remember position

			Set<Integer> l = new HashSet<>();
			l.addAll(raCurrentLabeling.get());

			// iterate the neighborhood of the pixel
			raNeighborhoods.setPosition(raCurrentLabeling);
			final Cursor<T> neighborhood = raNeighborhoods.get().cursor();
			// FIXME Maybe this could be replaced with a dedicated BOUNDARY label with which we extend the image
			boolean isBoundaries = true;
			while (neighborhood.hasNext()) {
				neighborhood.fwd();
				raOut.setPosition(neighborhood);
				final LabelingType<Integer> outputLabelingType = raOut.get();
				if (!Intervals.contains(out, raOut)) {
					continue;
				}

				if (outputLabelingType.isEmpty()) {
					// We can't get rid of this check I guess because Neighborhood is not a RandomAccessible and can't be sampled
					if (mask != null) {
						raMask.setPosition(raOut);
						if (raMask.get().get()) {
							pq.add(IntervalIndexer.positionToIndex(raOut, out));
							raCurrentLabeling.setPosition(raOut);
							raCurrentLabeling.get().addAll(l);
						}
					} else {
						pq.add(IntervalIndexer.positionToIndex(raOut, out));
						raCurrentLabeling.setPosition(raOut);
						raCurrentLabeling.get().addAll(l);

					}
					// dummy to mark positions as visited
					outputLabelingType.clear();
					outputLabelingType.addAll(dummy);

				} else if (!outputLabelingType.equals(l) && !outputLabelingType.equals(dummy)
						&& !outputLabelingType.equals(watersheds)) {
					l = watersheds;
				}
				isBoundaries = false;

			}
			if (isBoundaries) {
				l = watersheds;
			}
			raOut.setPosition(raIn);
			final LabelingType<Integer> outputLabelingType = raOut.get();
			outputLabelingType.clear();
			outputLabelingType.addAll(l);
		}

		/*
		 * Set Output
		 */
		IterableInterval<LabelingType<Integer>> sampledOut = Regions.sample(maskRegions, out);
		final Cursor<LabelingType<Integer>> cursorOut = sampledOut.cursor();
		while (cursorOut.hasNext()) {
			cursorOut.fwd();
			raOut.setPosition(cursorOut);
				if (raOut.get().contains(WSHED)) {
					if (withWatersheds) {
						cursorOut.get().clear();
					} else {
						raNeighborhoods.setPosition(cursorOut);
						final Cursor<T> neighborhood = raNeighborhoods.get().cursor();
						boolean newLab = false;
						final List<Integer> allLabels = new ArrayList<>();
						while (neighborhood.hasNext()) {
							neighborhood.fwd();
							raOut.setPosition(neighborhood);
							// Why do we need that check?
							// ---------------- TODO
							LabelingType<Integer> labelingType = cursorOut.get().createVariable();
							labelingType.add(DUMMY);
							Views.extendValue(out, labelingType);
							// ---------------- TODO
							if (Intervals.contains(in, neighborhood)) {
								if ((!raOut.get().contains(WSHED)) && (!raOut.get().contains(DUMMY))) {
									cursorOut.get().clear();
									cursorOut.get().addAll(raOut.get());
									allLabels.addAll(raOut.get());
									newLab = true;
								}
							}
						}
						if (!newLab) {
							cursorOut.get().clear();
						}
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
		if (seeds.numDimensions() != in().numDimensions())
			conformed = false;
		else {
			for (int i = 0; i < seeds.numDimensions(); i++) {
				if (seeds.dimension(i) != in().dimension(i))
					conformed = false;
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
