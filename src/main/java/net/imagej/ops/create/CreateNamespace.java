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

package net.imagej.ops.create;

import net.imagej.ImgPlus;
import net.imagej.ImgPlusMetadata;
import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The create namespace contains ops that create objects.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class CreateNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "create";
	}

	// -- img --

	/**
	 * Creates an {@link Img} of type {@link DoubleType} with the given
	 * dimensions.
	 */
	public Img<DoubleType> img(final Integer[] dims) {
		int[] ints = new int[dims.length];
		for (int i=0; i<ints.length; i++) ints[i] = dims[i];
		return img(ints);
	}

	/**
	 * Creates an {@link Img} of type {@link DoubleType} with the given
	 * dimensions.
	 */
	public Img<DoubleType> img(final Long[] dims) {
		long[] longs = new long[dims.length];
		for (int i=0; i<longs.length; i++) longs[i] = dims[i];
		return img(longs);
	}

	/**
	 * Creates an {@link Img} of type {@link DoubleType} with the given
	 * dimensions.
	 */
	public Img<DoubleType> img(final int[] dims) {
		return img(new FinalDimensions(dims), new DoubleType());
	}

	/**
	 * Creates an {@link Img} of type {@link DoubleType} with the given
	 * dimensions.
	 */
	public Img<DoubleType> img(final long[] dims) {
		return img(new FinalDimensions(dims), new DoubleType());
	}

	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromDimsAndType.class)
	public <T extends NativeType<T>> Img<T> img(final Dimensions in1,
		final T in2)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
			net.imagej.ops.create.img.CreateImgFromDimsAndType.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromDimsAndType.class)
	public <T extends NativeType<T>> Img<T> img(final Dimensions in1, final T in2,
		final ImgFactory<T> factory)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
			net.imagej.ops.create.img.CreateImgFromDimsAndType.class, in1, in2,
			factory);
		return result;
	}

	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromII.class)
	public <T extends NativeType<T>> Img<T> img(final IterableInterval<T> in) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
			net.imagej.ops.create.img.CreateImgFromII.class, in);
		return result;
	}

	// NB: Should be "T extends Type<T>" but then the Java compiler considers
	// it ambiguous with img(IterableInterval) and img(RandomAccessibleInterval).
	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromImg.class)
	public <T extends NativeType<T>> Img<T> img(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
			net.imagej.ops.create.img.CreateImgFromImg.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromInterval.class)
	public Img<DoubleType> img(final Interval interval) {
		@SuppressWarnings("unchecked")
		final Img<DoubleType> result = (Img<DoubleType>) ops().run(
			net.imagej.ops.create.img.CreateImgFromInterval.class, interval);
		return result;
	}

	@OpMethod(op = net.imagej.ops.create.img.CreateImgFromRAI.class)
	public <T extends NativeType<T>> Img<T> img(
		final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(
			net.imagej.ops.create.img.CreateImgFromRAI.class, in);
		return result;
	}

	// -- imgFactory --

	@OpMethod(op = net.imagej.ops.Ops.Create.ImgFactory.class)
	public Object imgFactory(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Create.ImgFactory.class, args);
	}

	@OpMethod(op = net.imagej.ops.create.imgFactory.DefaultCreateImgFactory.class)
	public <T extends NativeType<T>> ImgFactory<T> imgFactory() {
		// NB: The generic typing of ImgFactory is broken; see:
		// https://github.com/imglib/imglib2/issues/91
		@SuppressWarnings("unchecked")
		final ImgFactory<T> result = (ImgFactory<T>) ops().run(
			net.imagej.ops.create.imgFactory.DefaultCreateImgFactory.class);
		return result;
	}

	@OpMethod(
		ops = net.imagej.ops.create.imgFactory.DefaultCreateImgFactory.class)
	public <T extends NativeType<T>> ImgFactory<T> imgFactory(
		final Dimensions dims)
	{
		// NB: The generic typing of ImgFactory is broken; see:
		// https://github.com/imglib/imglib2/issues/91
		@SuppressWarnings("unchecked")
		final ImgFactory<T> result = (ImgFactory<T>) ops().run(
			net.imagej.ops.Ops.Create.ImgFactory.class, dims);
		return result;
	}

	@OpMethod(
		ops = net.imagej.ops.create.imgFactory.CreateImgFactoryFromImg.class)
	public <T extends NativeType<T>> ImgFactory<T> imgFactory(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final ImgFactory<T> result = (ImgFactory<T>) ops().run(
			net.imagej.ops.Ops.Create.ImgFactory.class, in);
		return result;
	}

	// -- imgLabeling --

	@OpMethod(op = net.imagej.ops.Ops.Create.ImgLabeling.class)
	public Object imgLabeling(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Create.ImgLabeling.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Dimensions dims)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class, dims);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Dimensions dims, final T outType)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class, dims,
				outType);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Dimensions dims, final T outType, final ImgFactory<T> fac)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class, dims,
				outType, fac);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Dimensions dims, final T outType, final ImgFactory<T> fac,
		final int maxNumLabelSets)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.DefaultCreateImgLabeling.class, dims,
				outType, fac, maxNumLabelSets);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Interval interval)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class,
				interval);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Interval interval, final T outType)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class,
				interval, outType);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Interval interval, final T outType, final ImgFactory<T> fac)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class,
				interval, outType, fac);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class)
	public <L, T extends IntegerType<T>> ImgLabeling<L, T> imgLabeling(
		final Interval interval, final T outType, final ImgFactory<T> fac,
		final int maxNumLabelSets)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, T> result =
			(ImgLabeling<L, T>) ops().run(
				net.imagej.ops.create.imgLabeling.CreateImgLabelingFromInterval.class,
				interval, outType, fac, maxNumLabelSets);
		return result;
	}

	// -- imgPlus --

	@OpMethod(op = net.imagej.ops.Ops.Create.ImgPlus.class)
	public Object imgPlus(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Create.ImgPlus.class, args);
	}

	@OpMethod(op = net.imagej.ops.create.imgPlus.DefaultCreateImgPlus.class)
	public <T> ImgPlus<T> imgPlus(final Img<T> img) {
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result =
			(ImgPlus<T>) ops().run(
				net.imagej.ops.create.imgPlus.DefaultCreateImgPlus.class, img);
		return result;
	}

	@OpMethod(op = net.imagej.ops.create.imgPlus.DefaultCreateImgPlus.class)
	public <T> ImgPlus<T>
		imgPlus(final Img<T> img, final ImgPlusMetadata metadata)
	{
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result =
			(ImgPlus<T>) ops()
				.run(net.imagej.ops.create.imgPlus.DefaultCreateImgPlus.class, img,
					metadata);
		return result;
	}

	// -- integerType --

	@OpMethod(op = net.imagej.ops.Ops.Create.IntegerType.class)
	public Object integerType(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Create.IntegerType.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.create.integerType.DefaultCreateIntegerType.class)
	public IntegerType integerType() {
		final IntegerType result =
			(IntegerType) ops().run(
				net.imagej.ops.create.integerType.DefaultCreateIntegerType.class);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.integerType.DefaultCreateIntegerType.class)
	public IntegerType integerType(final long maxValue) {
		final IntegerType result =
			(IntegerType) ops().run(
				net.imagej.ops.create.integerType.DefaultCreateIntegerType.class,
				maxValue);
		return result;
	}

	// -- kernel --

	/** Executes the "kernel" operation on the given arguments. */
	@OpMethod(op = Ops.Create.Kernel.class)
	public Object kernel(final Object... args) {
		return ops().run(Ops.Create.Kernel.NAME, args);
	}

	/** Executes the "kernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernel.CreateKernel.class)
	public <T extends ComplexType<T>> Img<T> kernel(final double[]... values) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(net.imagej.ops.create.kernel.CreateKernel.class,
				new Object[] { values });
		return result;
	}

	/** Executes the "kernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernel.CreateKernel.class)
	public <T extends ComplexType<T>> Img<T> kernel(final T outType, final double[]... values) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(net.imagej.ops.create.kernel.CreateKernel.class, outType, values);
		return result;
	}

	/** Executes the "kernel" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernel.CreateKernel.class)
	public <T extends ComplexType<T>> Img<T> kernel(final T outType, final ImgFactory<T> fac,
			final double[]... values) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(net.imagej.ops.create.kernel.CreateKernel.class, outType, fac, values);
		return result;
	}

	// -- kernelGauss --

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(op = Ops.Create.KernelGauss.class)
	public Object kernelGauss(final Object... args) {
		return ops().run(Ops.Create.KernelGauss.NAME, args);
	}

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class)
	public <T extends ComplexType<T>> Img<T> kernelGauss(final int numDimensions,
		final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class,
				numDimensions, sigma);
		return result;
	}

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class)
	public <T extends ComplexType<T>> Img<T> kernelGauss(final T outType,
		final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class,
				outType, numDimensions, sigma);
		return result;
	}

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(
		op = net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class)
	public <T extends ComplexType<T>> Img<T> kernelGauss(final T outType,
		final ImgFactory<T> fac, final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGaussSymmetric.class,
				outType, fac, numDimensions, sigma);
		return result;
	}


	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelGauss.CreateKernelGauss.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelGauss(
		final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGauss.class, sigma);
		return result;
	}

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelGauss.CreateKernelGauss.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelGauss(
		final T outType, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGauss.class, outType,
				sigma);
		return result;
	}

	/** Executes the "kernelGauss" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelGauss.CreateKernelGauss.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelGauss(
		final T outType, final ImgFactory<T> fac, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelGauss.CreateKernelGauss.class, outType,
				fac, sigma);
		return result;
	}



	// -- kernelLog --

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = Ops.Create.KernelLog.class)
	public Object kernelLog(final Object... args) {
		return ops().run(Ops.Create.KernelLog.NAME, args);
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class)
	public
		<T extends ComplexType<T>> Img<T> kernelLog(final int numDimensions,
			final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class,
				numDimensions, sigma);
		return result;
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class)
	public
		<T extends ComplexType<T>> Img<T> kernelLog(final T outType,
			final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class,
				outType, numDimensions, sigma);
		return result;
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class)
	public
		<T extends ComplexType<T>> Img<T> kernelLog(final T outType,
			final ImgFactory<T> fac, final int numDimensions, final double sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(
				net.imagej.ops.create.kernelLog.CreateKernelLogSymmetric.class,
				outType, fac, numDimensions, sigma);
		return result;
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLog.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelLog(
		final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.create.kernelLog.CreateKernelLog.class,
				sigma);
		return result;
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLog.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelLog(
		final T outType, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.create.kernelLog.CreateKernelLog.class,
				outType, sigma);
		return result;
	}

	/** Executes the "kernelLog" operation on the given arguments. */
	@OpMethod(op = net.imagej.ops.create.kernelLog.CreateKernelLog.class)
	public <T extends ComplexType<T> & NativeType<T>> Img<T> kernelLog(
		final T outType, final ImgFactory<T> fac, final double... sigma)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result =
			(Img<T>) ops().run(net.imagej.ops.create.kernelLog.CreateKernelLog.class,
				outType, fac, sigma);
		return result;
	}


	// -- labelingMapping --

	@OpMethod(op = net.imagej.ops.Ops.Create.LabelingMapping.class)
	public Object labelingMapping(final Object... args) {
		return ops().run(net.imagej.ops.Ops.Create.LabelingMapping.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.create.labelingMapping.DefaultCreateLabelingMapping.class)
	public
		<L> LabelingMapping<L> labelingMapping() {
		@SuppressWarnings("unchecked")
		final LabelingMapping<L> result =
			(LabelingMapping<L>) ops()
				.run(
					net.imagej.ops.create.labelingMapping.DefaultCreateLabelingMapping.class);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.labelingMapping.DefaultCreateLabelingMapping.class)
	public
		<L> LabelingMapping<L> labelingMapping(final int maxNumSets) {
		@SuppressWarnings("unchecked")
		final LabelingMapping<L> result =
			(LabelingMapping<L>) ops()
				.run(
					net.imagej.ops.create.labelingMapping.DefaultCreateLabelingMapping.class,
					maxNumSets);
		return result;
	}

	// -- nativeType --

	@OpMethod(op = net.imagej.ops.create.nativeType.DefaultCreateNativeType.class)
	public DoubleType nativeType() {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.create.nativeType.DefaultCreateNativeType.class);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.create.nativeType.CreateNativeTypeFromClass.class)
	public <T extends NativeType<T>> T nativeType(final Class<T> type) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(
			net.imagej.ops.create.nativeType.CreateNativeTypeFromClass.class, type);
		return result;
	}

	// -- object --

	@OpMethod(op = net.imagej.ops.create.object.CreateObjectFromClass.class)
	public <T> T object(final Class<T> in) {
		@SuppressWarnings("unchecked")
		final T result = (T) ops().run(
			net.imagej.ops.create.object.CreateObjectFromClass.class, in);
		return result;
	}

}
