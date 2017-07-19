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

package net.imagej.ops.transform;

import java.util.List;

import net.imagej.ImgPlus;
import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.transform.addDimensionView.DefaultAddDimensionView;
import net.imagej.ops.transform.collapseNumericView.DefaultCollapseNumeric2CompositeIntervalView;
import net.imagej.ops.transform.collapseNumericView.DefaultCollapseNumeric2CompositeView;
import net.imagej.ops.transform.collapseRealView.DefaultCollapseReal2CompositeIntervalView;
import net.imagej.ops.transform.collapseRealView.DefaultCollapseReal2CompositeView;
import net.imagej.ops.transform.collapseView.DefaultCollapse2CompositeIntervalView;
import net.imagej.ops.transform.collapseView.DefaultCollapse2CompositeView;
import net.imagej.ops.transform.concatenateView.ConcatenateViewWithAccessMode;
import net.imagej.ops.transform.concatenateView.DefaultConcatenateView;
import net.imagej.ops.transform.dropSingletonDimensionsView.DefaultDropSingletonDimensionsView;
import net.imagej.ops.transform.extendBorderView.DefaultExtendBorderView;
import net.imagej.ops.transform.extendMirrorDoubleView.DefaultExtendMirrorDoubleView;
import net.imagej.ops.transform.extendMirrorSingleView.DefaultExtendMirrorSingleView;
import net.imagej.ops.transform.extendPeriodicView.DefaultExtendPeriodicView;
import net.imagej.ops.transform.extendRandomView.DefaultExtendRandomView;
import net.imagej.ops.transform.extendValueView.DefaultExtendValueView;
import net.imagej.ops.transform.extendView.DefaultExtendView;
import net.imagej.ops.transform.extendZeroView.DefaultExtendZeroView;
import net.imagej.ops.transform.flatIterableView.DefaultFlatIterableView;
import net.imagej.ops.transform.hyperSliceView.DefaultHyperSliceView;
import net.imagej.ops.transform.interpolateView.DefaultInterpolateView;
import net.imagej.ops.transform.intervalView.DefaultIntervalView;
import net.imagej.ops.transform.intervalView.IntervalViewMinMax;
import net.imagej.ops.transform.invertAxisView.DefaultInvertAxisView;
import net.imagej.ops.transform.offsetView.DefaultOffsetView;
import net.imagej.ops.transform.offsetView.OffsetViewInterval;
import net.imagej.ops.transform.offsetView.OffsetViewOriginSize;
import net.imagej.ops.transform.permuteCoordinatesInverseView.DefaultPermuteCoordinatesInverseView;
import net.imagej.ops.transform.permuteCoordinatesInverseView.PermuteCoordinateInverseViewOfDimension;
import net.imagej.ops.transform.permuteCoordinatesView.DefaultPermuteCoordinatesView;
import net.imagej.ops.transform.permuteCoordinatesView.PermuteCoordinatesViewOfDimension;
import net.imagej.ops.transform.permuteView.DefaultPermuteView;
import net.imagej.ops.transform.rasterView.DefaultRasterView;
import net.imagej.ops.transform.rotateView.DefaultRotateView;
import net.imagej.ops.transform.shearView.DefaultShearView;
import net.imagej.ops.transform.shearView.ShearViewInterval;
import net.imagej.ops.transform.stackView.DefaultStackView;
import net.imagej.ops.transform.stackView.StackViewWithAccessMode;
import net.imagej.ops.transform.subsampleView.DefaultSubsampleView;
import net.imagej.ops.transform.subsampleView.SubsampleViewStepsForDims;
import net.imagej.ops.transform.translateView.DefaultTranslateView;
import net.imagej.ops.transform.unshearView.DefaultUnshearView;
import net.imagej.ops.transform.unshearView.UnshearViewInterval;
import net.imagej.ops.transform.zeroMinView.DefaultZeroMinView;
import net.imglib2.EuclideanSpace;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.transform.integer.shear.InverseShearTransform;
import net.imglib2.transform.integer.shear.ShearTransform;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.IterableRandomAccessibleInterval;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;
import net.imglib2.view.StackView;
import net.imglib2.view.StackView.StackAccessMode;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.TransformView;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;
import net.imglib2.view.composite.NumericComposite;
import net.imglib2.view.composite.RealComposite;

import org.scijava.plugin.Plugin;

/**
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @author Philipp Hanslovsky
 *
 * All method descriptions are from {@link net.imglib2.view.Views}.
 */
@Plugin(type = Namespace.class)
public class TransformNamespace extends AbstractNamespace {

	/**
	 * Create view which adds a dimension to the source
	 * {@link RandomAccessibleInterval}. The {@link Interval} boundaries in the
	 * additional dimension are set to the specified values. The additional
	 * dimension is the last dimension. For example, an XYZ view is created for an
	 * XY source. When accessing an XYZ sample in the view, the final coordinate
	 * is discarded and the source XY sample is accessed.
	 * 
	 * @param input the source
	 * @param min Interval min in the additional dimension.
	 * @param max Interval max in the additional dimension.
	 */
	@OpMethod(op = net.imagej.ops.transform.addDimensionView.AddDimensionViewMinMax.class)
	public <T> IntervalView<T> addDimensionView(
		final RandomAccessibleInterval<T> input, final long min, final long max)
	{
		return (IntervalView<T>) ops().run(
			Ops.Transform.AddDimensionView.class, input, min, max);
	}

	/**
	 * Create view which adds a dimension to the source {@link RandomAccessible} .
	 * The additional dimension is the last dimension. For example, an XYZ view is
	 * created for an XY source. When accessing an XYZ sample in the view, the
	 * final coordinate is discarded and the source XY sample is accessed.
	 * 
	 * @param input the source
	 */
	@OpMethod(op = net.imagej.ops.transform.addDimensionView.DefaultAddDimensionView.class)
	public <T> MixedTransformView<T> addDimensionView(
		final RandomAccessible<T> input)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.AddDimensionView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessible}&lt;T&gt; into an ( <em>n</em>
	 * -1)-dimensional {@link RandomAccessible}&lt; {@link GenericComposite}
	 * &lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link GenericComposite GenericComposites}
	 */
	@OpMethod(op = net.imagej.ops.transform.collapseView.DefaultCollapse2CompositeView.class)
	public <T> CompositeView<T, ? extends GenericComposite<T>>
		collapseView(final RandomAccessible<T> input)
	{
		return (CompositeView<T, ? extends GenericComposite<T>>) ops().run(
			Ops.Transform.CollapseView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T&gt; into an ( <em>n</em>
	 * -1)-dimensional {@link RandomAccessibleInterval}&lt;
	 * {@link GenericComposite}&lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link GenericComposite GenericComposites}
	 */
	@OpMethod(
		op = net.imagej.ops.transform.collapseView.DefaultCollapse2CompositeIntervalView.class)
	public <T>
		CompositeIntervalView<T, ? extends GenericComposite<T>> collapseView(
			final RandomAccessibleInterval<T> input)
	{
		return (CompositeIntervalView<T, ? extends GenericComposite<T>>) ops().run(
			Ops.Transform.CollapseView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T extends
	 * {@link NumericType}&lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional
	 * {@link RandomAccessibleInterval}&lt;{@link NumericComposite}&lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @param numChannels the number of channels that the {@link NumericComposite}
	 *          will consider when performing calculations
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link NumericComposite NumericComposites}
	 */
	@OpMethod(
		op = net.imagej.ops.transform.collapseNumericView.DefaultCollapseNumeric2CompositeView.class)
	public <N extends NumericType<N>> CompositeView<N, NumericComposite<N>>
		collapseNumericView(final RandomAccessible<N> input, final int numChannels)
	{
		return (CompositeView<N, NumericComposite<N>>) ops().run(
				Ops.Transform.CollapseNumericView.class, input, numChannels);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T extends
	 * {@link NumericType}&lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional
	 * {@link RandomAccessibleInterval}&lt;{@link NumericComposite}&lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link NumericComposite NumericComposites}
	 */
	@OpMethod(
		op = net.imagej.ops.transform.collapseNumericView.DefaultCollapseNumeric2CompositeIntervalView.class)
	public
		<N extends NumericType<N>>
		CompositeIntervalView<N, NumericComposite<N>>
		collapseNumericView(final RandomAccessibleInterval<N> input)
	{
		return (CompositeIntervalView<N, NumericComposite<N>>) ops().run(
				Ops.Transform.CollapseNumericView.class, input);
	}

	/**
	 * Executes the "crop" operation on the given arguments.
	 * @param in
	 * @param interval
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.crop.CropImgPlus.class)
	public <T extends Type<T>> ImgPlus<T> crop(final ImgPlus<T> in,
			final Interval interval) {
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result = (ImgPlus<T>) ops().run(
				Ops.Transform.Crop.class, in, interval);
		return result;
	}

	/** 
	 * Executes the "crop" operation on the given arguments.
	 * @param in
	 * @param interval
	 * @param dropSingleDimensions
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.crop.CropImgPlus.class)
	public <T extends Type<T>> ImgPlus<T> crop(final ImgPlus<T> in,
			final Interval interval, final boolean dropSingleDimensions) {
		@SuppressWarnings("unchecked")
		final ImgPlus<T> result = (ImgPlus<T>) ops().run(
				Ops.Transform.Crop.class, in, interval,
				dropSingleDimensions);
		return result;
	}

	/** 
	 * Executes the "crop" operation on the given arguments.
	 * @param in
	 * @param interval
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.crop.CropRAI.class)
	public <T> RandomAccessibleInterval<T> crop(
			final RandomAccessibleInterval<T> in, final Interval interval) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(Ops.Transform.Crop.class, in, interval);
		return result;
	}

	/** 
	 * Executes the "crop" operation on the given arguments. 
	 * @param in
	 * @param interval
	 * @param dropSingleDimensions
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.crop.CropRAI.class)
	public <T> RandomAccessibleInterval<T> crop(
			final RandomAccessibleInterval<T> in, final Interval interval,
			final boolean dropSingleDimensions) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(Ops.Transform.Crop.class, in, interval,
						dropSingleDimensions);
		return result;
	}

	/**
	 * Removes all unit dimensions (dimensions with size one) from the
	 * RandomAccessibleInterval
	 * 
	 * @param input the source
	 * @return a RandomAccessibleInterval without dimensions of size one
	 */
	@OpMethod(op = net.imagej.ops.transform.dropSingletonDimensionsView.DefaultDropSingletonDimensionsView.class)
	public <T> RandomAccessibleInterval<T>
		dropSingletonDimensionsView(final RandomAccessibleInterval<T> input)
	{
		return (RandomAccessibleInterval<T>) ops().run(
				Ops.Transform.DropSingletonDimensionsView.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with an out-of-bounds strategy.
	 * 
	 * @param input the interval to extend.
	 * @param factory the out-of-bounds strategy.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 */
	@OpMethod(op = net.imagej.ops.transform.extendView.DefaultExtendView.class)
	public <T, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendView(final F input,
			final OutOfBoundsFactory<T, ? super F> factory)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				Ops.Transform.ExtendView.class, input, factory);
	}

	/**
	 * Extend a RandomAccessibleInterval with an out-of-bounds strategy to repeat
	 * border pixels.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsBorder
	 */
	@OpMethod(op = net.imagej.ops.transform.extendBorderView.DefaultExtendBorderView.class)
	public <T, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendBorderView(final F input)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendBorderView.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a mirroring out-of-bounds strategy.
	 * Boundary pixels are repeated.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsMirrorDoubleBoundary
	 */
	@OpMethod(op = net.imagej.ops.transform.extendMirrorDoubleView.DefaultExtendMirrorDoubleView.class)
	public <T, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendMirrorDoubleView(final F input)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendMirrorDoubleView.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a mirroring out-of-bounds strategy.
	 * Boundary pixels are not repeated. Note that this requires that all
	 * dimensions of the source (F source) must be &gt; 1.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsMirrorSingleBoundary
	 */
	@OpMethod(op = net.imagej.ops.transform.extendMirrorSingleView.DefaultExtendMirrorSingleView.class)
	public <T extends Type<T>, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendMirrorSingleView(final F input)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendMirrorSingleView.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a periodic out-of-bounds strategy.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsPeriodic
	 */
	@OpMethod(op = net.imagej.ops.transform.extendPeriodicView.DefaultExtendPeriodicView.class)
	public <T, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendPeriodicView(final F input)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendPeriodicView.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a random-value out-of-bounds
	 * strategy.
	 * 
	 * @param input the interval to extend.
	 * @param min the minimal random value
	 * @param max the maximal random value
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsRandomValue
	 */
	@OpMethod(op = net.imagej.ops.transform.extendRandomView.DefaultExtendRandomView.class)
	public <T extends RealType<T>, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendRandomView(final F input,
			final double min, final double max)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendRandomView.class, input, min, max);
	}

	/**
	 * Extend a RandomAccessibleInterval with a constant-value out-of-bounds
	 * strategy.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsConstantValue
	 */
	@OpMethod(op = net.imagej.ops.transform.extendValueView.DefaultExtendValueView.class)
	public <T extends Type<T>, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendValueView(final F input,
			final T value)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
			Ops.Transform.ExtendValueView.class, input, value);
	}

	/**
	 * Extend a RandomAccessibleInterval with a constant-value out-of-bounds
	 * strategy where the constant value is the zero-element of the data type.
	 * 
	 * @param input the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity with a constant value of zero.
	 * @see net.imglib2.outofbounds.OutOfBoundsConstantValue
	 */
	@OpMethod(op = net.imagej.ops.transform.extendZeroView.DefaultExtendZeroView.class)
	public <T extends NumericType<T>, F extends RandomAccessibleInterval<T>>
		ExtendedRandomAccessibleInterval<T, F> extendZeroView(final F input)
	{
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				Ops.Transform.ExtendZeroView.class, input);
	}

	/**
	 * Return an {@link IterableInterval} having {@link FlatIterationOrder}. If
	 * the passed {@link RandomAccessibleInterval} is already an
	 * {@link IterableInterval} with {@link FlatIterationOrder} then it is
	 * returned directly (this is the case for {@link ArrayImg}). If not, then an
	 * {@link IterableRandomAccessibleInterval} is created.
	 * 
	 * @param input the source
	 * @return an {@link IterableInterval} with {@link FlatIterationOrder}
	 */
	@OpMethod(op = net.imagej.ops.transform.flatIterableView.DefaultFlatIterableView.class)
	public <T> IterableInterval<T> flatIterableView(
		final RandomAccessibleInterval<T> input)
	{
		return (IterableInterval<T>) ops().run(Ops.Transform.FlatIterableView.class, input);
	}

	/**
	 * take a (n-1)-dimensional slice of a n-dimensional view, fixing d-component
	 * of coordinates to pos.
	 * @param input
	 * @param d
	 * @param pos
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.hyperSliceView.DefaultHyperSliceView.class)
	public <T> MixedTransformView<T> hyperSliceView(
		final RandomAccessible<T> input, final int d, final long pos)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.HyperSliceView.class, input, d,
			pos);
	}

	/**
	 * Returns a {@link RealRandomAccessible} using interpolation
	 * 
	 * @param input the {@link EuclideanSpace} to be interpolated
	 * @param factory the {@link InterpolatorFactory} to provide interpolators for
	 *          source
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.interpolateView.DefaultInterpolateView.class)
	public <T, I extends EuclideanSpace> RealRandomAccessible<T>
		interpolateView(final I input, final InterpolatorFactory<T, I> factory)
	{
		return (RealRandomAccessible<T>) ops().run(Ops.Transform.InterpolateView.class, input,
			factory);
	}

	/**
	 * Invert the d-axis.
	 * 
	 * @param input the source
	 * @param d the axis to invert
	 */
	@OpMethod(op = net.imagej.ops.transform.invertAxisView.DefaultInvertAxisView.class)
	public <T> MixedTransformView<T> invertAxisView(
		final RandomAccessible<T> input, final int d)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.InvertAxisView.class, input, d);
	}

	/**
	 * Translate such that pixel at offset in randomAccessible is at the origin in
	 * the resulting view. This is equivalent to translating by -offset.
	 * 
	 * @param input the source
	 * @param offset offset of the source view. The pixel at offset becomes the
	 *          origin of resulting view.
	 */
	@OpMethod(op = net.imagej.ops.transform.offsetView.DefaultOffsetView.class)
	public <T> MixedTransformView<T> offsetView(
		final RandomAccessible<T> input, final long... offset)
	{
		return (MixedTransformView<T>) ops()
			.run(Ops.Transform.OffsetView.class, input, offset);
	}

	/**
	 * Create view with permuted axes. fromAxis and toAxis are swapped. If
	 * fromAxis=0 and toAxis=2, this means that the X-axis of the source view is
	 * mapped to the Z-Axis of the permuted view and vice versa. For a XYZ source,
	 * a ZYX view would be created.
	 * 
	 * @param input
	 * @param fromAxis
	 * @param toAxis
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.permuteView.DefaultPermuteView.class)
	public <T> MixedTransformView<T> permuteView(
		final RandomAccessible<T> input, final int fromAxis, final int toAxis)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.PermuteView.class, input,
			fromAxis, toAxis);
	}

	/**
	 * Inverse bijective permutation of the integer coordinates of one dimension
	 * of a {@link RandomAccessibleInterval}.
	 *
	 * @param input must have dimension(dimension) == permutation.length
	 * @param permutation must be a bijective permutation over its index set, i.e.
	 *          for a lut of length n, the sorted content the array must be
	 *          [0,...,n-1] which is the index set of the lut.
	 * @param d dimension index to be permuted
	 * @return {@link IntervalView} of permuted source.
	 */
	@OpMethod(op = net.imagej.ops.transform.permuteCoordinatesInverseView.PermuteCoordinateInverseViewOfDimension.class)
	public
		<T> IntervalView<T> permuteCoordinatesInverseView(
			final RandomAccessibleInterval<T> input, final int[] permutation,
			final int d)
	{
		return (IntervalView<T>) ops().run(
				Ops.Transform.PermuteCoordinatesView.class, input, permutation, d);
	}

	/**
	 * Inverse Bijective permutation of the integer coordinates in each dimension
	 * of a {@link RandomAccessibleInterval}.
	 *
	 * @param input must be an <em>n</em>-dimensional hypercube with each
	 *          dimension being of the same size as the permutation array
	 * @param permutation must be a bijective permutation over its index set, i.e.
	 *          for a LUT of length n, the sorted content the array must be
	 *          [0,...,n-1] which is the index set of the LUT.
	 * @return {@link IntervalView} of permuted source.
	 */
	@OpMethod(op = net.imagej.ops.transform.permuteCoordinatesInverseView.DefaultPermuteCoordinatesInverseView.class)
	public <T> IntervalView<T> permuteCoordinatesInverseView(
		final RandomAccessibleInterval<T> input, final int... permutation)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.PermuteCoordinatesView.class,
			input, permutation);
	}

	/**
	 * Bijective permutation of the integer coordinates in each dimension of a
	 * {@link RandomAccessibleInterval}.
	 *
	 * @param input must be an <em>n</em>-dimensional hypercube with each
	 *          dimension being of the same size as the permutation array
	 * @param permutation must be a bijective permutation over its index set, i.e.
	 *          for a LUT of length n, the sorted content the array must be
	 *          [0,...,n-1] which is the index set of the LUT.
	 * @return {@link IntervalView} of permuted source.
	 */
	@OpMethod(op = net.imagej.ops.transform.permuteCoordinatesView.DefaultPermuteCoordinatesView.class)
	public <T> IntervalView<T> permuteCoordinatesView(
		final RandomAccessibleInterval<T> input, final int... permutation)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.PermuteCoordinatesView.class, input,
			permutation);
	}

	/**
	 * Bijective permutation of the integer coordinates of one dimension of a
	 * {@link RandomAccessibleInterval}.
	 *
	 * @param input must have dimension(dimension) == permutation.length
	 * @param permutation must be a bijective permutation over its index set, i.e.
	 *          for a lut of length n, the sorted content the array must be
	 *          [0,...,n-1] which is the index set of the lut.
	 * @param d dimension index to be permuted
	 * @return {@link IntervalView} of permuted source.
	 */
	@OpMethod(op = net.imagej.ops.transform.permuteCoordinatesView.PermuteCoordinatesViewOfDimension.class)
	public <T> IntervalView<T> permuteCoordinatesView(
		final RandomAccessibleInterval<T> input, final int[] permutation, int d)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.PermuteCoordinatesView.class,
			input, permutation, d);
	}

	/**
	 * Executes the "project" operation on the given arguments.
	 * @param out
	 * @param in
	 * @param method
	 * @param dim
	 * @return
	 */
	@OpMethod(ops = {
			net.imagej.ops.transform.project.DefaultProjectParallel.class,
			net.imagej.ops.transform.project.ProjectRAIToIterableInterval.class,
			net.imagej.ops.transform.project.ProjectRAIToII.class})
	public <T, V> IterableInterval<V> project(final IterableInterval<V> out,
			final RandomAccessibleInterval<T> in,
			final UnaryComputerOp<Iterable<T>, V> method, final int dim) {
		@SuppressWarnings("unchecked")
		final IterableInterval<V> result = (IterableInterval<V>) ops().run(
				Ops.Transform.Project.class, out, in, method, dim);
		return result;
	}

	/**
	 * Turns a {@link RealRandomAccessible} into a {@link RandomAccessible},
	 * providing {@link RandomAccess} at integer coordinates.
	 * 
	 * @see #interpolateView(net.imglib2.EuclideanSpace,
	 *      net.imglib2.interpolation.InterpolatorFactory)
	 * @param input the {@link RealRandomAccessible} to be rasterized.
	 * @return a {@link RandomAccessibleOnRealRandomAccessible} wrapping source.
	 */
	@OpMethod(op = net.imagej.ops.transform.rasterView.DefaultRasterView.class)
	public <T> RandomAccessibleOnRealRandomAccessible<T> rasterView(
		final RealRandomAccessible<T> input)
	{
		return (RandomAccessibleOnRealRandomAccessible<T>) ops().run(
				Ops.Transform.RasterView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T extends {@link RealType}
	 * &lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional
	 * {@link RandomAccessibleInterval}&lt;{@link RealComposite}&lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link RealComposite RealComposites}
	 */
	@OpMethod(
		op = net.imagej.ops.transform.collapseRealView.DefaultCollapseReal2CompositeIntervalView.class)
	public <T extends Type<T>, R extends RealType<R>>
		CompositeIntervalView<R, RealComposite<R>> collapseRealView(
			final RandomAccessibleInterval<T> input)
	{
		return (CompositeIntervalView<R, RealComposite<R>>) ops().run(
				Ops.Transform.CollapseRealView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessible}&lt;T extends {@link RealType}
	 * &lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional {@link RandomAccessible}
	 * &lt;{@link RealComposite}&lt;T&gt;&gt;
	 * 
	 * @param input the source
	 * @param numChannels the number of channels that the {@link RealComposite}
	 *          will consider when performing calculations
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeView} of
	 *         {@link RealComposite RealComposites}
	 */
	@OpMethod(op = net.imagej.ops.transform.collapseRealView.DefaultCollapseReal2CompositeView.class)
	public <R extends RealType<R>> CompositeView<R, RealComposite<R>>
		collapseRealView(final RandomAccessible<R> input, final int numChannels)
	{
		return (CompositeView<R, RealComposite<R>>) ops().run(
				Ops.Transform.CollapseRealView.class, input, numChannels);
	}

	/**
	 * Executes the "scale" operation on the given arguments.
	 * @param in
	 * @param scaleFactors
	 * @param interpolator
	 * @return
	 */
	@OpMethod(ops = { net.imagej.ops.transform.scaleView.DefaultScaleView.class,
		net.imagej.ops.transform.scaleView.WrappedScaleView.class })
	public <T extends RealType<T>> RandomAccessibleInterval<T> scaleView(
		final RandomAccessibleInterval<T> in, final double[] scaleFactors,
		final InterpolatorFactory<T, RandomAccessible<T>> interpolator)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops().run(
				Ops.Transform.ScaleView.class, in, scaleFactors,
				interpolator);
		return result;
	}
	
	/**
	 * Executes the "scale" operation on the given arguments while preserving
	 * interval bounds.
	 * 
	 * @param in
	 * @param scaleFactors
	 * @param interpolator
	 * @param outOfBoundsFactory
	 * @return
	 */
	@OpMethod(ops = { net.imagej.ops.transform.scaleView.DefaultScaleView.class,
		net.imagej.ops.transform.scaleView.WrappedScaleView.class })
	public <T extends RealType<T>> RandomAccessibleInterval<T> scaleView(final RandomAccessibleInterval<T> in,
			final double[] scaleFactors, final InterpolatorFactory<T, RandomAccessible<T>> interpolator,
			final OutOfBoundsFactory<T, RandomAccessible<T>> outOfBoundsFactory) {
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(Ops.Transform.ScaleView.class, in, scaleFactors, interpolator, outOfBoundsFactory);
		return result;
	}

	/**
	 * Positive shear transform of a RandomAccessible using {@link ShearTransform}
	 * , i.e. c[ shearDimension ] = c[ shearDimension ] + c[ referenceDimension ]
	 *
	 * @param input input, e.g. extended {@link RandomAccessibleInterval}
	 * @param shearDimension dimension to be sheared
	 * @param referenceDimension reference dimension for shear
	 * @return {@link TransformView} containing the result.
	 */
	@OpMethod(op = net.imagej.ops.transform.shearView.DefaultShearView.class)
	public <T extends Type<T>> TransformView<T> shearView(
		final RandomAccessible<T> input, final int shearDimension,
		final int referenceDimension)
	{
		return (TransformView<T>) ops().run(Ops.Transform.ShearView.class, input,
			shearDimension, referenceDimension);
	}

	/**
	 * Positive shear transform of a RandomAccessible using {@link ShearTransform}
	 * , i.e. c[ shearDimension ] = c[ shearDimension ] + c[ referenceDimension ]
	 *
	 * @param input input, e.g. extended {@link RandomAccessibleInterval}
	 * @param interval original interval
	 * @param shearDimension dimension to be sheared
	 * @param referenceDimension reference dimension for shear
	 * @return {@link IntervalView} containing the result. The returned interval's
	 *         dimension are determined by applying the
	 *         {@link ShearTransform#transform} method on the input interval.
	 */
	@OpMethod(op = net.imagej.ops.transform.shearView.ShearViewInterval.class)
	public <T extends Type<T>> IntervalView<T> shearView(
		final RandomAccessible<T> input, final Interval interval,
		final int shearDimension, final int referenceDimension)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.ShearView.class, input, interval,
			shearDimension, referenceDimension);
	}

	/**
	 * Form a <em>(n+1)</em>-dimensional {@link RandomAccessibleInterval} by
	 * stacking <em>n</em>-dimensional {@link RandomAccessibleInterval}s.
	 *
	 * @param input a list of <em>n</em>-dimensional
	 *          {@link RandomAccessibleInterval} of identical sizes.
	 * @return a <em>(n+1)</em>-dimensional {@link RandomAccessibleInterval} where
	 *         the final dimension is the index of the hyperslice.
	 */
	@OpMethod(op = net.imagej.ops.transform.stackView.DefaultStackView.class)
	public <T extends Type<T>> RandomAccessibleInterval<T> stackView(
		final List<RandomAccessibleInterval<T>> input)
	{
		return (RandomAccessibleInterval<T>) ops().run(Ops.Transform.StackView.class, input);
	}

	/**
	 * Form a <em>(n+1)</em>-dimensional {@link RandomAccessibleInterval} by
	 * stacking <em>n</em>-dimensional {@link RandomAccessibleInterval}s.
	 *
	 * @param stackAccessMode describes how a {@link RandomAccess} on the
	 *          <em>(n+1)</em> -dimensional {@link StackView} maps position
	 *          changes into position changes of the underlying <em>n</em>
	 *          -dimensional {@link RandomAccess}es.
	 * @param input a list of <em>n</em>-dimensional
	 *          {@link RandomAccessibleInterval} of identical sizes.
	 * @return a <em>(n+1)</em>-dimensional {@link RandomAccessibleInterval} where
	 *         the final dimension is the index of the hyperslice.
	 */
	@OpMethod(op = net.imagej.ops.transform.stackView.StackViewWithAccessMode.class)
	public <T> RandomAccessibleInterval<T> stackView(
		final List<RandomAccessibleInterval<T>> input,
		final StackAccessMode stackAccessMode)
	{
		return (RandomAccessibleInterval<T>) ops().run(Ops.Transform.StackView.class,
			input, stackAccessMode);
	}

	/**
	 * Sample only every <em>step</em><sup>th</sup> value of a source
	 * {@link RandomAccessible}. This is effectively an integer scaling
	 * transformation.
	 * 
	 * @param input the source
	 * @param step the subsampling step size
	 * @return a subsampled {@link RandomAccessible}
	 */
	@OpMethod(op = net.imagej.ops.transform.subsampleView.DefaultSubsampleView.class)
	public <T> SubsampleView<T> subsampleView(
		final RandomAccessible<T> input, final long step)
	{
		return (SubsampleView<T>) ops().run(Ops.Transform.SubsampleView.class, input, step);
	}

	/**
	 * Translate the source view by the given translation vector. Pixel <em>x</em>
	 * in the source view has coordinates <em>(x + translation)</em> in the
	 * resulting view.
	 * 
	 * @param input the source
	 * @param translation translation vector of the source view. The pixel at
	 *          <em>x</em> in the source view becomes <em>(x + translation)</em>
	 *          in the resulting view.
	 */
	@OpMethod(op = net.imagej.ops.transform.translateView.DefaultTranslateView.class)
	public <T> MixedTransformView<T> translateView(
		final RandomAccessible<T> input, final long... translation)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.TranslateView.class, input,
			translation);
	}

	/**
	 * Negative shear transform of a RandomAccessible using
	 * {@link InverseShearTransform}, i.e. c[ shearDimension ] = c[ shearDimension
	 * ] - c[ referenceDimension ]
	 *
	 * @param input input, e.g. extended {@link RandomAccessibleInterval}
	 * @param shearDimension dimension to be sheared
	 * @param referenceDimension reference dimension for shear
	 * @return {@link TransformView} containing the result.
	 */
	@OpMethod(op = net.imagej.ops.transform.unshearView.DefaultUnshearView.class)
	public <T> TransformView<T> unshearView(
		final RandomAccessible<T> input, final int shearDimension,
		final int referenceDimension)
	{
		return (TransformView<T>) ops().run(Ops.Transform.UnshearView.class, input,
			shearDimension, referenceDimension);
	}

	/**
	 * Negative shear transform of a RandomAccessible using
	 * {@link InverseShearTransform}, i.e. c[ shearDimension ] = c[ shearDimension
	 * ] - c[ referenceDimension ]
	 *
	 * @param input input, e.g. extended {@link RandomAccessibleInterval}
	 * @param interval original interval
	 * @param shearDimension dimension to be sheared
	 * @param referenceDimension reference dimension for shear
	 * @return {@link IntervalView} containing the result. The returned interval's
	 *         dimension are determined by applying the
	 *         {@link ShearTransform#transform} method on the input interval.
	 */
	@OpMethod(op = net.imagej.ops.transform.unshearView.UnshearViewInterval.class)
	public <T> IntervalView<T> unshearView(
		final RandomAccessible<T> input, final Interval interval,
		final int shearDimension, final int referenceDimension)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.UnshearView.class, input, interval,
			shearDimension, referenceDimension);
	}

	/**
	 * Define an interval on a RandomAccessible. It is the callers responsibility
	 * to ensure that the source RandomAccessible is defined in the specified
	 * interval.
	 * 
	 * @param input the source
	 * @param interval interval boundaries.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.transform.intervalView.DefaultIntervalView.class)
	public <T> IntervalView<T> intervalView(
		final RandomAccessible<T> input, final Interval interval)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.IntervalView.class, input, interval);
	}

	/**
	 * Translate the source such that the upper left corner is at the origin
	 * 
	 * @param input the source.
	 * @return view of the source translated to the origin
	 */
	@OpMethod(op = net.imagej.ops.transform.zeroMinView.DefaultZeroMinView.class)
	public <T> IntervalView<T> zeroMinView(
		final RandomAccessibleInterval<T> input)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.ZeroMinView.class, input);
	}

	/**
	 * Define an interval on a RandomAccessible and translate it such that the min
	 * corner is at the origin. It is the callers responsibility to ensure that
	 * the source RandomAccessible is defined in the specified interval.
	 * 
	 * @param input the source
	 * @param interval the interval on source that should be cut out and
	 *          translated to the origin.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.transform.offsetView.OffsetViewInterval.class)
	public <T> IntervalView<T> offsetView(
		final RandomAccessible<T> input, final Interval interval)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.OffsetView.class, input, interval);
	}

	/**
	 * Define an interval on a RandomAccessible and translate it such that the min
	 * corner is at the origin. It is the callers responsibility to ensure that
	 * the source RandomAccessible is defined in the specified interval.
	 * 
	 * @param input the source
	 * @param offset offset of min corner.
	 * @param dimension size of the interval.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.transform.offsetView.OffsetViewOriginSize.class)
	public <T> IntervalView<T> offsetView(
		final RandomAccessible<T> input, final long[] offset,
		final long... dimension)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.OffsetView.class, input, offset,
			dimension);
	}

	/**
	 * Create view that is rotated by 90 degrees. The rotation is specified by the
	 * fromAxis and toAxis arguments. If fromAxis=0 and toAxis=1, this means that
	 * the X-axis of the source view is mapped to the Y-Axis of the rotated view.
	 * That is, it corresponds to a 90 degree clock-wise rotation of the source
	 * view in the XY plane. fromAxis=1 and toAxis=0 corresponds to a
	 * counter-clock-wise rotation in the XY plane.
	 * 
	 * @param input
	 * @param fromAxis
	 * @param toAxis
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.transform.rotateView.DefaultRotateView.class)
	public <T> MixedTransformView<T> rotateView(
		final RandomAccessible<T> input, final int fromAxis, final int toAxis)
	{
		return (MixedTransformView<T>) ops().run(Ops.Transform.RotateView.class, input,
			fromAxis, toAxis);
	}

	/**
	 * Sample only every <em>step<sub>d</sub></em><sup>th</sup> value of a source
	 * {@link RandomAccessible}. This is effectively an integer scaling
	 * transformation.
	 * 
	 * @param input the source
	 * @param steps the subsampling step sizes
	 * @return a subsampled {@link RandomAccessible}
	 */
	@OpMethod(op = net.imagej.ops.transform.subsampleView.SubsampleViewStepsForDims.class)
	public <T> SubsampleView<T> subsampleView(
		final RandomAccessible<T> input, final long... steps)
	{
		return (SubsampleView<T>) ops().run(Ops.Transform.SubsampleView.class, input,
			steps);
	}

	/**
	 * Define an interval on a RandomAccessible. It is the callers responsibility
	 * to ensure that the source RandomAccessible is defined in the specified
	 * interval.
	 * 
	 * @param input the source
	 * @param min lower bound of interval
	 * @param max upper bound of interval
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.transform.intervalView.IntervalViewMinMax.class)
	public <T> IntervalView<T> intervalView(
		final RandomAccessible<T> input, final long[] min, final long... max)
	{
		return (IntervalView<T>) ops().run(Ops.Transform.IntervalView.class, input, min, max);
	}

	@Override
	public String getName() {
		return "transform";
	}

	/**
	 * Concatenate {@link List} of {@link RandomAccessibleInterval} along the
	 * specified axis.
	 *
	 * @param source
	 *            a list of <em>n</em>-dimensional
	 *            {@link RandomAccessibleInterval} with same size in every
	 *            dimension except for the concatenation dimension.
	 * @param concatenationAxis
	 *            Concatenate along this dimension.
	 * @return <em>n</em>-dimensional {@link RandomAccessibleInterval}. The size
	 *         of the concatenation dimension is the sum of sizes of all sources
	 *         in that dimension.
	 *
	 */
	@OpMethod( op = net.imagej.ops.transform.concatenateView.DefaultConcatenateView.class )
	public < T extends Type< T > > RandomAccessibleInterval< T > concatenateView(
			final List< ? extends RandomAccessibleInterval< T > > source,
					final int concatenationAxis )
	{
		return ( RandomAccessibleInterval< T > ) ops().run( DefaultConcatenateView.class, source, concatenationAxis );
	}

	/**
	 * Concatenate {@link List} of {@link RandomAccessibleInterval} along the
	 * specified axis.
	 *
	 * @param source
	 *            a list of <em>n</em>-dimensional
	 *            {@link RandomAccessibleInterval} with same size in every
	 *            dimension except for the concatenation dimension.
	 * @param concatenationAxis
	 *            Concatenate along this dimension.
	 * @return <em>n</em>-dimensional {@link RandomAccessibleInterval}. The size
	 *         of the concatenation dimension is the sum of sizes of all sources
	 *         in that dimension.
	 *
	 */
	@OpMethod( op = net.imagej.ops.transform.concatenateView.ConcatenateViewWithAccessMode.class )
	public < T extends Type< T > > RandomAccessibleInterval< T > concatenateView(
			final List< ? extends RandomAccessibleInterval< T > > source,
					final int concatenationAxis,
					final StackAccessMode mode )
	{
		return ( RandomAccessibleInterval< T > ) ops().run( ConcatenateViewWithAccessMode.class, source, concatenationAxis, mode );
	}

}
