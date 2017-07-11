/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 ImageJ developers.
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

package net.imagej.ops.image.invert;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.Unsigned12BitType;
import net.imglib2.type.numeric.integer.Unsigned2BitType;
import net.imglib2.type.numeric.integer.Unsigned4BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;

import org.junit.Test;

/**
 * @author Martin Horn (University of Konstanz)
 * @author Gabe Selzer
 */
public class InvertTest extends AbstractOpTest {

	@Test
	public void testBitTypeInvert() {
		Img<BitType> inBitType = generateBitArrayTestImg(true, 10, 10);
		Img<BitType> outBitType = inBitType.factory().create(inBitType,
			new BitType());
		assertInvertDefault(inBitType, outBitType);
		assertInvertMinMaxProvided(inBitType, outBitType, new BitType(false),
			new BitType(true));
		assertInvertMinMaxProvided(inBitType, outBitType, new BitType(false),
			new BitType(false));
	}
	
	@Test
	public void testByteTypeInvert() {
		Img<ByteType> inByteType = generateByteArrayTestImg(true, 5, 5);
		Img<ByteType> outByteType = inByteType.factory().create(inByteType,
			new ByteType());
		assertInvertDefault(inByteType, outByteType);
		assertInvertMinMaxProvided(inByteType, outByteType, new ByteType((byte) 0),
			new ByteType((byte) 0));
		assertInvertMinMaxProvided(inByteType, outByteType, new ByteType((byte) 20),
			new ByteType((byte) 10));
		assertInvertMinMaxProvided(inByteType, outByteType, new ByteType((byte) 256),
			new ByteType((byte) 256));
	}
	
	@Test
	public void testUnsigned2BitTypeInvert() {
		Img<Unsigned2BitType> inUnsigned2BitType = generateUnsigned2BitArrayTestImg(true, 5, 5);
		Img<Unsigned2BitType> outUnsigned2BitType = inUnsigned2BitType.factory().create(inUnsigned2BitType,
			new Unsigned2BitType());
		assertInvertDefault(inUnsigned2BitType, outUnsigned2BitType);
		assertInvertMinMaxProvided(inUnsigned2BitType, outUnsigned2BitType, new Unsigned2BitType(2), new Unsigned2BitType(3));
	}
	
	@Test
	public void testUnsigned4BitTypeInvert() {
		Img<Unsigned4BitType> inUnsigned4BitType = generateUnsigned4BitArrayTestImg(true, 5, 5);
		Img<Unsigned4BitType> outUnsigned4BitType = inUnsigned4BitType.factory().create(inUnsigned4BitType,
			new Unsigned4BitType());
		assertInvertDefault(inUnsigned4BitType, outUnsigned4BitType);
		assertInvertMinMaxProvided(inUnsigned4BitType, outUnsigned4BitType, new Unsigned4BitType(14), new Unsigned4BitType(15));
	}
	
	@Test
	public void testUnsigned12BitTypeInvert() {
		Img<Unsigned12BitType> inUnsigned12BitType = generateUnsigned12BitArrayTestImg(true, 5, 5);
		Img<Unsigned12BitType> outUnsigned12BitType = inUnsigned12BitType.factory().create(inUnsigned12BitType,
			new Unsigned12BitType());
		assertInvertDefault(inUnsigned12BitType, outUnsigned12BitType);
		assertInvertMinMaxProvided(inUnsigned12BitType, outUnsigned12BitType, new Unsigned12BitType(3025), new Unsigned12BitType(3846));
	}
	
	@Test
	public void testUnsignedByteTypeInvert() {
		Img<UnsignedByteType> inUnsignedByteType = generateUnsignedByteArrayTestImg(
			true, 5, 5);
		Img<UnsignedByteType> outUnsignedByteType = inUnsignedByteType.factory()
			.create(inUnsignedByteType, new UnsignedByteType());
		assertInvertDefault(inUnsignedByteType, outUnsignedByteType);
		assertInvertMinMaxProvided(inUnsignedByteType, outUnsignedByteType,
			new UnsignedByteType((byte) 127), new UnsignedByteType((byte) 127));
		assertInvertMinMaxProvided(inUnsignedByteType, outUnsignedByteType,
			new UnsignedByteType((byte) -12), new UnsignedByteType((byte) -10));
	}
	
	@Test
	public void testDoubleTypeInvert() {
		Img<DoubleType> inDoubleType = generateDoubleArrayTestImg(true, 5, 5);
		Img<DoubleType> outDoubleType = inDoubleType.factory().create(inDoubleType,
			new DoubleType());
		assertInvertDefault(inDoubleType, outDoubleType);
		assertInvertMinMaxProvided(inDoubleType, outDoubleType, new DoubleType(437d), new DoubleType(8008d));
		assertInvertMinMaxProvided(inDoubleType, outDoubleType, new DoubleType(5d), new DoubleType(Double.MAX_VALUE));
	}
	
	@Test
	public void testFloatTypeInvert() {
		Img<FloatType> inFloatType = generateFloatArrayTestImg(true, 5, 5);
		Img<FloatType> outFloatType = inFloatType.factory().create(inFloatType,
			new FloatType());
		assertInvertDefault(inFloatType, outFloatType);
 		assertInvertMinMaxProvided(inFloatType, outFloatType, new FloatType(0f), new FloatType(1f));
	}
	
	@Test
	public void testIntTypeInvert() {
		Img<IntType> inIntType = generateIntArrayTestImg(true, 5, 5);
		Img<IntType> outIntType = inIntType.factory().create(inIntType,
			new IntType());
		assertInvertDefault(inIntType, outIntType);
		assertInvertMinMaxProvided(inIntType, outIntType, new IntType(10),
			new IntType(40));
		assertInvertMinMaxProvided(inIntType, outIntType, new IntType(Integer.MIN_VALUE),
			new IntType(-10));
	}
	
	@Test
	public void testUnsignedIntTypeInvert() {
		Img<UnsignedIntType> inUnsignedIntType = generateUnsignedIntArrayTestImg(
			true, 5, 5);
		Img<UnsignedIntType> outUnsignedIntType = inUnsignedIntType.factory()
			.create(inUnsignedIntType, new UnsignedIntType());
		assertInvertDefault(inUnsignedIntType, outUnsignedIntType);
		assertInvertMinMaxProvided(inUnsignedIntType, outUnsignedIntType,
			new UnsignedIntType(237), new UnsignedIntType(257));
		assertInvertMinMaxProvided(inUnsignedIntType, outUnsignedIntType,
			new UnsignedIntType(10), new UnsignedIntType(-10));
	}
	
//	@Test
//	public void testLongTypeInvert() {
//		Img<LongType> inLongType = generateLongArrayTestImg(true, 5, 5);
//		Img<LongType> outLongType = inLongType.factory().create(inLongType,
//			new LongType());
//		assertInvertDefault(inLongType, outLongType);
//		assertBigInvertMinMaxProvided(inLongType, outLongType, new LongType(3025),
//			new LongType(3846));
//	}
	
//	@Test
//	public void testUnsignedLongTypeInvert() {
//		Img<UnsignedLongType> inUnsignedLongType = generateUnsignedLongArrayTestImg(
//			true, 5, 5);
//		Img<UnsignedLongType> outUnsignedLongType = inUnsignedLongType.factory()
//			.create(inUnsignedLongType, new UnsignedLongType());
//		assertInvertDefault(inUnsignedLongType, outUnsignedLongType);
//		assertBigInvertMinMaxProvided(inUnsignedLongType, outUnsignedLongType,
//			new UnsignedLongType(3025), new UnsignedLongType(3846));
//	}
	
	@Test
	public void testShortTypeInvert() {
		Img<ShortType> inShortType = generateShortArrayTestImg(true, 5, 5);
		Img<ShortType> outShortType = inShortType.factory().create(inShortType,
			new ShortType());
		assertInvertDefault(inShortType, outShortType);
		assertInvertMinMaxProvided(inShortType, outShortType,
			new ShortType((short)(Short.MIN_VALUE)), new ShortType((short)(Short.MIN_VALUE + 1)));
		assertInvertMinMaxProvided(inShortType, outShortType,
			new ShortType((short)(Short.MAX_VALUE)), new ShortType((short)(Short.MAX_VALUE - 1)));
	}
	
	@Test
	public void testUnsignedShortTypeInvert() {
		Img<UnsignedShortType> inUnsignedShortType =
				generateUnsignedShortArrayTestImg(true, 5, 5);
			Img<UnsignedShortType> outUnsignedShortType = inUnsignedShortType.factory()
				.create(inUnsignedShortType, new UnsignedShortType());
			assertInvertDefault(inUnsignedShortType, outUnsignedShortType);
			assertInvertMinMaxProvided(inUnsignedShortType, outUnsignedShortType,
				new UnsignedShortType((short) 437), new UnsignedShortType((short) 8008));
	}
	
	private <T extends RealType<T>> void assertInvertMinMaxProvided(Img<T> in,
		Img<T> out, RealType<T> min, RealType<T> max)
	{

		// unsigned type test

		ops.run(InvertII.class, out, in, (min), (max));

		RealType<T> firstIn = in.firstElement();
		RealType<T> firstOut = out.firstElement();
		
		double minMax = min.getRealDouble() + max.getRealDouble();

		minMax -= firstIn.getRealDouble();

		if(firstIn.getRealDouble() >= firstIn.getMaxValue() || minMax <= firstIn.getMinValue()) assertEquals(firstIn.getMinValue(), firstOut.getRealDouble(), 0.00005);
		else if(firstIn.getRealDouble() <= firstIn.getMinValue() || minMax >= firstIn.getMaxValue()) assertEquals(firstIn.getMaxValue(), firstOut.getRealDouble(), 0.00005);
		else {

		assertEquals(minMax, firstOut.getRealDouble(), 0.00005);
		
		}
	}

	private <T extends RealType<T>> void assertInvertDefault(Img<T> in, Img<T> out) {

		RealType<T> type = (RealType<T>) in.firstElement();
		ops.run(InvertII.class, out, in);

		RealType<T> firstIn = in.firstElement();
		RealType<T> firstOut = out.firstElement();

		assertEquals((type.getMinValue() + type.getMaxValue()) - firstIn
			.getRealDouble(), firstOut.getRealDouble(), 0);
	}
}
