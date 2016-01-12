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

package net.imagej.ops.threshold.apply;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imagej.ops.threshold.localBernsen.LocalBernsen;
import net.imagej.ops.threshold.localContrast.LocalContrast;
import net.imagej.ops.threshold.localMean.LocalMean;
import net.imagej.ops.threshold.localMedian.LocalMedian;
import net.imagej.ops.threshold.localMidGrey.LocalMidGrey;
import net.imagej.ops.threshold.localNiblack.LocalNiblack;
import net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar;
import net.imagej.ops.threshold.localSauvola.LocalSauvola;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link LocalThreshold} and various {@link LocalThresholdMethod}s.
 *
 * @author Jonathan Hale
 * @author Martin Horn
 * @see LocalThreshold
 * @see LocalThresholdMethod
 */
public class LocalThresholdTest extends AbstractOpTest {

	Img<ByteType> in;
	Img<BitType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteArrayTestImg(true, new long[] { 10, 10 });

		out = in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	/**
	 * Test whether parameters for ops in {@link ThresholdNamespace} opmethods are
	 * correctly set.
	 */
	@Test
	public void testOpMethods() {
		final BitType out = new BitType();
		final Pair<ByteType, Iterable<ByteType>> in = new ValuePair<>(
			new ByteType(), Arrays.asList(new ByteType(), new ByteType()));

		ops.threshold().localBernsen(out, in, 1.0, Double.MAX_VALUE * 0.5);
		ops.threshold().localContrast(out, in);
		ops.threshold().localMean(out, in, 1.0);
		ops.threshold().localMedian(out, in, 1.0);
		ops.threshold().localMidGrey(out, in, 1.0);
		ops.threshold().localNiblack(out, in, 1.0, 2.0);
		ops.threshold().localPhansalkar(out, in, 0.25, 0.5);
		ops.threshold().localPhansalkar(out, in);
		ops.threshold().localSauvola(out, in, 0.5, 0.5);
		ops.threshold().localSauvola(out, in);
	}

	/**
	 * @see LocalBernsen
	 */
	@Test
	public void testLocalBernsen() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalBernsen.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 1.0,
				Double.MAX_VALUE * 0.5), new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalContrast
	 */
	@Test
	public void testLocalContrast() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalContrast.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in)),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalMean
	 */
	@Test
	public void testLocalMean() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalMean.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalMedian
	 */
	@Test
	public void testLocalMedian() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalMedian.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalMidGrey
	 */
	@Test
	public void testLocalMidGrey() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalMidGrey.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalNiblack
	 */
	@Test
	public void testLocalNiblack() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalNiblack.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0, 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalPhansalkar
	 */
	@Test
	public void testLocalPhansalkar() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalPhansalkar.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0, 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), false);
	}

	/**	 
	 * @see LocalSauvola
	 */
	@Test
	public void testLocalSauvola() {
		ops.threshold().apply(
			out,
			in,
			ops.op(LocalSauvola.class, BitType.class,
				new ValuePair<ByteType, Iterable<ByteType>>(null, in), 0.0, 0.0),
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), false);
	}

}
