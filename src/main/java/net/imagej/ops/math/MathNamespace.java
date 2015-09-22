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

package net.imagej.ops.math;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.IterableRealInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The math namespace contains arithmetic operations.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class MathNamespace extends AbstractNamespace {

	// -- Math namespace ops --

	@OpMethod(op = net.imagej.ops.Ops.Math.Abs.class)
	public Object abs(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Abs.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAbs.class)
	public int abs(final int a) {
		final int result =
			(Integer) ops()
				.run(net.imagej.ops.math.PrimitiveMath.IntegerAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAbs.class)
	public long abs(final long a) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatAbs.class)
	public float abs(final float a) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleAbs.class)
	public double abs(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Abs.class)
	public <I extends RealType<I>, O extends RealType<O>> O abs(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Abs.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Add.class)
	public Object add(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Add.class, args);
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.AddConstantToArrayByteImageP.class,
		net.imagej.ops.math.ConstantToArrayImage.AddConstantToArrayByteImage.class })
	public ArrayImg<ByteType, ByteArray> add(
		final ArrayImg<ByteType, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<ByteType, ByteArray> result =
			(ArrayImg<ByteType, ByteArray>) ops().run(Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(
		ops = {
			net.imagej.ops.math.ConstantToArrayImageP.AddConstantToArrayDoubleImageP.class,
			net.imagej.ops.math.ConstantToArrayImage.AddConstantToArrayDoubleImage.class })
	public
		ArrayImg<DoubleType, DoubleArray> add(
			final ArrayImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(Ops.Math.Add.NAME, image,
				value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAdd.class)
	public int add(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerAdd.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAdd.class)
	public long add(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatAdd.class)
	public float add(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleAdd.class)
	public double add(final double a, final double b) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleAdd.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Add.class)
	public <I extends RealType<I>, O extends RealType<O>> RealType<O> add(
		final RealType<O> out, final RealType<I> in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final RealType<O> result =
			(RealType<O>) ops().run(net.imagej.ops.math.RealMath.Add.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Add.class)
	public <T extends NumericType<T>> T add(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Add.class, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Add.class)
	public <T extends NumericType<T>> T add(final T out, final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Add.class, out,
				in, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.AddRandomAccessibleIntervalToIterableInterval.class)
	public
		<T extends NumericType<T>> IterableInterval<T> add(
			final IterableInterval<T> a, final RandomAccessibleInterval<T> b)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops()
				.run(
					net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.AddRandomAccessibleIntervalToIterableInterval.class,
					a, b);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.math.ConstantToPlanarImage.AddConstantToPlanarDoubleImage.class)
	public PlanarImg<DoubleType, DoubleArray> add(
		final PlanarImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(
				net.imagej.ops.math.ConstantToPlanarImage.AddConstantToPlanarDoubleImage.class,
				image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageInPlace.AddConstantToImageInPlace.class)
	public <T extends NumericType<T>> IterableRealInterval<T> add(
		final IterableRealInterval<T> image, final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableRealInterval<T> result =
			(IterableRealInterval<T>) ops().run(
				net.imagej.ops.math.ConstantToImageInPlace.AddConstantToImageInPlace.class, image,
				value);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.math.ConstantToImageFunctional.AddConstantToImageFunctional.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> add(
		final RandomAccessibleInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.math.ConstantToImageFunctional.AddConstantToImageFunctional.class, out,
				in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.AddIterableIntervalToImg.class)
	public <T extends NumericType<T>> Img<T> add(final Img<T> in,
		final IterableInterval<T> ii)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.math.IterableIntervalToImg.AddIterableIntervalToImg.class, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.AddIterableIntervalToImg.class)
	public <T extends NumericType<T>> Img<T> add(final Img<T> out,
		final Img<T> in, final IterableInterval<T> ii)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.math.IterableIntervalToImg.AddIterableIntervalToImg.class, out, in,
				ii);
		return result;
	}
	
	@OpMethod(ops = {
			net.imagej.ops.math.ConstantToArrayImageP.DivideArrayByteImageByConstantP.class, 
			net.imagej.ops.math.ConstantToArrayImage.DivideArrayByteImageByConstant.class})
	public ArrayImg<ByteType, ByteType> divide(final ArrayImg<ByteType, ByteType> image, final byte value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<ByteType, ByteType> result =
			(ArrayImg<ByteType, ByteType>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.DivideArrayByteImageByConstantP.class, image, value);
		return result;
	}

	@OpMethod(ops = {net.imagej.ops.math.ConstantToArrayImageP.DivideArrayDoubleImageByConstantP.class, 
			net.imagej.ops.math.ConstantToArrayImage.DivideArrayDoubleImageByConstant.class})
	public ArrayImg<DoubleType, DoubleType> divide(final ArrayImg<DoubleType, DoubleType> image, final double value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleType> result =
			(ArrayImg<DoubleType, DoubleType>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.DivideArrayDoubleImageByConstantP.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.DivideImgByInterableInterval.class)
	public <T extends NumericType<T>> Img<T> divide(final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.DivideImgByInterableInterval.class, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.DivideImgByInterableInterval.class)
	public <T extends NumericType<T>> Img<T> divide(final Img<T> out, final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.DivideImgByInterableInterval.class, out, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageInPlace.DivideImageByConstantInPlace.class)
	public <T extends NumericType<T>> IterableRealInterval<T> divide(final IterableRealInterval<T> image, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final IterableRealInterval<T> result =
			(IterableRealInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageInPlace.DivideImageByConstantInPlace.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.DividePlanarDoubleImageByConstant.class)
	public PlanarImg<DoubleType, DoubleArray> divide(final PlanarImg<DoubleType, DoubleArray> image, final double value) {
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(net.imagej.ops.math.ConstantToPlanarImage.DividePlanarDoubleImageByConstant.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.DivideIterableIntervalByRandomAccessibleInterval.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(final IterableInterval<T> a, final RandomAccessibleInterval<T> b) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.DivideIterableIntervalByRandomAccessibleInterval.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageFunctional.DivideImageByConstantFunctional.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> divide(final RandomAccessibleInterval<T> out, final IterableInterval<T> in, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageFunctional.DivideImageByConstantFunctional.class, out, in, value);
		return result;
	}
	
	@OpMethod(ops = {
			net.imagej.ops.math.ConstantToArrayImageP.MultiplyArrayByteImageByConstantP.class, 
			net.imagej.ops.math.ConstantToArrayImage.MultiplyArrayByteImageByConstant.class})
	public ArrayImg<ByteType, ByteArray> multiply(final ArrayImg<ByteType, ByteArray> image, final byte value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<ByteType, ByteArray> result =
			(ArrayImg<ByteType, ByteArray>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.MultiplyArrayByteImageByConstantP.class, image, value);
		return result;
	}

	@OpMethod(ops = {
			net.imagej.ops.math.ConstantToArrayImageP.MultiplyArrayDoubleImageByConstantP.class, 
			net.imagej.ops.math.ConstantToArrayImage.MultiplyArrayDoubleImageByConstant.class})
	public ArrayImg<DoubleType, DoubleArray> multiply(final ArrayImg<DoubleType, DoubleArray> image, final double value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.MultiplyArrayDoubleImageByConstantP.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.MultiplyImgByInterableInterval.class)
	public <T extends NumericType<T>> Img<T> multiply(final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.MultiplyImgByInterableInterval.class, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.MultiplyImgByInterableInterval.class)
	public <T extends NumericType<T>> Img<T> multiply(final Img<T> out, final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.MultiplyImgByInterableInterval.class, out, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageInPlace.MultiplyImageByConstantInPlace.class)
	public <T extends NumericType<T>> IterableRealInterval<T> multiply(final IterableRealInterval<T> image, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final IterableRealInterval<T> result =
			(IterableRealInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageInPlace.MultiplyImageByConstantInPlace.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.MultiplyPlanarDoubleImageByConstant.class)
	public PlanarImg<DoubleType, DoubleArray> multiply(final PlanarImg<DoubleType, DoubleArray> image, final double value) {
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(net.imagej.ops.math.ConstantToPlanarImage.MultiplyPlanarDoubleImageByConstant.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.MultiplyIterableIntervalByRandomAccessibleInterval.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(final IterableInterval<T> a, final RandomAccessibleInterval<T> b) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.MultiplyIterableIntervalByRandomAccessibleInterval.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageFunctional.MultiplyImageByConstantFunctional.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> multiply(final RandomAccessibleInterval<T> out, final IterableInterval<T> in, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageFunctional.MultiplyImageByConstantFunctional.class, out, in, value);
		return result;
	}
	
	@OpMethod(ops = {
			net.imagej.ops.math.ConstantToArrayImageP.SubtractConstantFromArrayByteImageP.class, 
			net.imagej.ops.math.ConstantToArrayImage.SubtractConstantFromArrayByteImage.class})
	public ArrayImg<ByteType, ByteArray> subtract(final ArrayImg<ByteType, ByteArray> image, final byte value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<ByteType, ByteArray> result =
			(ArrayImg<ByteType, ByteArray>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.SubtractConstantFromArrayByteImageP.class, image, value);
		return result;
	}

	@OpMethod(ops = {
			net.imagej.ops.math.ConstantToArrayImageP.SubtractConstantFromArrayDoubleImageP.class, 
			net.imagej.ops.math.ConstantToArrayImage.SubtractConstantFromArrayDoubleImage.class})
	public ArrayImg<DoubleType, DoubleArray> subtract(final ArrayImg<DoubleType, DoubleArray> image, final double value) {
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(net.imagej.ops.math.ConstantToArrayImageP.SubtractConstantFromArrayDoubleImageP.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.SubtractIterableIntervalFromImg.class)
	public <T extends NumericType<T>> Img<T> subtract(final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.SubtractIterableIntervalFromImg.class, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IterableIntervalToImg.SubtractIterableIntervalFromImg.class)
	public <T extends NumericType<T>> Img<T> subtract(final Img<T> out, final Img<T> in, final IterableInterval<T> ii) {
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.math.IterableIntervalToImg.SubtractIterableIntervalFromImg.class, out, in, ii);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageInPlace.SubtractConstantFromImageInPlace.class)
	public <T extends NumericType<T>> IterableRealInterval<T> subtract(final IterableRealInterval<T> image, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final IterableRealInterval<T> result =
			(IterableRealInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageInPlace.SubtractConstantFromImageInPlace.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.SubtractConstantFromPlanarDoubleImage.class)
	public PlanarImg<DoubleType, DoubleArray> subtract(final PlanarImg<DoubleType, DoubleArray> image, final double value) {
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(net.imagej.ops.math.ConstantToPlanarImage.SubtractConstantFromPlanarDoubleImage.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.SubtractRandomAccessibleIntervalFromIterableInterval.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(final IterableInterval<T> a, final RandomAccessibleInterval<T> b) {
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result =
			(IterableInterval<T>) ops().run(net.imagej.ops.math.RandomAccessibleIntervalToIterableInterval.SubtractRandomAccessibleIntervalFromIterableInterval.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToImageFunctional.SubtractConstantFromImageFunctional.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> subtract(final RandomAccessibleInterval<T> out, final IterableInterval<T> in, final NumericType<T> value) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.math.ConstantToImageFunctional.SubtractConstantFromImageFunctional.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.And.class)
	public Object and(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.And.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAnd.class)
	public int and(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerAnd.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAnd.class)
	public long and(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongAnd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.AndConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O and(final O out,
		final I in, final long constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.AndConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccos.class)
	public Object arccos(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccos.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArccos.class)
	public double arccos(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleArccos.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccos.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccos(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccos.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccosh.class)
	public Object arccosh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccosh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccosh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccosh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccosh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccot.class)
	public Object arccot(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccot.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccot.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccot.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccoth.class)
	public Object arccoth(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccoth.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccoth.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccoth(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccoth.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccsc.class)
	public Object arccsc(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccsc.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccsc.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccsc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccsc.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arccsch.class)
	public Object arccsch(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arccsch.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arccsch.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccsch(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arccsch.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arcsec.class)
	public Object arcsec(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arcsec.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arcsec.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsec(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arcsec.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arcsech.class)
	public Object arcsech(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arcsech.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arcsech.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsech(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arcsech.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arcsin.class)
	public Object arcsin(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arcsin.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArcsin.class)
	public double arcsin(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleArcsin.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arcsin.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsin(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arcsin.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arcsinh.class)
	public Object arcsinh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arcsinh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arcsinh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsinh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arcsinh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arctan.class)
	public Object arctan(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arctan.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArctan.class)
	public double arctan(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleArctan.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arctan.class)
	public <I extends RealType<I>, O extends RealType<O>> O arctan(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arctan.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Arctanh.class)
	public Object arctanh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Arctanh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Arctanh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arctanh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Arctanh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Ceil.class)
	public Object ceil(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Ceil.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCeil.class)
	public double ceil(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleCeil.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Ceil.class)
	public <I extends RealType<I>, O extends RealType<O>> O ceil(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Ceil.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Complement.class)
	public Object complement(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Complement.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerComplement.class)
	public int complement(final int a) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerComplement.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongComplement.class)
	public long complement(final long a) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongComplement.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Copy.class)
	public Object copy(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Copy.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Copy.class)
	public <I extends RealType<I>, O extends RealType<O>> O copy(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Copy.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Cos.class)
	public Object cos(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Cos.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCos.class)
	public double cos(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleCos.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Cos.class)
	public <I extends RealType<I>, O extends RealType<O>> O cos(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Cos.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Cosh.class)
	public Object cosh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Cosh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCosh.class)
	public double cosh(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleCosh.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Cosh.class)
	public <I extends RealType<I>, O extends RealType<O>> O cosh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Cosh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Cot.class)
	public Object cot(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Cot.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Cot.class)
	public <I extends RealType<I>, O extends RealType<O>> O cot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Cot.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Coth.class)
	public Object coth(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Coth.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Coth.class)
	public <I extends RealType<I>, O extends RealType<O>> O coth(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Coth.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Csc.class)
	public Object csc(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Csc.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Csc.class)
	public <I extends RealType<I>, O extends RealType<O>> O csc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Csc.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Csch.class)
	public Object csch(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Csch.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Csch.class)
	public <I extends RealType<I>, O extends RealType<O>> O csch(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Csch.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.CubeRoot.class)
	public Object cubeRoot(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.CubeRoot.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCubeRoot.class)
	public double cubeRoot(final double a) {
		final double result =
			(Double) ops().run(
				net.imagej.ops.math.PrimitiveMath.DoubleCubeRoot.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.CubeRoot.class)
	public <I extends RealType<I>, O extends RealType<O>> O cubeRoot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.CubeRoot.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Divide.class)
	public Object divide(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Divide.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerDivide.class)
	public int divide(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongDivide.class)
	public long divide(final long a, final long b) {
		final long result =
			(Long) ops()
				.run(net.imagej.ops.math.PrimitiveMath.LongDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatDivide.class)
	public float divide(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatDivide.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleDivide.class)
	public double divide(final double a, final double b) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleDivide.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Divide.class)
	public <I extends RealType<I>, O extends RealType<O>> O divide(final O out,
		final I in, final double constant, final double dbzVal)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Divide.class, out, in,
				constant, dbzVal);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Divide.class)
	public <T extends NumericType<T>> T divide(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Divide.class, in,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Divide.class)
	public <T extends NumericType<T>> T
		divide(final T out, final T in, final T b)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Divide.class,
				out, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Exp.class)
	public Object exp(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Exp.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleExp.class)
	public double exp(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleExp.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Exp.class)
	public <I extends RealType<I>, O extends RealType<O>> O exp(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Exp.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.ExpMinusOne.class)
	public Object expMinusOne(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.ExpMinusOne.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.ExpMinusOne.class)
	public <I extends RealType<I>, O extends RealType<O>> O expMinusOne(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.ExpMinusOne.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Floor.class)
	public Object floor(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Floor.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleFloor.class)
	public double floor(final double a) {
		final double result =
			(Double) ops()
				.run(net.imagej.ops.math.PrimitiveMath.DoubleFloor.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Floor.class)
	public <I extends RealType<I>, O extends RealType<O>> O floor(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Floor.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Gamma.class)
	public Object gamma(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Gamma.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.GammaConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O gamma(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.GammaConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Invert.class)
	public Object invert(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Invert.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Invert.class)
	public <I extends RealType<I>, O extends RealType<O>> O invert(final O out,
		final I in, final double specifiedMin, final double specifiedMax)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Invert.class, out, in,
				specifiedMin, specifiedMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.LeftShift.class)
	public Object leftShift(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.LeftShift.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerLeftShift.class)
	public int leftShift(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerLeftShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongLeftShift.class)
	public long leftShift(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongLeftShift.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Log.class)
	public Object log(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Log.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLog.class)
	public double log(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleLog.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Log.class)
	public <I extends RealType<I>, O extends RealType<O>> O log(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Log.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Log2.class)
	public Object log2(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Log2.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Log2.class)
	public <I extends RealType<I>, O extends RealType<O>> O log2(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Log2.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Log10.class)
	public Object log10(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Log10.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLog10.class)
	public double log10(final double a) {
		final double result =
			(Double) ops()
				.run(net.imagej.ops.math.PrimitiveMath.DoubleLog10.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Log10.class)
	public <I extends RealType<I>, O extends RealType<O>> O log10(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Log10.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.LogOnePlusX.class)
	public Object logOnePlusX(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.LogOnePlusX.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLogOnePlusX.class)
	public double logOnePlusX(final double a) {
		final double result =
			(Double) ops().run(
				net.imagej.ops.math.PrimitiveMath.DoubleLogOnePlusX.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.LogOnePlusX.class)
	public <I extends RealType<I>, O extends RealType<O>> O logOnePlusX(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.LogOnePlusX.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Max.class)
	public Object max(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Max.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMax.class)
	public int max(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerMax.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMax.class)
	public long max(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMax.class)
	public float max(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMax.class)
	public double max(final double a, final double b) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleMax.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.MaxConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O max(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.MaxConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Min.class)
	public Object min(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Min.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMin.class)
	public int min(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerMin.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMin.class)
	public long min(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMin.class)
	public float min(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMin.class)
	public double min(final double a, final double b) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleMin.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.MinConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O min(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.MinConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Multiply.class)
	public Object multiply(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Multiply.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMultiply.class)
	public int multiply(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMultiply.class)
	public long multiply(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongMultiply.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMultiply.class)
	public float multiply(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatMultiply.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMultiply.class)
	public double multiply(final double a, final double b) {
		final double result =
			(Double) ops().run(
				net.imagej.ops.math.PrimitiveMath.DoubleMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Multiply.class)
	public <I extends RealType<I>, O extends RealType<O>> O multiply(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Multiply.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class)
	public <T extends NumericType<T>> T multiply(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class,
				in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class)
	public <T extends NumericType<T>> T multiply(final T out, final T in,
		final T b)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class,
				out, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.NearestInt.class)
	public Object nearestInt(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.NearestInt.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.NearestInt.class)
	public <I extends RealType<I>, O extends RealType<O>> O nearestInt(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.NearestInt.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Negate.class)
	public Object negate(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Negate.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerNegate.class)
	public int negate(final int a) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongNegate.class)
	public long negate(final long a) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatNegate.class)
	public float negate(final float a) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleNegate.class)
	public double negate(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleNegate.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Negate.class)
	public <I extends RealType<I>, O extends RealType<O>> O negate(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Negate.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Or.class)
	public Object or(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Or.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerOr.class)
	public int or(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerOr.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongOr.class)
	public long or(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongOr.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.OrConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O or(final O out,
		final I in, final long constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.OrConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Power.class)
	public Object power(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Power.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoublePower.class)
	public double power(final double a, final double b) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoublePower.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.PowerConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O power(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.PowerConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.RandomGaussian.class)
	public Object randomGaussian(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.RandomGaussian.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.RandomGaussian.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomGaussian(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.RandomGaussian.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.RandomGaussian.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomGaussian(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.RandomGaussian.class, out, in,
				seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.RandomUniform.class)
	public Object randomUniform(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.RandomUniform.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.RandomUniform.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomUniform(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.RandomUniform.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.RandomUniform.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomUniform(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.RandomUniform.class, out, in,
				seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Reciprocal.class)
	public Object reciprocal(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Reciprocal.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Reciprocal.class)
	public <I extends RealType<I>, O extends RealType<O>> O reciprocal(
		final O out, final I in, final double dbzVal)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Reciprocal.class, out, in,
				dbzVal);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Remainder.class)
	public Object remainder(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Remainder.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerRemainder.class)
	public int remainder(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongRemainder.class)
	public long remainder(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongRemainder.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatRemainder.class)
	public float remainder(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatRemainder.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleRemainder.class)
	public double remainder(final double a, final double b) {
		final double result =
			(Double) ops().run(
				net.imagej.ops.math.PrimitiveMath.DoubleRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.RightShift.class)
	public Object rightShift(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.RightShift.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerRightShift.class)
	public int rightShift(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongRightShift.class)
	public long rightShift(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongRightShift.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Round.class)
	public Object round(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Round.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatRound.class)
	public float round(final float a) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatRound.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleRound.class)
	public double round(final double a) {
		final double result =
			(Double) ops()
				.run(net.imagej.ops.math.PrimitiveMath.DoubleRound.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Round.class)
	public <I extends RealType<I>, O extends RealType<O>> O round(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Round.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sec.class)
	public Object sec(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sec.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sec.class)
	public <I extends RealType<I>, O extends RealType<O>> O sec(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sec.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sech.class)
	public Object sech(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sech.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sech.class)
	public <I extends RealType<I>, O extends RealType<O>> O sech(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sech.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Signum.class)
	public Object signum(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Signum.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatSignum.class)
	public float signum(final float a) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatSignum.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSignum.class)
	public double signum(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleSignum.class,
				a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Signum.class)
	public <I extends RealType<I>, O extends RealType<O>> O signum(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Signum.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sin.class)
	public Object sin(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sin.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSin.class)
	public double sin(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleSin.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sin.class)
	public <I extends RealType<I>, O extends RealType<O>> O sin(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sin.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sinc.class)
	public Object sinc(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sinc.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sinc.class)
	public <I extends RealType<I>, O extends RealType<O>> O sinc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sinc.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.SincPi.class)
	public Object sincPi(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.SincPi.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.SincPi.class)
	public <I extends RealType<I>, O extends RealType<O>> O sincPi(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.SincPi.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sinh.class)
	public Object sinh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sinh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSinh.class)
	public double sinh(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleSinh.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sinh.class)
	public <I extends RealType<I>, O extends RealType<O>> O sinh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sinh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sqr.class)
	public Object sqr(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sqr.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sqr.class)
	public <I extends RealType<I>, O extends RealType<O>> O sqr(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sqr.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Sqrt.class)
	public Object sqrt(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Sqrt.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSqrt.class)
	public double sqrt(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleSqrt.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Sqrt.class)
	public <I extends RealType<I>, O extends RealType<O>> O sqrt(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Sqrt.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Step.class)
	public Object step(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Step.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Step.class)
	public <I extends RealType<I>, O extends RealType<O>> O step(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Step.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Subtract.class)
	public Object subtract(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Subtract.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerSubtract.class)
	public int subtract(final int a, final int b) {
		final int result =
			(Integer) ops().run(
				net.imagej.ops.math.PrimitiveMath.IntegerSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongSubtract.class)
	public long subtract(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongSubtract.class, a,
				b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatSubtract.class)
	public float subtract(final float a, final float b) {
		final float result =
			(Float) ops().run(net.imagej.ops.math.PrimitiveMath.FloatSubtract.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSubtract.class)
	public double subtract(final double a, final double b) {
		final double result =
			(Double) ops().run(
				net.imagej.ops.math.PrimitiveMath.DoubleSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Subtract.class)
	public <I extends RealType<I>, O extends RealType<O>> O subtract(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Subtract.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class)
	public <T extends NumericType<T>> T subtract(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class,
				in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class)
	public <T extends NumericType<T>> T subtract(final T out, final T in,
		final T b)
	{
		@SuppressWarnings("unchecked")
		final T result =
			(T) ops().run(net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class,
				out, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Tan.class)
	public Object tan(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Tan.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleTan.class)
	public double tan(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleTan.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Tan.class)
	public <I extends RealType<I>, O extends RealType<O>> O tan(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Tan.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Tanh.class)
	public Object tanh(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Tanh.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleTanh.class)
	public double tanh(final double a) {
		final double result =
			(Double) ops().run(net.imagej.ops.math.PrimitiveMath.DoubleTanh.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Tanh.class)
	public <I extends RealType<I>, O extends RealType<O>> O tanh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Tanh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Ulp.class)
	public Object ulp(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Ulp.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Ulp.class)
	public <I extends RealType<I>, O extends RealType<O>> O ulp(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Ulp.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.UnsignedRightShift.class)
	public Object unsignedRightShift(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.UnsignedRightShift.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.math.PrimitiveMath.IntegerUnsignedRightShift.class)
	public int unsignedRightShift(final int a, final int b) {
		final int result =
			(Integer) ops()
				.run(net.imagej.ops.math.PrimitiveMath.IntegerUnsignedRightShift.class,
					a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongUnsignedRightShift.class)
	public
		long unsignedRightShift(final long a, final long b) {
		final long result =
			(Long) ops().run(
				net.imagej.ops.math.PrimitiveMath.LongUnsignedRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Xor.class)
	public Object xor(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Xor.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerXor.class)
	public int xor(final int a, final int b) {
		final int result =
			(Integer) ops().run(net.imagej.ops.math.PrimitiveMath.IntegerXor.class,
				a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongXor.class)
	public long xor(final long a, final long b) {
		final long result =
			(Long) ops().run(net.imagej.ops.math.PrimitiveMath.LongXor.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.XorConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O xor(final O out,
		final I in, final long constant)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.XorConstant.class, out, in,
				constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.Ops.Math.Zero.class)
	public Object zero(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Math.Zero.class, args);
	}

	@OpMethod(op = net.imagej.ops.math.RealMath.Zero.class)
	public <I extends RealType<I>, O extends RealType<O>> O zero(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.math.RealMath.Zero.class, out, in);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "math";
	}

}
