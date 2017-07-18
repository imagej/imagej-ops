/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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
package net.imagej.ops.transform.concatenateView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.operators.ValueEquals;
import net.imglib2.util.Intervals;
import net.imglib2.util.Pair;
import net.imglib2.view.IntervalView;
import net.imglib2.view.StackView.StackAccessMode;
import net.imglib2.view.Views;

/**
 * Tests {@link net.imagej.ops.Ops.Transform.ConcatenateView} ops.
 * <p>
 * This test only checks if the op call works with all parameters and that the
 * result is equal to that of the {@link Views} method call. It is not a
 * correctness test of {@link Views} itself.
 * </p>
 *
 * @author Philipp Hanslovsky
 */
public class ConcatenateViewTest extends AbstractOpTest {

	private final long[] dim = { 3, 4, 5, 6 };

	private final long divider = 3;

	private final int axis = 3;final long numElements = Intervals.numElements( dim );
	final Random rng = new Random();
	final byte[] data = new byte[ ( int ) numElements ];
	final ArrayImg< ByteType, ByteArray > img = ArrayImgs.bytes( data, dim );

	@Before
	public void fillData()
	{
		rng.nextBytes( data );
	}

	private < T > List< RandomAccessibleInterval< T > > createIntervals( final RandomAccessibleInterval< T > source, final long divider, final int axis )
	{
		final long[] min = Intervals.minAsLongArray( source );
		final long[] max = Intervals.maxAsLongArray( source );
		final long[] min1 = min.clone();
		final long[] min2 = min.clone();
		final long[] max1 = max.clone();
		final long[] max2 = max.clone();
		max1[ axis ] = divider;
		min2[ axis ] = divider + 1;
		final IntervalView< T > interval1 = Views.interval( source, min1, max1 );
		final IntervalView< T > interval2 = Views.interval( source, min2, max2 );

		return Arrays.asList( interval1, interval2 );
	}

	private static < T extends ValueEquals< T > > void testEqual( final RandomAccessibleInterval< T > rai1, final RandomAccessibleInterval< T > rai2 )
	{
		Assert.assertArrayEquals( Intervals.minAsLongArray( rai1 ), Intervals.minAsLongArray( rai2 ) );
		Assert.assertArrayEquals( Intervals.maxAsLongArray( rai1 ), Intervals.maxAsLongArray( rai2 ) );
		for ( final Pair< T, T > p : Views.interval( Views.pair( rai1, rai2 ), rai1 ) )
			Assert.assertTrue( p.getA().valueEquals( p.getB() ) );
	}

	@Test
	public void defaultConcatenateTest() {
		final List< RandomAccessibleInterval< ByteType > > intervals = createIntervals( img, divider, axis );
		final RandomAccessibleInterval< ByteType > cat1 = Views.concatenate( axis, intervals );
		final RandomAccessibleInterval< ByteType > cat2 = ops.transform().concatenateView( intervals, axis );
		testEqual( cat1, cat2 );

	}

	@Test
	public void concatenateWithAccessModeTest() {
		final List< RandomAccessibleInterval< ByteType > > intervals = createIntervals( img, divider, axis );
		for ( final StackAccessMode mode : StackAccessMode.values() )
		{
			final RandomAccessibleInterval< ByteType > cat1 = Views.concatenate( axis, mode, intervals );
			final RandomAccessibleInterval< ByteType > cat2 = ops.transform().concatenateView( intervals, axis, mode );
			testEqual( cat1, cat2 );
		}
	}

}
