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

package net.imagej.ops.slice;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.OpService;
import net.imagej.ops.slicer.Hyperslice;
import net.imagej.ops.slicer.Slicewise;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

/**
 * Testing functionality of SlicingIterableIntervals
 * 
 * @author Christian Dietz
 * @author Brian Northan
 */
public class HypersliceTest extends AbstractOpTest {

	private Img<ByteType> in;

	private ArrayImg<ByteType, ByteArray> out;

	@Override
	@Before
	public void setUp() {
		context = new Context(OpService.class);
		ops = context.service(OpService.class);

		in = ArrayImgs.bytes(20, 20, 21);
		out = ArrayImgs.bytes(20, 20, 21);

		// fill array img with values (plane position = value in px);

		for (final Cursor<ByteType> cur = in.cursor(); cur.hasNext();) {
			cur.fwd();
			cur.get().set((byte) cur.getIntPosition(2));
		}
	}

	@Test
	public void testXYCropping() {

		// selected interval XY
		final int[] xyAxis = new int[] { 0, 1 };

		ops.run(Slicewise.class, out, in, new DummyOp(), xyAxis);

		for (final Cursor<ByteType> cur = out.cursor(); cur.hasNext();) {
			cur.fwd();
			assertEquals(cur.getIntPosition(2), cur.get().getRealDouble(), 0);
		}
	}
	
	@Test
	public void testXYZCropping() {
		// the slices can end up being processed in parallel.  So try with a few different timepoint values
		// in order to test the chunker with various chunk sizes
		testXYZCropping(1);
		testXYZCropping(5);
		testXYZCropping(11);
		testXYZCropping(17);
		testXYZCropping(27);
	}
	
	private void testXYZCropping(int t) {
		
		Img<ByteType> inSequence=ArrayImgs.bytes(20, 20, 21, t);
		ArrayImg<ByteType, ByteArray> outSequence=ArrayImgs.bytes(20, 20, 21, t);
		
		// fill array img with values (plane position = value in px);
		for (final Cursor<ByteType> cur = inSequence.cursor(); cur.hasNext();) {
			cur.fwd();
			cur.get().set((byte) cur.getIntPosition(2));
		}
		
		// selected interval XYZ
		final int[] xyAxis = new int[] { 0, 1, 2 };

		ops.run(Slicewise.class, outSequence, inSequence, new DummyOp(), xyAxis);

		for (final Cursor<ByteType> cur = outSequence.cursor(); cur.hasNext();) {
			cur.fwd();
			assertEquals(cur.getIntPosition(2), cur.get().getRealDouble(), 0);
		}
	}

	@Test
	public void testNonZeroMinimumInterval() {

		Img<ByteType> img3D = ArrayImgs.bytes(50, 50, 3);
		IntervalView<ByteType> interval2D =
			Views.interval(img3D, new FinalInterval(new long[] { 25, 25, 2 },
				new long[] { 35, 35, 2 }));
		final int[] xyAxis = new int[] { 0, 1 };

		// iterate through every slice, should return a single
		// RandomAccessibleInterval<?> from 25, 25, 2 to 35, 35, 2

		final Hyperslice hyperSlices = new Hyperslice(ops, interval2D, xyAxis, true);
		final Cursor<RandomAccessibleInterval<?>> c = hyperSlices.cursor();
		int i = 0;
		while (c.hasNext()) {
			c.next();
			i++;
		}

		assertEquals(1, i);
	}


	@Test
	public void LoopThroughHyperSlicesTest() {
		final int xSize = 40;
		final int ySize = 50;
		final int numChannels = 3;
		final int numSlices = 25;
		final int numTimePoints = 5;

		final Img<UnsignedByteType> testImage = generateUnsignedByteTestImg(
				true, xSize, ySize, numChannels, numSlices, numTimePoints);

		final int[] axisIndices = new int[3];

		// set up the axis so the resulting hyperslices are x,y,z and
		// we loop through channels and time
		axisIndices[0] = 0;
		axisIndices[1] = 1;
		axisIndices[2] = 3;

		final Hyperslice hyperSlices = new Hyperslice(
				ops, testImage, axisIndices, true);

		final Cursor<RandomAccessibleInterval<?>> c = hyperSlices.cursor();

		int numHyperSlices = 0;
		while (c.hasNext()) {

			c.fwd();
			numHyperSlices++;
			try {
				final RandomAccessibleInterval<?> hyperSlice = c.get();

				assertEquals(3, hyperSlice.numDimensions());
				assertEquals(hyperSlice.dimension(0), xSize);
				assertEquals(hyperSlice.dimension(1), ySize);
				assertEquals(hyperSlice.dimension(2), numSlices);

			} catch (final Exception e) {
				System.out.println(e);
			}
		}

		assertEquals(numChannels * numTimePoints, numHyperSlices);

	}

	class DummyOp extends
			AbstractStrictFunction<Iterable<ByteType>, Iterable<ByteType>> {

		@Override
		public Iterable<ByteType> compute(final Iterable<ByteType> input,
				final Iterable<ByteType> output) {
			final Iterator<ByteType> itA = input.iterator();
			final Iterator<ByteType> itB = output.iterator();

			while (itA.hasNext() && itB.hasNext()) {
				itB.next().set(itA.next().get());
			}
			return output;
		}

	}
}
