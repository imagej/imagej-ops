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
package net.imagej.ops.image.watershed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.IntervalIndexer;
import net.imglib2.util.Intervals;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * The Watershed algorithm segments and labels a grayscale image analogous to a
 * heightmap. In short, a drop of water following the gradient of an image flows
 * along a path to finally reach a local minimum.
 * <p>
 * Beucher, Serge, and Fernand Meyer. "The morphological approach to
 * segmentation: the watershed transformation." Optical Engineering-New
 * York-Marcel Dekker Incorporated- 34 (1992): 433-433.
 * </p>
 * <p>
 * Input is a grayscale image with arbitrary number of dimensions, defining the
 * heightmap, and labeling image defining where the seeds, i.e. the minima are.
 * It needs to be defined whether a neighborhood with eight- or
 * four-connectivity (respective to 2D) is used. A binary image can be set as
 * mask which defines the area where computation shall be done. If desired, the
 * watersheds are drawn and labeled as 0. Otherwise the watersheds will be
 * labeled as one of their neighbors.
 * </p>
 * <p>
 * Output is a labeling of the different catchment basins.
 * </p>
 * 
 * @param <T> element type of input
 * @param <B> element type of mask
 * 
 * @author Simon Schmid (University of Konstanz)
 */
@Plugin(type = Ops.Image.Watershed.class)
public class WatershedSeeded<T extends RealType<T>, B extends BooleanType<B>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, ImgLabeling<Integer, IntType>>
		implements Ops.Image.Watershed, Contingent {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Interval, ImgLabeling> createOp;

	@Parameter(required = true)
	private ImgLabeling<Integer, IntType> seeds;

	@Parameter(required = true)
	private boolean useEightConnectivity;

	@Parameter(required = true)
	private boolean drawWatersheds;

	@Parameter(required = false)
	private RandomAccessibleInterval<B> mask;

	/** Default label for watershed, input seeds must have a greater label*/
	private static final int WSHED = -1;

	/** Default label for initialization, must be lower than WSHED */
	private static final int INIT = -2;

	/** Default label for in queue, must be lower than WSHED */
	private static final int INQUEUE = -3;

	/** Default label for in out of bounds, must be lower than WSHED */
	private static final int OUTSIDE = -4;

	/** Used by {@link WatershedSeeded.WatershedVoxel} */
	private static final AtomicLong seq = new AtomicLong();

	@SuppressWarnings("unchecked")
	@Override
	public void compute(final RandomAccessibleInterval<T> in, final ImgLabeling<Integer, IntType> out) {
		// extend border to be able to do a quick check, if a voxel is inside
		final LabelingType<Integer> oustide = out.firstElement().copy();
		oustide.clear();
		oustide.add(OUTSIDE);
		final ExtendedRandomAccessibleInterval<LabelingType<Integer>, ImgLabeling<Integer, IntType>> outExt = Views
				.extendValue(out, oustide);
		final OutOfBounds<LabelingType<Integer>> raOut = outExt.randomAccess();

		// if no mask provided, set the mask to the whole image
		if (mask == null) {
			mask = (RandomAccessibleInterval<B>) ops().create().img(in, new BitType());
			for (B b : Views.flatIterable(mask)) {
				b.set(true);
			}
		}

		// initialize output labels
		final Cursor<B> maskCursor = Views.flatIterable(mask).cursor();
		while (maskCursor.hasNext()) {
			maskCursor.fwd();
			if (maskCursor.get().get()) {
				raOut.setPosition(maskCursor);
				raOut.get().clear();
				raOut.get().add(INIT);
			}
		}

		// RandomAccess for Mask, Seeds and Neighborhoods
		final RandomAccess<B> raMask = mask.randomAccess();
		final RandomAccess<LabelingType<Integer>> raSeeds = seeds.randomAccess();
		final Shape shape;
		if (useEightConnectivity) {
			shape = new RectangleShape(1, true);
		} else {
			shape = new DiamondShape(1);
		}
		final RandomAccessible<Neighborhood<T>> neighborhoods = shape.neighborhoodsRandomAccessible(in);
		final RandomAccess<Neighborhood<T>> raNeigh = neighborhoods.randomAccess();

		/*
		 * Carry over the seeding points to the new label and adds them to a
		 * voxel priority queue
		 */
		final PriorityQueue<WatershedVoxel> pq = new PriorityQueue<>();

		// Only iterate seeds that are not excluded by the mask
		final IterableRegion<B> maskRegions = Regions.iterable(mask);
		final IterableInterval<LabelingType<Integer>> seedsMasked = Regions.sample(maskRegions, seeds);
		final Cursor<LabelingType<Integer>> cursorSeeds = seedsMasked.localizingCursor();

		while (cursorSeeds.hasNext()) {
			final Set<Integer> l = cursorSeeds.next();
			if (l.isEmpty()) {
				continue;
			}
			if (l.size() > 1) {
				throw new IllegalArgumentException("Seeds must have exactly one label!");
			}
			final Integer label = l.iterator().next();
			if (label < 0) {
				throw new IllegalArgumentException("Seeds must have positive integers as labels!");
			}
			raNeigh.setPosition(cursorSeeds);

			final Cursor<T> neighborhood = raNeigh.get().cursor();

			// Add unlabeled neighbors to priority queue
			while (neighborhood.hasNext()) {
				neighborhood.fwd();
				raSeeds.setPosition(neighborhood);
				raMask.setPosition(neighborhood);
				raOut.setPosition(neighborhood);
				final Integer labelNeigh = raOut.get().iterator().next();
				if (labelNeigh != INQUEUE && labelNeigh != OUTSIDE && !raOut.isOutOfBounds() && raMask.get().get() 
						&& raSeeds.get().isEmpty()) {
					raOut.setPosition(neighborhood);
					pq.add(new WatershedVoxel(IntervalIndexer.positionToIndex(neighborhood, in), neighborhood.get().getRealDouble()));
					raOut.get().clear();
					raOut.get().add(INQUEUE);
				}
			}

			// Overwrite label in output with the seed label
			raOut.setPosition(cursorSeeds);
			raOut.get().clear();
			raOut.get().add(label);
		}

		/*
		 * Pop the head of the priority queue, label and push all unlabeled
		 * neighbored pixels.
		 */

		// list to store neighbor labels
		final ArrayList<Integer> neighborLabels = new ArrayList<>();
		// list to store neighbor voxels
		final ArrayList<WatershedVoxel> neighborVoxels = new ArrayList<>();

		// iterate the queue
		final Point pos = new Point(in.numDimensions());
		while (!pq.isEmpty()) {
			IntervalIndexer.indexToPosition(pq.poll().getPos(), out, pos);

			// reset list of neighbor labels
			neighborLabels.clear();

			// reset list of neighbor voxels
			neighborVoxels.clear();

			// iterate the neighborhood of the pixel
			raNeigh.setPosition(pos);
			final Cursor<T> neighborhood = raNeigh.get().cursor();
			while (neighborhood.hasNext()) {
				neighborhood.fwd();
				// Unlabeled neighbors go into the queue if they are not there
				// yet
				raOut.setPosition(neighborhood);
				raMask.setPosition(raOut);
				if (!raOut.get().isEmpty()) {
					final Integer label = raOut.get().iterator().next();
					if (label == INIT && raMask.get().get()) {
						neighborVoxels.add(new WatershedVoxel(IntervalIndexer.positionToIndex(neighborhood, out),
							neighborhood.get().getRealDouble()));
					} else {
						if (label > WSHED && (!drawWatersheds || !neighborLabels.contains(label))) {
							// store labels of neighbors in a list
							neighborLabels.add(label);
						}
					}
				}

			}

			if (drawWatersheds) {
				// if the neighbors of the extracted voxel that have already
				// been labeled
				// all have the same label, then the voxel is labeled with their
				// label.
				raOut.setPosition(pos);
				raOut.get().clear();
				if (neighborLabels.size() == 1) {
					raOut.get().add(neighborLabels.get(0));
					// now that we know the voxel is labeled, add neighbors to
					// list
					for (final WatershedVoxel v : neighborVoxels) {
						IntervalIndexer.indexToPosition(v.getPos(), out, raOut);
						raOut.get().clear();
						raOut.get().add(INQUEUE);
						pq.add(v);
					}
				} else if (neighborLabels.size() > 1)
					raOut.get().add(WSHED);
			} else {
				if (neighborLabels.size() > 0) {
					raOut.setPosition(pos);
					raOut.get().clear();

					// take the label which most of the neighbors have
					if (neighborLabels.size() > 2) {
						final Map<Integer, Long> countLabels = neighborLabels.stream()
								.collect(Collectors.groupingBy(e -> e, Collectors.counting()));
						final Integer keyMax = Collections
								.max(countLabels.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey();
						raOut.get().add(keyMax);
					} else {
						raOut.get().add(neighborLabels.get(0));
					}
					// now that we know the voxel is labeled, add neighbors to
					// list
					for (final WatershedVoxel v : neighborVoxels) {
						IntervalIndexer.indexToPosition(v.getPos(), out, raOut);
						raOut.get().clear();
						raOut.get().add(INQUEUE);
						pq.add(v);
					}
				}
			}
		}

		/*
		 * Merge already present labels before calculation of watershed
		 */
		if (out() != null) {
			final Cursor<LabelingType<Integer>> cursor = out().cursor();
			while (cursor.hasNext()) {
				cursor.fwd();
				raOut.setPosition(cursor);
				final List<Integer> labels = new ArrayList<>();
				cursor.get().iterator().forEachRemaining(labels::add);
				raOut.get().addAll(labels);
			}
		}

	}

	@Override
	public boolean conforms() {
		boolean conformed = true;
		if (mask != null) {
			conformed = Intervals.equalDimensions(mask, in());
		}
		conformed &= Intervals.equalDimensions(seeds, in());
		return conformed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImgLabeling<Integer, IntType> createOutput(final RandomAccessibleInterval<T> in) {
		return createOp.calculate(in);
	}

	@Override
	public void initialize() {
		createOp = Functions.unary(ops(), CreateImgLabelingFromInterval.class, ImgLabeling.class, in());
	}

	/**
	 * Used to store the voxels in the priority queue. "Lower" voxels will be
	 * given out first. If two voxels have the same value, the one which joined
	 * the queue earlier will be given out.
	 */
	class WatershedVoxel implements Comparable<WatershedVoxel> {

		private final long pos;
		private final double value;
		private final long seqNum;

		public WatershedVoxel(final long pos, final double value) {
			this.pos = pos;
			this.value = value;
			seqNum = seq.getAndIncrement();
		}

		public long getPos() {
			return pos;
		}

		public double getValue() {
			return value;
		}

		@Override
		public int compareTo(WatershedVoxel o) {
			int res = Double.compare(value, o.value);
			if (res == 0)
				res = (seqNum < o.seqNum ? -1 : 1);

			return res;
		}

	}

}
