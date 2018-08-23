/*-
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

package net.imagej.ops.coloc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.imglib2.AbstractInterval;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;
import net.imglib2.View;
import net.imglib2.util.IntervalIndexer;
import net.imglib2.view.Views;

import org.scijava.util.IntArray;

/**
 * Randomly shuffles an image blockwise.
 * 
 * @author Curtis Rueden
 * @author Ellen T Arena
 *
 * @param <T> Type of image to be shuffled.
 */
public class ShuffledView<T> extends AbstractInterval implements
	RandomAccessibleInterval<T>, View
{

	private Random rng;
	private final RandomAccessibleInterval<T> image;
	private List<Integer> blockIndices;
	private int[] blockSize;
	private int[] blockDims;

	public ShuffledView(final RandomAccessibleInterval<T> image,
		final int[] blockSize, final long seed)
	{
		this(image, blockSize, null, seed);
	}

	public ShuffledView(final RandomAccessibleInterval<T> image,
		final int[] blockSize, final List<Integer> blockIndices)
	{
		this(image, blockSize, blockIndices, 0);
	}

	private ShuffledView(final RandomAccessibleInterval<T> image,
		final int[] blockSize, final List<Integer> blockIndices, final long seed)
	{
		super(image); // uses same bounds as the input image
		this.image = image;
		this.blockSize = blockSize;

		// compute some info about our block sizes
		final int numDims = image.numDimensions();
		blockDims = new int[numDims];
		long totalBlocks = 1;
		for (int d = 0; d < numDims; d++) {
			final long blockDim = image.dimension(d) / blockSize[d];
			if (blockDim * blockSize[d] != image.dimension(d)) {
				throw new IllegalArgumentException("Image dimension #" + d +
					" is not evenly divisible by block size:" + blockSize[d]);
			}
			if (blockDim > Integer.MAX_VALUE) {
				throw new UnsupportedOperationException("Block dimension #" + d +
					" is too large: " + blockDim);
			}
			blockDims[d] = (int) blockDim;
			totalBlocks *= blockDims[d];
		}
		if (totalBlocks > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("Too many blocks: " +
				totalBlocks);
		}
		if (blockIndices == null) {
			this.blockIndices = createBlocks((int) totalBlocks);
			rng = new Random(seed);
			shuffleBlocks();
		} else {
			this.blockIndices = blockIndices;
		}
	}

	private static List<Integer> createBlocks(final int blockCount)
	{
		// generate the identity mapping of indices
		final IntArray blocks = new IntArray();
		blocks.ensureCapacity(blockCount);
		for (int b = 0; b < blockCount; b++)
			blocks.addValue(b);
		return blocks;
	}

	public void shuffleBlocks() {
		if (rng == null) {
			throw new IllegalStateException("No seed provided. Cannot shuffle.");
		}
		Collections.shuffle(blockIndices, rng);
	}
	
	@Override
	public RandomAccess<T> randomAccess() {
		return new ShuffledRandomAccess();
	}

	@Override
	public RandomAccess<T> randomAccess(final Interval interval) {
		return randomAccess(); // FIXME
	}

	private class ShuffledRandomAccess extends Point implements RandomAccess<T> {

		public ShuffledRandomAccess() {
			super(image.numDimensions());
		}

		@Override
		public T get() {
			// Convert from image coordinates to block coordinates.
			final long[] blockPos = new long[position.length];
			final long[] blockOffset = new long[position.length];
			for (int d = 0; d < position.length; d++) {
				blockPos[d] = position[d] / blockSize[d];
				blockOffset[d] = position[d] % blockSize[d];
			}

			// Convert N-D block coordinates to 1D block index.
			final int blockIndex = IntervalIndexer.positionToIndex(blockPos,
				blockDims);

			// Map block index to shuffled block index.
			final int shuffledBlockIndex = blockIndices.get(blockIndex);

			// Now convert our 1D shuffled block index back to N-D block
			// coordinates.
			final long[] shuffledBlockPos = new long[position.length];
			IntervalIndexer.indexToPosition(shuffledBlockIndex, blockDims,
				shuffledBlockPos);

			// Finally, position the original image according to our shuffled
			// position.
			final RandomAccess<T> imageRA = image.randomAccess();
			for (int d = 0; d < position.length; d++) {
				final long pd = shuffledBlockPos[d] * blockSize[d] + blockOffset[d];
				imageRA.setPosition(pd, d);
			}
			return imageRA.get();
		}

		@Override
		public Sampler<T> copy() {
			throw new UnsupportedOperationException();
		}

		@Override
		public RandomAccess<T> copyRandomAccess() {
			throw new UnsupportedOperationException();
		}
	}

	public static <T> RandomAccessibleInterval<T> cropAtMin(
		final RandomAccessibleInterval<T> image, final int[] blockSize)
	{
		return cropAt(image, blockSize, new Point(image.numDimensions()));
	}

	public static <T> RandomAccessibleInterval<T> cropAtMax(
		final RandomAccessibleInterval<T> image, final int[] blockSize)
	{
		final long[] pos = new long[image.numDimensions()];
		for (int d = 0; d < pos.length; d++) {
			pos[d] = image.dimension(d) % blockSize[d];
		}
		return cropAt(image, blockSize, new Point(pos));
	}

	public static <T> RandomAccessibleInterval<T> cropAtCenter(
		final RandomAccessibleInterval<T> image, final int[] blockSize)
	{
		final long[] pos = new long[image.numDimensions()];
		for (int d = 0; d < pos.length; d++) {
			pos[d] = (image.dimension(d) % blockSize[d]) / 2;
		}
		return cropAt(image, blockSize, new Point(pos));
	}

	private static <T> RandomAccessibleInterval<T> cropAt(
		final RandomAccessibleInterval<T> image, final int[] blockSize,
		final Localizable offset)
	{
		final int numDims = image.numDimensions();
		final long[] minsize = new long[numDims * 2];
		for (int d = 0; d < numDims; d++) {
			minsize[d] = offset.getLongPosition(d);
			final long shaveSize = image.dimension(d) % blockSize[d];
			minsize[numDims + d] = image.dimension(d) - shaveSize;
		}
		return Views.interval(image, FinalInterval.createMinSize(minsize));
	}
}
