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

package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.scif.img.IO;

import java.math.BigInteger;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.StreamSupport;

import net.imagej.types.UnboundedIntegerType;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.cell.CellImg;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.list.ListImg;
import net.imglib2.img.list.ListImgFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned128BitType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.Unsigned2BitType;
import net.imglib2.type.numeric.integer.Unsigned4BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.integer.UnsignedVariableBitLengthType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.junit.After;
import org.junit.Before;
import org.scijava.Context;
import org.scijava.cache.CacheService;
import org.scijava.plugin.Parameter;
import org.scijava.util.MersenneTwisterFast;

/**
 * Base class for {@link Op} unit testing.
 * <p>
 * <i>All</i> {@link Op} unit tests need to have an {@link OpService} instance.
 * Following the DRY principle, we should implement it only once. Here.
 * </p>
 *
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
public abstract class AbstractOpTest {

	@Parameter
	protected Context context;

	@Parameter
	protected OpService ops;

	@Parameter
	protected OpMatchingService matcher;

	/** Subclasses can override to create a context with different services. */
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
			CacheService.class);
	}

	/** Sets up a SciJava context with {@link OpService}. */
	@Before
	public void setUp() {
		createContext().inject(this);
	}

	/**
	 * Disposes of the {@link OpService} that was initialized in {@link #setUp()}.
	 */
	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
			ops = null;
			matcher = null;
		}
	}

	private int seed;

	private int pseudoRandom() {
		return seed = 3170425 * seed + 132102;
	}

	public ArrayImg<BitType, LongArray> generateBitArrayTestImg(
		final boolean fill, final long... dims)
	{
		ArrayImg<BitType, LongArray> bits = ArrayImgs.bits(dims);

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (BitType b : bits) {
				b.set(betterRNG.nextBoolean());
			}
		}
		return bits;
	}

	public ArrayImg<Unsigned2BitType, LongArray> generateUnsigned2BitArrayTestImg(
		final boolean fill, final long... dims)
	{
		ArrayImg<Unsigned2BitType, LongArray> bits = ArrayImgs.unsigned2Bits(dims);

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (Unsigned2BitType b : bits) {
				b.set(betterRNG.nextLong());
			}
		}
		return bits;
	}

	public ArrayImg<Unsigned4BitType, LongArray> generateUnsigned4BitArrayTestImg(
		final boolean fill, final long... dims)
	{
		ArrayImg<Unsigned4BitType, LongArray> bits = ArrayImgs.unsigned4Bits(dims);

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (Unsigned4BitType b : bits) {
				b.set(betterRNG.nextLong());
			}
		}
		return bits;
	}

	public ArrayImg<Unsigned12BitType, LongArray>
		generateUnsigned12BitArrayTestImg(final boolean fill, final long... dims)
	{
		ArrayImg<Unsigned12BitType, LongArray> bits = ArrayImgs.unsigned12Bits(
			dims);

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (Unsigned12BitType b : bits) {
				b.set(betterRNG.nextLong());
			}
		}
		return bits;
	}

	public ArrayImg<Unsigned128BitType, LongArray>
		generateUnsigned128BitArrayTestImg(final boolean fill, final long... dims)
	{
		ArrayImg<Unsigned128BitType, LongArray> bits = ArrayImgs.unsigned128Bits(
			dims);

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (Unsigned128BitType b : bits) {
				BigInteger big = BigInteger.valueOf(betterRNG.nextLong());
				b.set(big);
			}
		}
		return bits;
	}

	public ArrayImg<ByteType, ByteArray> generateByteArrayTestImg(
		final boolean fill, final long... dims)
	{
		final byte[] array = new byte[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) pseudoRandom();
			}
		}

		return ArrayImgs.bytes(array, dims);
	}

	public ArrayImg<UnsignedByteType, ByteArray> generateUnsignedByteArrayTestImg(
		final boolean fill, final long... dims)
	{
		final byte[] array = new byte[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (byte) pseudoRandom();
			}
		}

		return ArrayImgs.unsignedBytes(array, dims);
	}

	public ArrayImg<IntType, IntArray> generateIntArrayTestImg(final boolean fill,
		final long... dims)
	{
		final int[] array = new int[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (int) pseudoRandom() / (int) Integer.MAX_VALUE;
			}
		}

		return ArrayImgs.ints(array, dims);
	}

	public ArrayImg<UnsignedIntType, IntArray> generateUnsignedIntArrayTestImg(
		final boolean fill, final long... dims)
	{
		final int[] array = new int[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (int) pseudoRandom() / (int) Integer.MAX_VALUE;
			}
		}

		return ArrayImgs.unsignedInts(array, dims);
	}

	public CellImg<ByteType, ?> generateByteTestCellImg(final boolean fill,
		final long... dims)
	{
		final CellImg<ByteType, ?> img = new CellImgFactory<ByteType>().create(dims,
			new ByteType());

		if (fill) {
			final Cursor<ByteType> c = img.cursor();
			while (c.hasNext())
				c.next().set((byte) pseudoRandom());
		}

		return img;
	}

	public CellImg<ByteType, ?> generateByteTestCellImg(final boolean fill,
		final int[] cellDims, final long... dims)
	{
		final CellImg<ByteType, ?> img = new CellImgFactory<ByteType>(cellDims)
			.create(dims, new ByteType());

		if (fill) {
			final Cursor<ByteType> c = img.cursor();
			while (c.hasNext())
				c.next().set((byte) pseudoRandom());
		}

		return img;
	}

	public ArrayImg<DoubleType, DoubleArray> generateDoubleArrayTestImg(
		final boolean fill, final long... dims)
	{
		final double[] array = new double[(int) Intervals.numElements(
			new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (double) pseudoRandom() / (double) Integer.MAX_VALUE;
			}
		}

		return ArrayImgs.doubles(array, dims);
	}

	public ArrayImg<LongType, LongArray> generateLongArrayTestImg(
		final boolean fill, final long... dims)
	{
		final long[] array = new long[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (long) (pseudoRandom() / Integer.MAX_VALUE);
			}
		}

		return ArrayImgs.longs(array, dims);
	}

	public ArrayImg<UnsignedLongType, LongArray> generateUnsignedLongArrayTestImg(
		final boolean fill, final long... dims)
	{
		final long[] array = new long[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (long) (pseudoRandom() / Integer.MAX_VALUE);
			}
		}

		return ArrayImgs.unsignedLongs(array, dims);
	}

	public ArrayImg<ShortType, ShortArray> generateShortArrayTestImg(
		final boolean fill, final long... dims)
	{
		final short[] array = new short[(int) Intervals.numElements(
			new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (short) (pseudoRandom() / Integer.MAX_VALUE);
			}
		}

		return ArrayImgs.shorts(array, dims);
	}

	public ArrayImg<UnsignedShortType, ShortArray>
		generateUnsignedShortArrayTestImg(final boolean fill, final long... dims)
	{
		final short[] array = new short[(int) Intervals.numElements(
			new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (short) (pseudoRandom() / Integer.MAX_VALUE);
			}
		}

		return ArrayImgs.unsignedShorts(array, dims);
	}

	public ArrayImg<UnsignedVariableBitLengthType, LongArray>
		generateUnsignedVariableBitLengthTypeArrayTestImg(final boolean fill,
			final int nbits, final long... dims)
	{
		final long[] array = new long[(int) Intervals.numElements(new FinalInterval(
			dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (long) (((pseudoRandom() / Integer.MAX_VALUE)) % (Math.pow(2, nbits))) ;
			}
		}

		final LongArray l = new LongArray(array);

		return ArrayImgs.unsignedVariableBitLengths(l, nbits, dims);
	}

	public ListImg<UnboundedIntegerType> generateUnboundedIntegerTypeListTestImg(
		final boolean fill, final long... dims)
	{

		final ListImg<UnboundedIntegerType> l =
			new ListImgFactory<UnboundedIntegerType>().create(dims,
				new UnboundedIntegerType());

		final BigInteger[] array = new BigInteger[(int) Intervals.numElements(
			dims)];

		RandomAccess<UnboundedIntegerType> ra = l.randomAccess();

		if (fill) {
			MersenneTwisterFast betterRNG = new MersenneTwisterFast(0xf1eece);
			for (int i = 0; i < Intervals.numElements(dims); i++) {
				BigInteger val = BigInteger.valueOf(betterRNG.nextLong());
				ra.get().set(val);
				ra.fwd(0);
			}
		}

		return l;
	}

	public Img<UnsignedByteType>
		generateRandomlyFilledUnsignedByteTestImgWithSeed(final long[] dims,
			final long tempSeed)
	{

		final Img<UnsignedByteType> img = ArrayImgs.unsignedBytes(dims);

		final Random rand = new Random(tempSeed);
		final Cursor<UnsignedByteType> cursor = img.cursor();
		while (cursor.hasNext()) {
			cursor.next().set(rand.nextInt((int) img.firstElement().getMaxValue()));
		}

		return img;
	}

	public ArrayImg<FloatType, FloatArray> generateFloatArrayTestImg(
		final boolean fill, final long... dims)
	{
		final float[] array = new float[(int) Intervals.numElements(
			new FinalInterval(dims))];

		if (fill) {
			seed = 17;
			for (int i = 0; i < array.length; i++) {
				array[i] = (float) pseudoRandom() / (float) Integer.MAX_VALUE;
			}
		}

		return ArrayImgs.floats(array, dims);
	}

	public Img<FloatType> openFloatImg(final String resourcePath) {
		return openFloatImg(getClass(), resourcePath);
	}

	public Img<DoubleType> openDoubleImg(final String resourcePath) {
		return openDoubleImg(getClass(), resourcePath);
	}

	public static Img<FloatType> openFloatImg(final Class<?> c,
		final String resourcePath)
	{
		final URL url = c.getResource(resourcePath);
		return IO.openFloatImgs(url.getPath()).get(0).getImg();
	}

	public static Img<UnsignedByteType> openUnsignedByteType(final Class<?> c,
		final String resourcePath)
	{
		final URL url = c.getResource(resourcePath);
		return IO.openUnsignedByteImgs(url.getPath()).get(0).getImg();
	}

	public static Img<DoubleType> openDoubleImg(final Class<?> c,
		final String resourcePath)
	{
		final URL url = c.getResource(resourcePath);
		return IO.openDoubleImgs(url.getPath()).get(0).getImg();
	}

	public <T> void assertIterationsEqual(final Iterable<T> expected,
		final Iterable<T> actual)
	{
		final Iterator<T> e = expected.iterator();
		final Iterator<T> a = actual.iterator();
		while (e.hasNext()) {
			assertTrue("Fewer elements than expected", a.hasNext());
			assertEquals(e.next(), a.next());
		}
		assertFalse("More elements than expected", a.hasNext());
	}

	public static <T> RandomAccessible<T> deinterval(
		RandomAccessibleInterval<T> input)
	{
		return Views.extendBorder(input);
	}

	public <T extends RealType<T>> boolean areCongruent(
		final IterableInterval<T> in, final RandomAccessible<T> out,
		final double epsilon)
	{
		Cursor<T> cin = in.localizingCursor();
		RandomAccess<T> raOut = out.randomAccess();

		while (cin.hasNext()) {
			cin.fwd();
			raOut.setPosition(cin);
			if (Math.abs(cin.get().getRealDouble() - raOut.get()
				.getRealDouble()) > epsilon) return false;
		}
		return true;
	}

	public <T extends RealType<T>> double[] asArray(final Iterable<T> image) {
		return StreamSupport.stream(image.spliterator(), false).mapToDouble(t -> t
			.getRealDouble()).toArray();
	}

	public static class NoOp extends AbstractOp {

		@Override
		public void run() {
			// NB: No implementation needed.
		}
	}

}
