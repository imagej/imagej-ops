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

package net.imagej.ops.convert;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
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
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.plugin.Plugin;

/**
 * The convert namespace contains operations for converting between types.
 *
 * @author Curtis Rueden
 */

@Plugin(type = Namespace.class)
public class ConvertNamespace extends AbstractNamespace {

	// -- Convert namespace ops --

	@OpMethod(op = Ops.Convert.Clip.class)
	public Object clip(final Object... args) {
		return ops().run(Ops.Convert.Clip.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.clip.ClipRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O clip(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.clip.ClipRealTypes.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Copy.class)
	public Object copy(final Object... args) {
		return ops().run(Ops.Convert.Clip.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.copy.CopyRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O copy(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.copy.CopyRealTypes.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.ImageType.class)
	public Object imageType(final Object... args) {
		return ops().run(Ops.Convert.ImageType.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.convert.imageType.ConvertIterableIntervals.class)
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		imageType(final IterableInterval<O> out, final IterableInterval<I> in,
			final RealTypeConverter<I, O> typeConverter)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.convert.imageType.ConvertIterableIntervals.class, out,
				in, typeConverter);
		return result;
	}

	@OpMethod(op = Ops.Convert.NormalizeScale.class)
	public Object normalizeScale(final Object... args) {
		return ops().run(Ops.Convert.NormalizeScale.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.convert.normalizeScale.NormalizeScaleRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O normalizeScale(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(
				net.imagej.ops.convert.normalizeScale.NormalizeScaleRealTypes.class,
				out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Scale.class)
	public Object scale(final Object... args) {
		return ops().run(Ops.Convert.Scale.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.scale.ScaleRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O scale(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.scale.ScaleRealTypes.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Bit.class)
	public Object bit(final Object... args) {
		return ops().run(Ops.Convert.Bit.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToBit.class)
	public <C extends ComplexType<C>> BitType bit(final C in) {
		final BitType result =
			(BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToBit.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToBit.class)
	public <C extends ComplexType<C>> BitType bit(final BitType out, final C in) {
		final BitType result =
			(BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToBit.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint2.class)
	public Object uint2(final Object... args) {
		return ops().run(Ops.Convert.Uint2.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint2.class)
	public <C extends ComplexType<C>> Unsigned2BitType uint2(final C in) {
		final Unsigned2BitType result =
			(Unsigned2BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint2.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint2.class)
	public <C extends ComplexType<C>> Unsigned2BitType uint2(
		final Unsigned2BitType out, final C in)
	{
		final Unsigned2BitType result =
			(Unsigned2BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint2.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint2.class)
	public <T extends IntegerType<T>> Unsigned2BitType uint2(final T in) {
		final Unsigned2BitType result =
			(Unsigned2BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint2.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint2.class)
	public <T extends IntegerType<T>> Unsigned2BitType uint2(
		final Unsigned2BitType out, final T in)
	{
		final Unsigned2BitType result =
			(Unsigned2BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint2.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint4.class)
	public Object uint4(final Object... args) {
		return ops().run(Ops.Convert.Uint4.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint4.class)
	public <C extends ComplexType<C>> Unsigned4BitType uint4(final C in) {
		final Unsigned4BitType result =
			(Unsigned4BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint4.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint4.class)
	public <C extends ComplexType<C>> Unsigned4BitType uint4(
		final Unsigned4BitType out, final C in)
	{
		final Unsigned4BitType result =
			(Unsigned4BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint4.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint4.class)
	public <T extends IntegerType<T>> Unsigned4BitType uint4(final T in) {
		final Unsigned4BitType result =
			(Unsigned4BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint4.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint4.class)
	public <T extends IntegerType<T>> Unsigned4BitType uint4(
		final Unsigned4BitType out, final T in)
	{
		final Unsigned4BitType result =
			(Unsigned4BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint4.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Int8.class)
	public Object int8(final Object... args) {
		return ops().run(Ops.Convert.Int8.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt8.class)
	public <C extends ComplexType<C>> ByteType int8(final C in) {
		final ByteType result =
			(ByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt8.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt8.class)
	public <C extends ComplexType<C>> ByteType
		int8(final ByteType out, final C in)
	{
		final ByteType result =
			(ByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt8.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt8.class)
	public <T extends IntegerType<T>> ByteType int8(final T in) {
		final ByteType result =
			(ByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt8.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt8.class)
	public <T extends IntegerType<T>> ByteType
		int8(final ByteType out, final T in)
	{
		final ByteType result =
			(ByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt8.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint8.class)
	public Object uint8(final Object... args) {
		return ops().run(Ops.Convert.Uint8.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint8.class)
	public <C extends ComplexType<C>> UnsignedByteType uint8(final C in) {
		final UnsignedByteType result =
			(UnsignedByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint8.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint8.class)
	public <C extends ComplexType<C>> UnsignedByteType uint8(
		final UnsignedByteType out, final C in)
	{
		final UnsignedByteType result =
			(UnsignedByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint8.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint8.class)
	public <T extends IntegerType<T>> UnsignedByteType uint8(final T in) {
		final UnsignedByteType result =
			(UnsignedByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint8.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint8.class)
	public <T extends IntegerType<T>> UnsignedByteType uint8(
		final UnsignedByteType out, final T in)
	{
		final UnsignedByteType result =
			(UnsignedByteType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint8.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint12.class)
	public Object uint12(final Object... args) {
		return ops().run(Ops.Convert.Uint12.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint12.class)
	public <C extends ComplexType<C>> Unsigned12BitType uint12(final C in) {
		final Unsigned12BitType result =
			(Unsigned12BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint12.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint12.class)
	public <C extends ComplexType<C>> Unsigned12BitType uint12(
		final Unsigned12BitType out, final C in)
	{
		final Unsigned12BitType result =
			(Unsigned12BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint12.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint12.class)
	public <T extends IntegerType<T>> Unsigned12BitType uint12(final T in) {
		final Unsigned12BitType result =
			(Unsigned12BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint12.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint12.class)
	public <T extends IntegerType<T>> Unsigned12BitType uint12(
		final Unsigned12BitType out, final T in)
	{
		final Unsigned12BitType result =
			(Unsigned12BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint12.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Int16.class)
	public Object int16(final Object... args) {
		return ops().run(Ops.Convert.Int16.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt16.class)
	public <C extends ComplexType<C>> ShortType int16(final C in) {
		final ShortType result =
			(ShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt16.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt16.class)
	public <C extends ComplexType<C>> ShortType int16(final ShortType out,
		final C in)
	{
		final ShortType result =
			(ShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt16.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt16.class)
	public <T extends IntegerType<T>> ShortType int16(final T in) {
		final ShortType result =
			(ShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt16.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt16.class)
	public <T extends IntegerType<T>> ShortType int16(final ShortType out,
		final T in)
	{
		final ShortType result =
			(ShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt16.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint16.class)
	public Object uint16(final Object... args) {
		return ops().run(Ops.Convert.Uint16.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint16.class)
	public <C extends ComplexType<C>> UnsignedShortType uint16(final C in) {
		final UnsignedShortType result =
			(UnsignedShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint16.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint16.class)
	public <C extends ComplexType<C>> UnsignedShortType uint16(
		final UnsignedShortType out, final C in)
	{
		final UnsignedShortType result =
			(UnsignedShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint16.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint16.class)
	public <T extends IntegerType<T>> UnsignedShortType uint16(final T in) {
		final UnsignedShortType result =
			(UnsignedShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint16.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint16.class)
	public <T extends IntegerType<T>> UnsignedShortType uint16(
		final UnsignedShortType out, final T in)
	{
		final UnsignedShortType result =
			(UnsignedShortType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint16.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Int32.class)
	public Object int32(final Object... args) {
		return ops().run(Ops.Convert.Int32.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt32.class)
	public <T extends IntegerType<T>> IntType int32(final T in) {
		final IntType result =
			(IntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt32.class)
	public <T extends IntegerType<T>> IntType
		int32(final IntType out, final T in)
	{
		final IntType result =
			(IntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt32.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt32.class)
	public <C extends ComplexType<C>> IntType int32(final C in) {
		final IntType result =
			(IntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt32.class)
	public <C extends ComplexType<C>> IntType
		int32(final IntType out, final C in)
	{
		final IntType result =
			(IntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt32.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint32.class)
	public Object uint32(final Object... args) {
		return ops().run(Ops.Convert.Uint32.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint32.class)
	public <C extends ComplexType<C>> UnsignedIntType uint32(final C in) {
		final UnsignedIntType result =
			(UnsignedIntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint32.class)
	public <C extends ComplexType<C>> UnsignedIntType uint32(
		final UnsignedIntType out, final C in)
	{
		final UnsignedIntType result =
			(UnsignedIntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint32.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint32.class)
	public <T extends IntegerType<T>> UnsignedIntType uint32(final T in) {
		final UnsignedIntType result =
			(UnsignedIntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint32.class)
	public <T extends IntegerType<T>> UnsignedIntType uint32(
		final UnsignedIntType out, final T in)
	{
		final UnsignedIntType result =
			(UnsignedIntType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint32.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Int64.class)
	public Object int64(final Object... args) {
		return ops().run(Ops.Convert.Int64.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt64.class)
	public <C extends ComplexType<C>> LongType int64(final C in) {
		final LongType result =
			(LongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToInt64.class)
	public <C extends ComplexType<C>> LongType int64(final LongType out,
		final C in)
	{
		final LongType result =
			(LongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToInt64.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt64.class)
	public <T extends IntegerType<T>> LongType int64(final T in) {
		final LongType result =
			(LongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToInt64.class)
	public <T extends IntegerType<T>> LongType int64(final LongType out,
		final T in)
	{
		final LongType result =
			(LongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToInt64.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint64.class)
	public Object uint64(final Object... args) {
		return ops().run(Ops.Convert.Uint64.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint64.class)
	public <C extends ComplexType<C>> UnsignedLongType uint64(final C in) {
		final UnsignedLongType result =
			(UnsignedLongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint64.class)
	public <C extends ComplexType<C>> UnsignedLongType uint64(
		final UnsignedLongType out, final C in)
	{
		final UnsignedLongType result =
			(UnsignedLongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint64.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint64.class)
	public <T extends IntegerType<T>> UnsignedLongType uint64(final T in) {
		final UnsignedLongType result =
			(UnsignedLongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint64.class)
	public <T extends IntegerType<T>> UnsignedLongType uint64(
		final UnsignedLongType out, final T in)
	{
		final UnsignedLongType result =
			(UnsignedLongType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint64.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Uint128.class)
	public Object uint128(final Object... args) {
		return ops().run(Ops.Convert.Uint128.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint128.class)
	public <T extends IntegerType<T>> Unsigned128BitType uint128(final T in) {
		final Unsigned128BitType result =
			(Unsigned128BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint128.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.IntegerToUint128.class)
	public <T extends IntegerType<T>> Unsigned128BitType uint128(
		final Unsigned128BitType out, final T in)
	{
		final Unsigned128BitType result =
			(Unsigned128BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.IntegerToUint128.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint128.class)
	public <C extends ComplexType<C>> Unsigned128BitType uint128(final C in) {
		final Unsigned128BitType result =
			(Unsigned128BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint128.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToUint128.class)
	public <C extends ComplexType<C>> Unsigned128BitType uint128(
		final Unsigned128BitType out, final C in)
	{
		final Unsigned128BitType result =
			(Unsigned128BitType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToUint128.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Float32.class)
	public Object float32(final Object... args) {
		return ops().run(Ops.Convert.Float32.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToFloat32.class)
	public <C extends ComplexType<C>> FloatType float32(final C in) {
		final FloatType result =
			(FloatType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToFloat32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToFloat32.class)
	public <C extends ComplexType<C>> FloatType float32(final FloatType out,
		final C in)
	{
		final FloatType result =
			(FloatType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToFloat32.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Cfloat32.class)
	public Object cfloat32(final Object... args) {
		return ops().run(Ops.Convert.Cfloat32.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToCfloat32.class)
	public <C extends ComplexType<C>> ComplexFloatType cfloat32(final C in) {
		final ComplexFloatType result =
			(ComplexFloatType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToCfloat32.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToCfloat32.class)
	public <C extends ComplexType<C>> ComplexFloatType cfloat32(
		final ComplexFloatType out, final C in)
	{
		final ComplexFloatType result =
			(ComplexFloatType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToCfloat32.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Float64.class)
	public Object float64(final Object... args) {
		return ops().run(Ops.Convert.Float64.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToFloat64.class)
	public <C extends ComplexType<C>> DoubleType float64(final C in) {
		final DoubleType result =
			(DoubleType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToFloat64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToFloat64.class)
	public <C extends ComplexType<C>> DoubleType float64(final DoubleType out,
		final C in)
	{
		final DoubleType result =
			(DoubleType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToFloat64.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Cfloat64.class)
	public Object cfloat64(final Object... args) {
		return ops().run(Ops.Convert.Cfloat64.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToCfloat64.class)
	public <C extends ComplexType<C>> ComplexDoubleType cfloat64(final C in) {
		final ComplexDoubleType result =
			(ComplexDoubleType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToCfloat64.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.convert.ConvertTypes.ComplexToCfloat64.class)
	public <C extends ComplexType<C>> ComplexDoubleType cfloat64(
		final ComplexDoubleType out, final C in)
	{
		final ComplexDoubleType result =
			(ComplexDoubleType) ops().run(
				net.imagej.ops.convert.ConvertTypes.ComplexToCfloat64.class, out, in);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "convert";
	}

}
