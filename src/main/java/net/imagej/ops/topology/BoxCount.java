/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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

package net.imagej.ops.topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An N-dimensional version of fixed-grid box counting algorithm that can be
 * used to estimate the fractal dimension of an interval
 * <p>
 * The algorithm repeatedly lays a fixed grid on the interval, and counts the
 * number of boxes that contain foreground. After each step the grid is made
 * finer by a factor of {@link #scaling}. If the objects in the interval are
 * fractal, the proportion of foreground boxes should increase as the boxes
 * get smaller.
 * </p>
 * <p>
 * Produces a set of points (log(foreground count), -log(box size)) for curve
 * fitting. The slope of the function gives the fractal dimension of the
 * interval.
 * </p>
 *
 * @author Richard Domander (Royal Veterinary College, London)
 * @author Per Christian Henden
 * @author Jens Bache-Wiig
 */
@Plugin(type = Ops.Topology.BoxCount.class)
public class BoxCount<B extends BooleanType<B>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<B>, List<ValuePair<DoubleType, DoubleType>>>
	implements Ops.Topology.BoxCount
{

	@Parameter
	private LogService logService;
	
	/** Starting size of the boxes in pixels */
	@Parameter(required = false, persist = false)
	private Long maxSize = 48L;

	/** Minimum size of the boxes in pixels */
	@Parameter(required = false, persist = false)
	private Long minSize = 6L;

	/** Box size downscaling factor */
	@Parameter(required = false, persist = false)
	private Double scaling = 1.2;

	/**
	 * Number of times the box grid is moved in each dimension to find the best fit
	 * <p>
	 * The best fitting grid covers the objects in the interval with the least
	 * amount of boxes. The default grid starts from [0, 0, ... 0].
	 * </p>
	 * <p>
	 * NB This is not a sliding box scan. The grid is moved a fix amount of times.
	 * </p>
	 * <p>
	 * NB Additional moves multiply algorithm's time complexity by n^d!
	 * </p>
	 */
	@Parameter(required = false, persist = false)
	private Long gridMoves = 0L;

	/**
	 * Counts the number of boxes that have foreground in the interval repeatedly with
	 * different size boxes
	 *
	 * @param input An n-dimensional binary interval.
	 * @return A list of (log(foreground count), -log(box size))
	 *         {@link ValuePair} objects for curve fitting
	 */
	@Override
	public List<ValuePair<DoubleType, DoubleType>> calculate(
		final RandomAccessibleInterval<B> input)
	{
		if (scaling < 1.0 || (scaling == 1.0 && maxSize > minSize)) {
			throw new IllegalArgumentException("Scaling must be > 1.0 or algorithm won't stop.");
		}

		final List<ValuePair<DoubleType, DoubleType>> points = new ArrayList<>();
		final int dimensions = input.numDimensions();
		final long[] sizes = new long[dimensions];
		input.dimensions(sizes);
		for (long boxSize = maxSize; boxSize >= minSize; boxSize /=
			scaling)
		{
			final long numTranslations = limitTranslations(boxSize,
					1 + gridMoves);
			final long translationAmount = boxSize / numTranslations;
			final Stream<long[]> translations = translationStream(
					numTranslations, translationAmount, dimensions - 1,
					new long[dimensions]);
			final LongStream foregroundCounts = countTranslatedGrids(input,
				translations, sizes, boxSize);
			final long foreground = foregroundCounts.min().orElse(0);
			final double logSize = -Math.log(boxSize);
			final double logCount = Math.log(foreground);
			final ValuePair<DoubleType, DoubleType> point = new ValuePair<>(
					new DoubleType(logSize), new DoubleType(logCount));
			points.add(point);
		}
		return points;
	}

	/**
	 * A helper method that culls unnecessary translations for finding the best fit.
	 * <p>
	 * For example, if size = 2 and there's a 2 x 2 x 2 object in a 3D image, then at worst
	 * it's in 8 different boxes. The best fit in this case is one box. However the boxes can
	 * only be adjusted by 1 pixel in each direction, so more than 2 translations is unnecessary.
	 * </p>
	 * <p>
	 * NB the minimum number of translations is 1, which means the boxes are counted once,
	 * and there are no attempts at adjusting their coordinates for a better fit.
	 * </p>
	 * @param size Size n of a box in the algorithm. E.g. in the 3D case it's n * n * n.
	 * @param translations Number of times the box counting grid is moved in each dimension to find the best fit.
	 * @return The original or maximum number of meaningful translations if the original was too many.
	 * @throws IllegalArgumentException if size is non-positive.
	 */
	static long limitTranslations(final long size, final long translations)
			throws IllegalArgumentException {
		if (size < 1L) {
			throw new IllegalArgumentException("Size must be positive");
		}

		if (translations < 1L) {
			return 1L;
		}

		return Math.min(size, translations);
	}

	/**
	 * Counts the number of foreground boxes in a grid with each translation
	 *
	 * @param input N-dimensional binary interval
	 * @param translations Stream of grid translation coordinates in n-dimensions
	 * @param sizes Sizes of the interval's dimensions in pixels
	 * @param boxSize Size of a box in the grids
	 * @return Foreground boxes counted with each translation
	 */
	private LongStream countTranslatedGrids(
			final RandomAccessibleInterval<B> input,
			final Stream<long[]> translations, final long[] sizes,
			final long boxSize) {
		return translations
				.mapToLong(t -> countForegroundBoxes(input, boxSize, sizes, t));
	}

	private int countNThreads(final long boxCount, final long boxSize) {
		final int processors = ops().getMaxThreads();
		// I tested the op runs faster with a single thread
		// when box size is very small
		return boxSize >= 2 ? (int) Math.min(processors, boxCount) : 1;
	}

	/**
	 * Counts the number of foreground boxes in a grid over the given interval
	 *
	 * @param sizes Sizes of the interval's dimensions in pixels
	 * @param translation Translation of the box grid in each dimension
	 * @param boxSize Size of a box (n * n * ... n)
	 */
	private long countForegroundBoxes(final RandomAccessibleInterval<B> input,
			final long boxSize, final long[] sizes, final long[] translation) {
		final long[] dimensions = new long[input.numDimensions()];
		input.dimensions(dimensions);
		long boxCount = 1;
		for (int i = 0; i < sizes.length; i++) {
			boxCount *= Math.ceil((1.0 * sizes[i] - translation[i]) / boxSize);
		}
		int nThreads = countNThreads(boxCount, boxSize);
		final long boxesPerThread = Math.max(1, boxCount / nThreads);
		final long remainder = boxCount % nThreads;

		final ExecutorService pool = Executors.newFixedThreadPool(nThreads);
		final List<Future<Long>> futures = new ArrayList<>(nThreads);
		for (int i = 0; i < nThreads; i++) {
			final long first = i * boxesPerThread;
			final long boxes = i < nThreads - 1 ? boxesPerThread :
					boxesPerThread + remainder;
			final Callable<Long> task = createCalculationTask(dimensions,
					boxSize, translation, first, input, boxes);
			futures.add(pool.submit(task));
		}

		long foregroundBoxes = 0;
		for (Future<Long> future : futures) {
			try {
				foregroundBoxes += future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		shutdownAndAwaitTermination(pool);
		return foregroundBoxes;
	}
	
	// Shuts down an ExecutorService as per recommended by Oracle
	private void shutdownAndAwaitTermination(final ExecutorService executor) {
		executor.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
				executor.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
					logService.trace("Pool did not terminate");
				}
			}
		}
		catch (final InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
			logService.trace(ie);
		}
	}

	/**
	 * Creates a task for parallel calculation of foreground boxes.
	 *
	 * @param dimensions Size of input image in each dimension
	 * @param boxSize Size of a box (n * n * ... n)
	 * @param translation Starting position of the box grid
	 * @param first Number of the first box for this thread
	 * @param input The input interval.
	 * @param boxes Number of boxes this thread counts
	 * @param <B> type of the elements in the input interval
	 * @return a task that counts which boxes have foreground in them.
	 */
	private static <B extends BooleanType<B>> Callable<Long> createCalculationTask(
			long[] dimensions, long boxSize, long[] translation, long first,
			RandomAccessibleInterval<B> input, long boxes) {
		return () -> {
			// I tried sharing a single generator between the threads,
			// but having to call next() and hasNext() in a synchronized block
			// created a bottle neck.
			final CoordinateGenerator boxer = new CoordinateGenerator(
					dimensions, boxSize, translation);
			boxer.skip(first);
			final RandomAccess<B> access = input.randomAccess();
			final long[] boxPosition = new long[access.numDimensions()];
			final long[] position = new long[access.numDimensions()];
			long foreground = 0;
			long generated = 0;
			while (generated < boxes) {
				boxer.next(boxPosition);
				final int d = access.numDimensions() - 1;
				if (hasBoxForeground(d, access, boxPosition, boxSize,
						dimensions, position)) {
					foreground++;
				}
				generated++;
			}
			return foreground;
		};
	}

	/**
	 * Recursively checks whether an N-dimensional box has any foreground pixels
	 * <p>
	 * Checks that box doesn't go out of bounds.
	 * </p>
	 * @param dimension Current dimension. Start from the last, numDimensions - 1
	 * @param access A random access to the input image
	 * @param boxStart Starting coordinates of the box
	 * @param boxSize Size of the box in each dimension
	 * @param sizes Dimensions of the input image
	 * @param position Current coordinates in the image
	 * @param <B> Type of pixels in the image
	 * @return true if any of the pixels is true
	 */
	private static <B extends BooleanType<B>> boolean hasBoxForeground(
			final int dimension, final RandomAccess<B> access,
			final long[] boxStart, final long boxSize, final long[] sizes,
			final long[] position) {
		long min = Math.max(boxStart[dimension], 0);
		long max = Math.min(boxStart[dimension] + boxSize, sizes[dimension]);
		for (long p = min; p < max; p++) {
			position[dimension] = p;
			if (dimension > 0) {
				if (hasBoxForeground(dimension - 1, access, boxStart, boxSize,
						sizes, position)) {
					return true;
				}
			} else {
				access.setPosition(position);
				if (access.get().get()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates a {@link Stream} of t * 2^n translations in n-dimensions
	 * <p>
	 * The elements in the stream are arrays of coordinates [0, 0, .. 0], [-i, 0,
	 * .. 0], [0, -i, 0, .. 0], .. [-i, -i, .. -i], [-2i, 0, .. 0] .. [-ti, -ti,
	 * .. -ti], where each array has n elements, i = number of pixels translated,
	 * and t = number of translations. If the translations were positive, a part
	 * of the interval would not get inspected, because it always starts from [0,
	 * 0, ... 0].
	 * </p>
	 * <p>
	 * The order of arrays in the stream is not guaranteed.
	 * </p>
	 *
	 * @param numTranslations Number of translations (1 produces
	 *          Stream.of(long[]{0, 0, .. 0}))
	 * @param amount Number of pixels shifted in translations
	 * @param dimension Current translation dimension (start from last)
	 * @param translation The accumulated position of the current translation
	 *          (start from {0, 0, .. 0})
	 * @return A stream of coordinates of the translations
	 */
	private static Stream<long[]> translationStream(final long numTranslations,
		final long amount, final int dimension, final long[] translation)
	{
		final Stream.Builder<long[]> builder = Stream.builder();
		generateTranslations(numTranslations, amount, dimension, translation,
			builder);
		return builder.build();
	}

	/**
	 * Adds translations to the given {@link Stream.Builder}
	 * 
	 * @see #translationStream(long, long, int, long[])
	 */
	private static void generateTranslations(final long numTranslations,
		final long amount, final int dimension, final long[] translation,
		final Stream.Builder<long[]> builder)
	{
		for (int t = 0; t < numTranslations; t++) {
			translation[dimension] = -t * amount;
			if (dimension == 0) {
				builder.add(translation.clone());
			} else {
				generateTranslations(numTranslations, amount, dimension - 1,
						translation, builder);
			}
		}
	}

	// Pre-calculating the coordinates of all the boxes would take a lot of
	// memory, when the input image is big and box size small. Thus the
	// generator that can be create the next coordinates as needed.
	/**
	 * Generates coordinates in an interval like an N-dimensional for-loop would
	 */
	private static class CoordinateGenerator {
		private final long[] dimensions;
		private final long[] position;
		private final long[] start;
		private final long increment;
		private boolean hasNext;

		/**
		 * Creates and initialises a generator.
		 *
		 * @param dimensions size of the interval in each dimension.
		 * @param increment increment between coordinates in each dimension.
		 * @param start starting coordinates of the interval.
		 */
		private CoordinateGenerator(final long[] dimensions,
				final long increment, long[] start) {
			this.start = new long[dimensions.length];
			System.arraycopy(start, 0, this.start, 0, start.length);
			position = new long[dimensions.length];
			System.arraycopy(this.start, 0, position, 0, this.start.length);
			this.increment = increment;
			this.dimensions = dimensions;
			hasNext = dimensions.length > 0 && Arrays.stream(dimensions)
					.allMatch(d -> d > 0);
		}

		/**
		 * Advances the coordinate position n steps
		 *
		 * @param n steps to skip
		 */
		private void skip(final long n) {
			int skipped = 0;
			while (skipped < n && hasNext) {
				incrementPosition(0);
				skipped++;
			}
		}

		private void next(final long[] position) {
			System.arraycopy(this.position, 0, position, 0, position.length);
			incrementPosition(0);
		}

		private void incrementPosition(int dimension) {
			if (dimension >= dimensions.length) {
				hasNext = false;
				return;
			}
			position[dimension] += increment;
			if (position[dimension] >= dimensions[dimension] ) {
				position[dimension] = start[dimension];
				incrementPosition(dimension + 1);
			}
		}
	}
}
