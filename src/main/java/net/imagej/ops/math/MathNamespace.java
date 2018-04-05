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

package net.imagej.ops.math;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.ArrayDataAccess;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.GenericByteType;
import net.imglib2.type.numeric.integer.GenericIntType;
import net.imglib2.type.numeric.integer.GenericShortType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.plugin.Plugin;

/**
 * The math namespace contains arithmetic operations.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class MathNamespace extends AbstractNamespace {

	// -- Math namespace ops --

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleAbs.class)
	public double abs(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatAbs.class)
	public float abs(final float a) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAbs.class)
	public int abs(final int a) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAbs.class)
	public long abs(final long a) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongAbs.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Abs.class)
	public <I extends RealType<I>, O extends RealType<O>> O abs(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Abs.class, out, in);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyByte.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyByte.class,
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyUnsignedByte.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyUnsignedByte.class })
	public <B extends GenericByteType<B>> ArrayImg<B, ByteArray> multiply(
		final ArrayImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<B, ByteArray> result = (ArrayImg<B, ByteArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyDouble.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyDouble.class })
	public ArrayImg<DoubleType, DoubleArray> multiply(
		final ArrayImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(Ops.Math.Multiply.NAME,
				image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyFloat.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyFloat.class })
	public ArrayImg<FloatType, FloatArray> multiply(
		final ArrayImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<FloatType, FloatArray> result =
			(ArrayImg<FloatType, FloatArray>) ops().run(Ops.Math.Multiply.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.MultiplyInt.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyInt.class,
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyUnsignedInt.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyUnsignedInt.class })
	public <I extends GenericIntType<I>> ArrayImg<I, IntArray> multiply(
		final ArrayImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<I, IntArray> result = (ArrayImg<I, IntArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyLong.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyLong.class,
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyUnsignedLong.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyUnsignedLong.class })
	public <N extends NativeType<N>> ArrayImg<N, LongArray> multiply(
		final ArrayImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<N, LongArray> result = (ArrayImg<N, LongArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyShort.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyShort.class,
		net.imagej.ops.math.ConstantToArrayImageP.MultiplyUnsignedShort.class,
		net.imagej.ops.math.ConstantToArrayImage.MultiplyUnsignedShort.class })
	public <S extends GenericShortType<S>> ArrayImg<S, ShortArray> multiply(
		final ArrayImg<S, ShortArray> image, final short value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<S, ShortArray> result = (ArrayImg<S, ShortArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleAdd.class)
	public double add(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatAdd.class)
	public float add(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAdd.class)
	public int add(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> in, final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Add.class)
	public <T extends NumericType<T>> IterableInterval<T> add(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Add.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAdd.class)
	public long add(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongAdd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Add.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		add(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Add.class, out, in1,
			in2);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.AddByte.class,
		net.imagej.ops.math.ConstantToPlanarImage.AddUnsignedByte.class })
	public <B extends GenericByteType<B>> PlanarImg<B, ByteArray> add(
		final PlanarImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<B, ByteArray> result = (PlanarImg<B, ByteArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.AddDouble.class)
	public PlanarImg<DoubleType, DoubleArray> add(
		final PlanarImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(
				net.imagej.ops.Ops.Math.Add.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.AddFloat.class)
	public PlanarImg<FloatType, FloatArray> add(
		final PlanarImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<FloatType, FloatArray> result =
			(PlanarImg<FloatType, FloatArray>) ops().run(
				net.imagej.ops.Ops.Math.Add.class, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.AddInt.class,
		net.imagej.ops.math.ConstantToPlanarImage.AddUnsignedInt.class })
	public <I extends GenericIntType<I>> PlanarImg<I, IntArray> add(
		final PlanarImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<I, IntArray> result = (PlanarImg<I, IntArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.AddLong.class,
		net.imagej.ops.math.ConstantToPlanarImage.AddUnsignedLong.class })
	public <N extends NativeType<N>> PlanarImg<N, LongArray> add(
		final PlanarImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<N, LongArray> result = (PlanarImg<N, LongArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.AddShort.class,
		net.imagej.ops.math.ConstantToPlanarImage.AddUnsignedShort.class })
	public <S extends GenericShortType<S>> PlanarImg<S, ShortArray> add(
		final PlanarImg<S, ShortArray> arg, final short value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<S, ShortArray> result = (PlanarImg<S, ShortArray>) ops()
			.run(Ops.Math.Add.NAME, arg, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputRAI.Add.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> add(
		final RandomAccessibleInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.Ops.Math.Add.class,
				out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Add.class)
	public <T extends NumericType<T>> T add(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Add.class, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Add.class)
	public <T extends NumericType<T>> T add(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Add.class, out, in1,
			in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerAnd.class)
	public int and(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerAnd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongAnd.class)
	public long and(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongAnd.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.And.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		and(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.And.class, out, in1,
			in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArccos.class)
	public double arccos(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleArccos.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccos.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccos(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccos.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccosh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccosh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccosh.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccot.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccot.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccoth.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccoth(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccoth.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccsc.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccsc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccsc.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arccsch.class)
	public <I extends RealType<I>, O extends RealType<O>> O arccsch(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arccsch.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arcsec.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsec(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arcsec.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arcsech.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsech(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arcsech.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArcsin.class)
	public double arcsin(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleArcsin.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arcsin.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsin(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arcsin.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arcsinh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arcsinh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arcsinh.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleArctan.class)
	public double arctan(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleArctan.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arctan.class)
	public <I extends RealType<I>, O extends RealType<O>> O arctan(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arctan.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Arctanh.class)
	public <I extends RealType<I>, O extends RealType<O>> O arctanh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Arctanh.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NullaryNumericTypeMath.Assign.class)
	public <T extends Type<T>> T assign(final T out, final T constant) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(
			net.imagej.ops.math.NullaryNumericTypeMath.Assign.class, out, constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCeil.class)
	public double ceil(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleCeil.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Ceil.class)
	public <I extends RealType<I>, O extends RealType<O>> O ceil(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Ceil.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerComplement.class)
	public int complement(final int a) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerComplement.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongComplement.class)
	public long complement(final long a) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongComplement.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCos.class)
	public double cos(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleCos.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Cos.class)
	public <I extends RealType<I>, O extends RealType<O>> O cos(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Cos.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCosh.class)
	public double cosh(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleCosh.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Cosh.class)
	public <I extends RealType<I>, O extends RealType<O>> O cosh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Cosh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Cot.class)
	public <I extends RealType<I>, O extends RealType<O>> O cot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Cot.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Coth.class)
	public <I extends RealType<I>, O extends RealType<O>> O coth(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Coth.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Csc.class)
	public <I extends RealType<I>, O extends RealType<O>> O csc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Csc.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Csch.class)
	public <I extends RealType<I>, O extends RealType<O>> O csch(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Csch.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleCubeRoot.class)
	public double cubeRoot(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleCubeRoot.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.CubeRoot.class)
	public <I extends RealType<I>, O extends RealType<O>> O cubeRoot(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.CubeRoot.class, out,
			in);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.DivideByte.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideByte.class,
		net.imagej.ops.math.ConstantToArrayImageP.DivideUnsignedByte.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideUnsignedByte.class })
	public <B extends GenericByteType<B>> ArrayImg<B, ByteArray> divide(
		final ArrayImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<B, ByteArray> result = (ArrayImg<B, ByteArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.DivideDouble.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideDouble.class })
	public ArrayImg<DoubleType, DoubleArray> divide(
		final ArrayImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(Ops.Math.Divide.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.DivideFloat.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideFloat.class })
	public ArrayImg<FloatType, FloatArray> divide(
		final ArrayImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<FloatType, FloatArray> result =
			(ArrayImg<FloatType, FloatArray>) ops().run(Ops.Math.Divide.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.DivideInt.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideInt.class,
		net.imagej.ops.math.ConstantToArrayImageP.DivideUnsignedInt.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideUnsignedInt.class })
	public <I extends GenericIntType<I>> ArrayImg<I, IntArray> divide(
		final ArrayImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<I, IntArray> result = (ArrayImg<I, IntArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.DivideLong.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideLong.class,
		net.imagej.ops.math.ConstantToArrayImageP.DivideUnsignedLong.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideUnsignedLong.class })
	public <N extends NativeType<N>> ArrayImg<N, LongArray> divide(
		final ArrayImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<N, LongArray> result = (ArrayImg<N, LongArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.DivideShort.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideShort.class,
		net.imagej.ops.math.ConstantToArrayImageP.DivideUnsignedShort.class,
		net.imagej.ops.math.ConstantToArrayImage.DivideUnsignedShort.class })
	public <S extends GenericShortType<S>> ArrayImg<S, ShortArray> divide(
		final ArrayImg<S, ShortArray> image, final short value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<S, ShortArray> result = (ArrayImg<S, ShortArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleDivide.class)
	public double divide(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatDivide.class)
	public float divide(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerDivide.class)
	public int divide(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> in, final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Divide.class)
	public <T extends NumericType<T>> IterableInterval<T> divide(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Divide.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongDivide.class)
	public long divide(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongDivide.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Divide.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		divide(final O out, final I1 in1, final I2 in2, final double dbzVal)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Divide.class, out,
			in1, in2, dbzVal);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.DivideByte.class,
		net.imagej.ops.math.ConstantToPlanarImage.DivideUnsignedByte.class })
	public <B extends GenericByteType<B>> PlanarImg<B, ByteArray> divide(
		final PlanarImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<B, ByteArray> result = (PlanarImg<B, ByteArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.DivideDouble.class)
	public PlanarImg<DoubleType, DoubleArray> divide(
		final PlanarImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(
				net.imagej.ops.Ops.Math.Divide.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.DivideFloat.class)
	public PlanarImg<FloatType, FloatArray> divide(
		final PlanarImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<FloatType, FloatArray> result =
			(PlanarImg<FloatType, FloatArray>) ops().run(
				net.imagej.ops.Ops.Math.Divide.class, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.DivideInt.class,
		net.imagej.ops.math.ConstantToPlanarImage.DivideUnsignedInt.class })
	public <I extends GenericIntType<I>> PlanarImg<I, IntArray> divide(
		final PlanarImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<I, IntArray> result = (PlanarImg<I, IntArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.DivideLong.class,
		net.imagej.ops.math.ConstantToPlanarImage.DivideUnsignedLong.class })
	public <N extends NativeType<N>> PlanarImg<N, LongArray> divide(
		final PlanarImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<N, LongArray> result = (PlanarImg<N, LongArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.DivideShort.class,
		net.imagej.ops.math.ConstantToPlanarImage.DivideUnsignedShort.class })
	public <S extends GenericShortType<S>> PlanarImg<S, ShortArray> divide(
		final PlanarImg<S, ShortArray> arg, final short value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<S, ShortArray> result = (PlanarImg<S, ShortArray>) ops()
			.run(Ops.Math.Divide.NAME, arg, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputRAI.Divide.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> divide(
		final RandomAccessibleInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.Ops.Math.Divide.class, out, in, value);
		return result;
	}

	@OpMethod(ops = net.imagej.ops.math.NumericTypeBinaryMath.Divide.class)
	public <T extends NumericType<T>> T divide(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Divide.class, in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Divide.class)
	public <T extends NumericType<T>> T divide(final T out, final T in,
		final T b)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Divide.class, out,
			in, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleExp.class)
	public double exp(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleExp.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Exp.class)
	public <I extends RealType<I>, O extends RealType<O>> O exp(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Exp.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.ExpMinusOne.class)
	public <I extends RealType<I>, O extends RealType<O>> O expMinusOne(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.ExpMinusOne.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleFloor.class)
	public double floor(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleFloor.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Floor.class)
	public <I extends RealType<I>, O extends RealType<O>> O floor(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Floor.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.GammaConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O gamma(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Gamma.class, out, in,
			constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Invert.class)
	public <I extends RealType<I>, O extends RealType<O>> O invert(final O out,
		final I in, final double specifiedMin, final double specifiedMax)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Invert.class, out,
			in, specifiedMin, specifiedMax);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerLeftShift.class)
	public int leftShift(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerLeftShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongLeftShift.class)
	public long leftShift(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongLeftShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLog.class)
	public double log(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleLog.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Log.class)
	public <I extends RealType<I>, O extends RealType<O>> O log(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Log.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLog10.class)
	public double log10(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleLog10.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Log10.class)
	public <I extends RealType<I>, O extends RealType<O>> O log10(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Log10.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Log2.class)
	public <I extends RealType<I>, O extends RealType<O>> O log2(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Log2.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleLogOnePlusX.class)
	public double logOnePlusX(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleLogOnePlusX.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.LogOnePlusX.class)
	public <I extends RealType<I>, O extends RealType<O>> O logOnePlusX(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.LogOnePlusX.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMax.class)
	public double max(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMax.class)
	public float max(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMax.class)
	public int max(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMax.class)
	public long max(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongMax.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.MaxConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O max(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Max.class, out, in,
			constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMin.class)
	public double min(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMin.class)
	public float min(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMin.class)
	public int min(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMin.class)
	public long min(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongMin.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.MinConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O min(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Min.class, out, in,
			constant);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddByte.class,
		net.imagej.ops.math.ConstantToArrayImage.AddByte.class,
		net.imagej.ops.math.ConstantToArrayImageP.AddUnsignedByte.class,
		net.imagej.ops.math.ConstantToArrayImage.AddUnsignedByte.class })
	public <B extends GenericByteType<B>> ArrayImg<B, ByteArray> add(
		final ArrayImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<B, ByteArray> result = (ArrayImg<B, ByteArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddDouble.class,
		net.imagej.ops.math.ConstantToArrayImage.AddDouble.class })
	public ArrayImg<DoubleType, DoubleArray> add(
		final ArrayImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(Ops.Math.Add.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddFloat.class,
		net.imagej.ops.math.ConstantToArrayImage.AddFloat.class })
	public ArrayImg<FloatType, FloatArray> add(
		final ArrayImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<FloatType, FloatArray> result =
			(ArrayImg<FloatType, FloatArray>) ops().run(Ops.Math.Add.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddInt.class,
		net.imagej.ops.math.ConstantToArrayImage.AddInt.class,
		net.imagej.ops.math.ConstantToArrayImageP.AddUnsignedInt.class,
		net.imagej.ops.math.ConstantToArrayImage.AddUnsignedInt.class })
	public <I extends GenericIntType<I>> ArrayImg<I, IntArray> add(
		final ArrayImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<I, IntArray> result = (ArrayImg<I, IntArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddLong.class,
		net.imagej.ops.math.ConstantToArrayImage.AddLong.class,
		net.imagej.ops.math.ConstantToArrayImageP.AddUnsignedLong.class,
		net.imagej.ops.math.ConstantToArrayImage.AddUnsignedLong.class })
	public <N extends NativeType<N>> ArrayImg<N, LongArray> add(
		final ArrayImg<LongType, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<N, LongArray> result = (ArrayImg<N, LongArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.AddShort.class,
		net.imagej.ops.math.ConstantToArrayImage.AddShort.class,
		net.imagej.ops.math.ConstantToArrayImageP.AddUnsignedShort.class,
		net.imagej.ops.math.ConstantToArrayImage.AddUnsignedShort.class })
	public <S extends GenericShortType<S>> ArrayImg<S, ShortArray> add(
		final ArrayImg<S, ShortArray> image, final short value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<S, ShortArray> result = (ArrayImg<S, ShortArray>) ops().run(
			Ops.Math.Add.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleMultiply.class)
	public double multiply(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatMultiply.class)
	public float multiply(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerMultiply.class)
	public int multiply(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> in, final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Multiply.class)
	public <T extends NumericType<T>> IterableInterval<T> multiply(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Multiply.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongMultiply.class)
	public long multiply(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongMultiply.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Multiply.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		multiply(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Multiply.class, out,
			in1, in2);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyByte.class,
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyUnsignedByte.class })
	public <B extends GenericByteType<B>> PlanarImg<B, ByteArray> multiply(
		final PlanarImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<B, ByteArray> result = (PlanarImg<B, ByteArray>) ops().run(
			Ops.Math.Divide.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.MultiplyDouble.class)
	public PlanarImg<DoubleType, DoubleArray> multiply(
		final PlanarImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(
				net.imagej.ops.Ops.Math.Multiply.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.MultiplyFloat.class)
	public PlanarImg<FloatType, FloatArray> multiply(
		final PlanarImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<FloatType, FloatArray> result =
			(PlanarImg<FloatType, FloatArray>) ops().run(
				net.imagej.ops.Ops.Math.Multiply.class, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.MultiplyInt.class,
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyUnsignedInt.class })
	public <I extends GenericIntType<I>> PlanarImg<I, IntArray> multiply(
		final PlanarImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<I, IntArray> result = (PlanarImg<I, IntArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyLong.class,
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyUnsignedLong.class })
	public <N extends NativeType<N>> PlanarImg<N, LongArray> multiply(
		final PlanarImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<N, LongArray> result = (PlanarImg<N, LongArray>) ops().run(
			Ops.Math.Multiply.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyShort.class,
		net.imagej.ops.math.ConstantToPlanarImage.MultiplyUnsignedShort.class })
	public <S extends GenericShortType<S>> PlanarImg<S, ShortArray> multiply(
		final PlanarImg<S, ShortArray> arg, final short value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<S, ShortArray> result = (PlanarImg<S, ShortArray>) ops()
			.run(Ops.Math.Multiply.NAME, arg, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputRAI.Multiply.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> multiply(
		final RandomAccessibleInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.Ops.Math.Multiply.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class)
	public <T extends NumericType<T>> T multiply(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Multiply.class, in,
			b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Multiply.class)
	public <T extends NumericType<T>> T multiply(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Multiply.class, out,
			in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.NearestInt.class)
	public <I extends RealType<I>, O extends RealType<O>> O nearestInt(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.NearestInt.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleNegate.class)
	public double negate(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatNegate.class)
	public float negate(final float a) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerNegate.class)
	public int negate(final int a) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongNegate.class)
	public long negate(final long a) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongNegate.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Negate.class)
	public <I extends RealType<I>, O extends RealType<O>> O negate(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Negate.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerOr.class)
	public int or(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerOr.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongOr.class)
	public long or(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongOr.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Or.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		or(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Or.class, out, in1,
			in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoublePower.class)
	public double power(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoublePower.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.PowerConstant.class)
	public <I extends RealType<I>, O extends RealType<O>> O power(final O out,
		final I in, final double constant)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Power.class, out, in,
			constant);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.RandomGaussian.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomGaussian(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.RandomGaussian.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.RandomGaussian.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomGaussian(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.RandomGaussian.class,
			out, in, seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.RandomUniform.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomUniform(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.RandomUniform.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.RandomUniform.class)
	public <I extends RealType<I>, O extends RealType<O>> O randomUniform(
		final O out, final I in, final long seed)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.RandomUniform.class,
			out, in, seed);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Reciprocal.class)
	public <I extends RealType<I>, O extends RealType<O>> O reciprocal(
		final O out, final I in, final double dbzVal)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Reciprocal.class,
			out, in, dbzVal);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleRemainder.class)
	public double remainder(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatRemainder.class)
	public float remainder(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerRemainder.class)
	public int remainder(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongRemainder.class)
	public long remainder(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongRemainder.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerRightShift.class)
	public int rightShift(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongRightShift.class)
	public long rightShift(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleRound.class)
	public double round(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleRound.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatRound.class)
	public float round(final float a) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatRound.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Round.class)
	public <I extends RealType<I>, O extends RealType<O>> O round(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Round.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sec.class)
	public <I extends RealType<I>, O extends RealType<O>> O sec(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sec.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sech.class)
	public <I extends RealType<I>, O extends RealType<O>> O sech(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sech.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSignum.class)
	public double signum(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleSignum.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatSignum.class)
	public float signum(final float a) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatSignum.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Signum.class)
	public <I extends RealType<I>, O extends RealType<O>> O signum(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Signum.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSin.class)
	public double sin(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleSin.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sin.class)
	public <I extends RealType<I>, O extends RealType<O>> O sin(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sin.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sinc.class)
	public <I extends RealType<I>, O extends RealType<O>> O sinc(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sinc.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.SincPi.class)
	public <I extends RealType<I>, O extends RealType<O>> O sincPi(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.SincPi.class, out,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSinh.class)
	public double sinh(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleSinh.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sinh.class)
	public <I extends RealType<I>, O extends RealType<O>> O sinh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sinh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sqr.class)
	public <I extends RealType<I>, O extends RealType<O>> O sqr(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sqr.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSqrt.class)
	public double sqrt(final double a) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleSqrt.class, a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Sqrt.class)
	public <I extends RealType<I>, O extends RealType<O>> O sqrt(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Sqrt.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Step.class)
	public <I extends RealType<I>, O extends RealType<O>> O step(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Step.class, out, in);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.SubtractByte.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractByte.class,
		net.imagej.ops.math.ConstantToArrayImageP.SubtractUnsignedByte.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractUnsignedByte.class })
	public <B extends GenericByteType<B>> ArrayImg<B, ByteArray> subtract(
		final ArrayImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<B, ByteArray> result = (ArrayImg<B, ByteArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.SubtractDouble.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractDouble.class })
	public ArrayImg<DoubleType, DoubleArray> subtract(
		final ArrayImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<DoubleType, DoubleArray> result =
			(ArrayImg<DoubleType, DoubleArray>) ops().run(Ops.Math.Subtract.NAME,
				image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.SubtractFloat.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractFloat.class })
	public ArrayImg<FloatType, FloatArray> subtract(
		final ArrayImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<FloatType, FloatArray> result =
			(ArrayImg<FloatType, FloatArray>) ops().run(Ops.Math.Subtract.NAME, image,
				value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToArrayImageP.SubtractInt.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractInt.class,
		net.imagej.ops.math.ConstantToArrayImageP.SubtractUnsignedInt.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractUnsignedInt.class })
	public <I extends GenericIntType<I>> ArrayImg<I, IntArray> subtract(
		final ArrayImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<I, IntArray> result = (ArrayImg<I, IntArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.SubtractLong.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractLong.class,
		net.imagej.ops.math.ConstantToArrayImageP.SubtractUnsignedLong.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractUnsignedLong.class })
	public <N extends NativeType<N>> ArrayImg<N, LongArray> subtract(
		final ArrayImg<N, LongArray> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<N, LongArray> result = (ArrayImg<N, LongArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToArrayImageP.SubtractShort.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractShort.class,
		net.imagej.ops.math.ConstantToArrayImageP.SubtractUnsignedShort.class,
		net.imagej.ops.math.ConstantToArrayImage.SubtractUnsignedShort.class })
	public <S extends GenericShortType<S>> ArrayImg<S, ShortArray> subtract(
		final ArrayImg<S, ShortArray> image, final short value)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<S, ShortArray> result = (ArrayImg<S, ShortArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleSubtract.class)
	public double subtract(final double a, final double b) {
		final double result = (Double) ops().run(
			net.imagej.ops.math.PrimitiveMath.DoubleSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.FloatSubtract.class)
	public float subtract(final float a, final float b) {
		final float result = (Float) ops().run(
			net.imagej.ops.math.PrimitiveMath.FloatSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToIIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> in1, final IterableInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerSubtract.class)
	public int subtract(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> in1, final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.IIToRAIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> out, final IterableInterval<T> in1,
		final RandomAccessibleInterval<T> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> in, final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputII.Subtract.class)
	public <T extends NumericType<T>> IterableInterval<T> subtract(
		final IterableInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Math.Subtract.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongSubtract.class)
	public long subtract(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongSubtract.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Subtract.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		subtract(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Subtract.class, out,
			in1, in2);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.SubtractByte.class,
		net.imagej.ops.math.ConstantToPlanarImage.SubtractUnsignedByte.class })
	public <B extends GenericByteType<B>> PlanarImg<B, ByteArray> subtract(
		final PlanarImg<B, ByteArray> image, final byte value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<B, ByteArray> result = (PlanarImg<B, ByteArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.SubtractDouble.class)
	public PlanarImg<DoubleType, DoubleArray> subtract(
		final PlanarImg<DoubleType, DoubleArray> image, final double value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<DoubleType, DoubleArray> result =
			(PlanarImg<DoubleType, DoubleArray>) ops().run(
				net.imagej.ops.Ops.Math.Subtract.class, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToPlanarImage.SubtractFloat.class)
	public PlanarImg<FloatType, FloatArray> subtract(
		final PlanarImg<FloatType, FloatArray> image, final float value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<FloatType, FloatArray> result =
			(PlanarImg<FloatType, FloatArray>) ops().run(
				net.imagej.ops.Ops.Math.Subtract.class, image, value);
		return result;
	}

	@OpMethod(ops = { net.imagej.ops.math.ConstantToPlanarImage.SubtractInt.class,
		net.imagej.ops.math.ConstantToPlanarImage.SubtractUnsignedInt.class })
	public <I extends GenericIntType<I>> PlanarImg<I, IntArray> subtract(
		final PlanarImg<I, IntArray> image, final int value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<I, IntArray> result = (PlanarImg<I, IntArray>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.SubtractLong.class,
		net.imagej.ops.math.ConstantToPlanarImage.SubtractUnsignedLong.class })
	public <N extends NativeType<N>, A extends ArrayDataAccess<A>> PlanarImg<N, A>
		subtract(final PlanarImg<N, A> image, final long value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<N, A> result = (PlanarImg<N, A>) ops().run(
			Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.ConstantToPlanarImage.SubtractShort.class,
		net.imagej.ops.math.ConstantToPlanarImage.SubtractUnsignedShort.class })
	public <S extends GenericShortType<S>> PlanarImg<S, ShortArray> subtract(
		final PlanarImg<S, ShortArray> image, final short value)
	{
		@SuppressWarnings("unchecked")
		final PlanarImg<S, ShortArray> result = (PlanarImg<S, ShortArray>) ops()
			.run(Ops.Math.Subtract.NAME, image, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.ConstantToIIOutputRAI.Subtract.class)
	public <T extends NumericType<T>> RandomAccessibleInterval<T> subtract(
		final RandomAccessibleInterval<T> out, final IterableInterval<T> in,
		final T value)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.Ops.Math.Subtract.class, out, in, value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class)
	public <T extends NumericType<T>> T subtract(final T in, final T b) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Subtract.class, in,
			b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NumericTypeBinaryMath.Subtract.class)
	public <T extends NumericType<T>> T subtract(final T out, final T in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Subtract.class, out,
			in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleTan.class)
	public double tan(final double a) {
		final double result = (Double) ops().run(net.imagej.ops.Ops.Math.Tan.class,
			a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Tan.class)
	public <I extends RealType<I>, O extends RealType<O>> O tan(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Tan.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.DoubleTanh.class)
	public double tanh(final double a) {
		final double result = (Double) ops().run(net.imagej.ops.Ops.Math.Tanh.class,
			a);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Tanh.class)
	public <I extends RealType<I>, O extends RealType<O>> O tanh(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Tanh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.UnaryRealTypeMath.Ulp.class)
	public <I extends RealType<I>, O extends RealType<O>> O ulp(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Ulp.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.math.PrimitiveMath.IntegerUnsignedRightShift.class)
	public int unsignedRightShift(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerUnsignedRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongUnsignedRightShift.class)
	public long unsignedRightShift(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongUnsignedRightShift.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.IntegerXor.class)
	public int xor(final int a, final int b) {
		final int result = (Integer) ops().run(
			net.imagej.ops.math.PrimitiveMath.IntegerXor.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.PrimitiveMath.LongXor.class)
	public long xor(final long a, final long b) {
		final long result = (Long) ops().run(
			net.imagej.ops.math.PrimitiveMath.LongXor.class, a, b);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.BinaryRealTypeMath.Xor.class)
	public <
		I1 extends RealType<I1>, I2 extends RealType<I2>, O extends RealType<O>> O
		xor(final O out, final I1 in1, final I2 in2)
	{
		@SuppressWarnings("unchecked")
		final O result = (O) ops().run(net.imagej.ops.Ops.Math.Xor.class, out, in1,
			in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.NullaryNumericTypeMath.Zero.class)
	public <T extends NumericType<T>> T zero(final T out) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(net.imagej.ops.Ops.Math.Zero.class, out);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.math.multiply.ComplexConjugateMultiplyMap.class })
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		complexConjugateMultiply(final IterableInterval<O> out,
			final IterableInterval<I> in1, final IterableInterval<I> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result = (IterableInterval<O>) ops().run(
			net.imagej.ops.Ops.Math.ComplexConjugateMultiply.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.math.multiply.ComplexConjugateMultiplyOp.class)
	public <C extends ComplexType<C>> C complexConjugateMultiply(final C in1,
		final C in2, final C out)
	{
		@SuppressWarnings("unchecked")
		final C result = (C) ops().run(
			net.imagej.ops.math.multiply.ComplexConjugateMultiplyOp.class, out, in1,
			in2);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "math";
	}

}
