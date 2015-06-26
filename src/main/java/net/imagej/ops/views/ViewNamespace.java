package net.imagej.ops.views;

import org.junit.internal.runners.statements.RunAfters;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;
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
import net.imglib2.type.Type;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.IterableRandomAccessibleInterval;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;
import net.imglib2.view.SubsampleView;
import net.imglib2.view.TransformView;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;
import net.imglib2.view.composite.NumericComposite;
import net.imglib2.view.composite.RealComposite;

public class ViewNamespace<T, L extends Type<L>, N extends NumericType<N>, F extends RandomAccessibleInterval<T>, I extends EuclideanSpace, R extends RealType<R>>
		extends AbstractNamespace {

	/**
	 * Create view which adds a dimension to the source
	 * {@link RandomAccessibleInterval}. The {@link Interval} boundaries in the
	 * additional dimension are set to the specified values.
	 * 
	 * The additional dimension is the last dimension. For example, an XYZ view
	 * is created for an XY source. When accessing an XYZ sample in the view,
	 * the final coordinate is discarded and the source XY sample is accessed.
	 * 
	 * @param interval
	 *            the source
	 * @param minOfNewDim
	 *            Interval min in the additional dimension.
	 * @param maxOfNewDim
	 *            Interval max in the additional dimension.
	 */
	@OpMethod(op = net.imagej.ops.views.AddDimensionMinMax.class)
	public IntervalView<T> addDimension(
			final RandomAccessibleInterval<T> input, final long min,
			final long max) {
		return (IntervalView<T>) ops().run(
				net.imagej.ops.views.AddDimensionMinMax.class, input, min, max);
	}

	/**
	 * Create view which adds a dimension to the source {@link RandomAccessible}
	 * .
	 * 
	 * The additional dimension is the last dimension. For example, an XYZ view
	 * is created for an XY source. When accessing an XYZ sample in the view,
	 * the final coordinate is discarded and the source XY sample is accessed.
	 * 
	 * @param randomAccessible
	 *            the source
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultAddDimension.class)
	public MixedTransformView<T> addDimension(final RandomAccessible<T> input) {
		return (MixedTransformView<T>) ops().run(DefaultAddDimension.class,
				input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T&gt; into an (
	 * <em>n</em>-1)-dimensional {@link RandomAccessibleInterval}&lt;
	 * {@link GenericComposite}&lt;T&gt;&gt;
	 * 
	 * @param source
	 *            the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link GenericComposite GenericComposites}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultCollapse2CompositeIntervalView.class)
	public CompositeIntervalView<T, ? extends GenericComposite<T>> collapse(
			final RandomAccessibleInterval<T> input) {
		return (CompositeIntervalView<T, ? extends GenericComposite<T>>) ops()
				.run(DefaultCollapse2CompositeIntervalView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessible}&lt;T&gt; into an (<em>n</em>
	 * -1)-dimensional {@link RandomAccessible}&lt;{@link GenericComposite}
	 * &lt;T&gt;&gt;
	 * 
	 * @param source
	 *            the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeView} of
	 *         {@link GenericComposite GenericComposites}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultCollapse2CompositeView.class)
	public CompositeView<T, ? extends GenericComposite<T>> collapse(
			final RandomAccessible<T> input) {
		return (CompositeView<T, ? extends GenericComposite<T>>) ops().run(
				DefaultCollapse2CompositeView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T extends
	 * {@link NumericType}&lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional
	 * {@link RandomAccessibleInterval}&lt;{@link NumericComposite}&lt;T&gt;&gt;
	 * 
	 * @param source
	 *            the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link NumericComposite NumericComposites}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultCollapseNummeric2CompositeView.class)
	public CompositeView<N, NumericComposite<N>> collapseNumeric(
			final RandomAccessible<N> input, final int numChannels) {
		return (CompositeView<N, NumericComposite<N>>) ops().run(
				DefaultCollapseNummeric2CompositeView.class, input, numChannels);
	}

	/**
	 * Removes all unit dimensions (dimensions with size one) from the
	 * RandomAccessibleInterval
	 * 
	 * @param source
	 *            the source
	 * @return a RandomAccessibleInterval without dimensions of size one
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultDropSingletonDimensions.class)
	public RandomAccessibleInterval<T> dropSingletonDimensions(
			final RandomAccessibleInterval<T> input) {
		return (RandomAccessibleInterval<T>) ops().run(
				DefaultDropSingletonDimensions.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with an out-of-bounds strategy.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @param factory
	 *            the out-of-bounds strategy.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtend.class)
	public ExtendedRandomAccessibleInterval<T, F> extend(final F input,
			final OutOfBoundsFactory<T, ? super F> factory) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtend.class, input, factory);
	}

	/**
	 * Extend a RandomAccessibleInterval with an out-of-bounds strategy to
	 * repeat border pixels.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsBorder
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendBorder.class)
	public ExtendedRandomAccessibleInterval<T, F> extendBorder(final F input) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendBorder.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a mirroring out-of-bounds
	 * strategy. Boundary pixels are repeated.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsMirrorDoubleBoundary
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendMirrorDouble.class)
	public ExtendedRandomAccessibleInterval<T, F> extendMirrorDouble(
			final F input) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendMirrorDouble.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a mirroring out-of-bounds
	 * strategy. Boundary pixels are not repeated. Note that this requires that
	 * all dimensions of the source (F source) must be &gt; 1.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsMirrorSingleBoundary
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendMirrorSingle.class)
	public ExtendedRandomAccessibleInterval<T, F> extendMirrorSingle(
			final F input) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendMirrorSingle.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a periodic out-of-bounds strategy.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsPeriodic
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendPeriodic.class)
	public ExtendedRandomAccessibleInterval<T, F> extendPeriodic(final F input) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendPeriodic.class, input);
	}

	/**
	 * Extend a RandomAccessibleInterval with a random-value out-of-bounds
	 * strategy.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @param min
	 *            the minimal random value
	 * @param max
	 *            the maximal random value
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsRandomValue
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendRandom.class)
	public ExtendedRandomAccessibleInterval<T, F> extendRandom(final F input,
			final double min, final double max) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendRandom.class, input, min, max);
	}

	/**
	 * Extend a RandomAccessibleInterval with a constant-value out-of-bounds
	 * strategy.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity.
	 * @see net.imglib2.outofbounds.OutOfBoundsConstantValue
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendValue.class)
	public ExtendedRandomAccessibleInterval<T, F> extendValue(final F input,
			final L value) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendValue.class, input, value);
	}

	/**
	 * Extend a RandomAccessibleInterval with a constant-value out-of-bounds
	 * strategy where the constant value is the zero-element of the data type.
	 * 
	 * @param source
	 *            the interval to extend.
	 * @return (unbounded) RandomAccessible which extends the input interval to
	 *         infinity with a constant value of zero.
	 * @see net.imglib2.outofbounds.OutOfBoundsConstantValue
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultExtendZero.class)
	public ExtendedRandomAccessibleInterval<T, F> extendZero(final F input) {
		return (ExtendedRandomAccessibleInterval<T, F>) ops().run(
				DefaultExtendZero.class, input);
	}

	/**
	 * Return an {@link IterableInterval} having {@link FlatIterationOrder}. If
	 * the passed {@link RandomAccessibleInterval} is already an
	 * {@link IterableInterval} with {@link FlatIterationOrder} then it is
	 * returned directly (this is the case for {@link ArrayImg}). If not, then
	 * an {@link IterableRandomAccessibleInterval} is created.
	 * 
	 * @param randomAccessibleInterval
	 *            the source
	 * @return an {@link IterableInterval} with {@link FlatIterationOrder}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultFlatIterable.class)
	public IterableInterval<T> flatIterable(
			final RandomAccessibleInterval<T> input) {
		return (IterableInterval<T>) ops()
				.run(DefaultFlatIterable.class, input);
	}

	/**
	 * take a (n-1)-dimensional slice of a n-dimensional view, fixing
	 * d-component of coordinates to pos.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultHyperSlice.class)
	public MixedTransformView<T> hyperSlice(final RandomAccessible<T> input,
			final int d, final long pos) {
		return (MixedTransformView<T>) ops().run(DefaultHyperSlice.class,
				input, d, pos);
	}

	/**
	 * Returns a {@link RealRandomAccessible} using interpolation
	 * 
	 * @param source
	 *            the {@link EuclideanSpace} to be interpolated
	 * @param factory
	 *            the {@link InterpolatorFactory} to provide interpolators for
	 *            source
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultInterpolate.class)
	public RealRandomAccessible<T> interpolate(final I input,
			final InterpolatorFactory<T, I> factory) {
		return (RealRandomAccessible<T>) ops().run(DefaultInterpolate.class,
				input, factory);
	}

	/**
	 * Invert the d-axis.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param d
	 *            the axis to invert
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultInvertAxis.class)
	public MixedTransformView<T> invertAxis(final RandomAccessible<T> input,
			final int d) {
		return (MixedTransformView<T>) ops().run(DefaultInvertAxis.class,
				input, d);
	}

	/**
	 * test whether the source interval starts at (0,0,...,0)
	 * 
	 * @param interval
	 *            - the {@link Interval} to test
	 * @return true if zero-bounded, false otherwise
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultIsZeroMin.class)
	public Boolean isZeroMin(Interval input) {
		return (Boolean) ops().run(DefaultIsZeroMin.class, input);
	}

	/**
	 * Translate such that pixel at offset in randomAccessible is at the origin
	 * in the resulting view. This is equivalent to translating by -offset.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param offset
	 *            offset of the source view. The pixel at offset becomes the
	 *            origin of resulting view.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultOffset.class)
	public MixedTransformView<T> offset(final RandomAccessible<T> input,
			final long... offset) {
		return (MixedTransformView<T>) ops().run(DefaultOffset.class, input,
				offset);
	}

	/**
	 * Create view with permuted axes. fromAxis and toAxis are swapped.
	 * 
	 * If fromAxis=0 and toAxis=2, this means that the X-axis of the source view
	 * is mapped to the Z-Axis of the permuted view and vice versa. For a XYZ
	 * source, a ZYX view would be created.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultPermute.class)
	public MixedTransformView<T> permute(final RandomAccessible<T> input,
			final int fromAxis, final int toAxis) {
		return (MixedTransformView<T>) ops().run(DefaultPermute.class, input,
				fromAxis, toAxis);
	}

	/**
	 * Not supported right now, because imagej-ops uses an old version of
	 * imglib2.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultPermuteCoordinatesInverse.class)
	public IntervalView<T> permuteCoordinatesInverse(
			final RandomAccessibleInterval<T> input, final int[] permutation,
			final int d) {
		return (IntervalView<T>) ops().run(
				DefaultPermuteCoordinatesInverse.class, input, permutation, d);
	}

	/**
	 * Not supported right now, because imagej-ops uses an old version of
	 * imglib2.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultPermuteCoordinates.class)
	public IntervalView<T> permuteCoordinates(
			final RandomAccessibleInterval<T> input, final int... permutation) {
		return (IntervalView<T>) ops().run(DefaultPermuteCoordinates.class,
				input, permutation);
	}

	/**
	 * Turns a {@link RealRandomAccessible} into a {@link RandomAccessible},
	 * providing {@link RandomAccess} at integer coordinates.
	 * 
	 * @see #interpolate(net.imglib2.EuclideanSpace,
	 *      net.imglib2.interpolation.InterpolatorFactory)
	 * 
	 * @param source
	 *            the {@link RealRandomAccessible} to be rasterized.
	 * @return a {@link RandomAccessibleOnRealRandomAccessible} wrapping source.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultRaster.class)
	public RandomAccessibleOnRealRandomAccessible<T> raster(
			final RealRandomAccessible<T> input) {
		return (RandomAccessibleOnRealRandomAccessible<T>) ops().run(
				DefaultRaster.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessibleInterval}&lt;T extends
	 * {@link RealType}&lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional
	 * {@link RandomAccessibleInterval}&lt;{@link RealComposite}&lt;T&gt;&gt;
	 * 
	 * @param source
	 *            the source
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeIntervalView} of
	 *         {@link RealComposite RealComposites}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultRealCollapse2CompositeIntervalView.class)
	public CompositeIntervalView<R, RealComposite<R>> collapseReal(
			final RandomAccessibleInterval<T> input) {
		return (CompositeIntervalView<R, RealComposite<R>>) ops().run(
				DefaultRealCollapse2CompositeIntervalView.class, input);
	}

	/**
	 * Collapse the <em>n</em><sup>th</sup> dimension of an <em>n</em>
	 * -dimensional {@link RandomAccessible}&lt;T extends {@link RealType}
	 * &lt;T&gt;&gt; into an (<em>n</em>-1)-dimensional {@link RandomAccessible}
	 * &lt;{@link RealComposite}&lt;T&gt;&gt;
	 * 
	 * @param source
	 *            the source
	 * @param numChannels
	 *            the number of channels that the {@link RealComposite} will
	 *            consider when performing calculations
	 * @return an (<em>n</em>-1)-dimensional {@link CompositeView} of
	 *         {@link RealComposite RealComposites}
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultRealCollapse2CompositeView.class)
	public CompositeView<R, RealComposite<R>> collapseReal(
			final RandomAccessible<R> input, final int numChannels) {
		return (CompositeView<R, RealComposite<R>>) ops().run(
				DefaultRealCollapse2CompositeView.class, input, numChannels);
	}

	/**
	 * Not supported right now, because imagej-ops uses an old version of
	 * imglib2.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultShear.class)
	public TransformView<T> shear(final RandomAccessible<T> input,
			final int shearDimension, final int referenceDimension) {
		return (TransformView<T>) ops().run(DefaultShear.class, input,
				shearDimension, referenceDimension);
	}

	/**
	 * Not supported right now, because imagej-ops uses an old version of
	 * imglib2.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultStack.class)
	public RandomAccessibleInterval<T> stack(
			final RandomAccessibleInterval<T> input) {
		return (RandomAccessibleInterval<T>) ops().run(DefaultStack.class,
				input);
	}

	/**
	 * Sample only every <em>step</em><sup>th</sup> value of a source
	 * {@link RandomAccessible}. This is effectively an integer scaling
	 * transformation.
	 * 
	 * @param source
	 *            the source
	 * @param step
	 *            the subsampling step size
	 * @return a subsampled {@link RandomAccessible}
	 * 
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultSubsample.class)
	public SubsampleView<T> subsample(final RandomAccessible<T> input,
			final long step) {
		return (SubsampleView<T>) ops()
				.run(DefaultSubsample.class, input, step);
	}

	/**
	 * Translate the source view by the given translation vector. Pixel
	 * <em>x</em> in the source view has coordinates <em>(x + translation)</em>
	 * in the resulting view.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param translation
	 *            translation vector of the source view. The pixel at <em>x</em>
	 *            in the source view becomes <em>(x + translation)</em> in the
	 *            resulting view.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultTranslate.class)
	public MixedTransformView<T> translate(final RandomAccessible<T> input,
			final long... translation) {
		return (MixedTransformView<T>) ops().run(DefaultTranslate.class, input,
				translation);
	}

	/**
	 * Not supported right now, because imagej-ops uses an old version of
	 * imglib2.
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultUnshear.class)
	public TransformView<T> unshear(final RandomAccessible<T> input,
			final int shearDimension, final int referenceDimension) {
		return (TransformView<T>) ops().run(DefaultUnshear.class, input,
				shearDimension, referenceDimension);
	}

	/**
	 * Define an interval on a RandomAccessible. It is the callers
	 * responsibility to ensure that the source RandomAccessible is defined in
	 * the specified interval.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param interval
	 *            interval boundaries.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultView.class)
	public IntervalView<T> view(final RandomAccessible<T> input,
			final Interval interval) {
		return (IntervalView<T>) ops().run(DefaultView.class, input, interval);
	}

	/**
	 * Translate the source such that the upper left corner is at the origin
	 * 
	 * @param interval
	 *            the source.
	 * @return view of the source translated to the origin
	 */
	@OpMethod(op = net.imagej.ops.views.DefaultZeroMin.class)
	public IntervalView<T> zeroMin(final RandomAccessibleInterval<T> input) {
		return (IntervalView<T>) ops().run(DefaultZeroMin.class, input);
	}

	/**
	 * Define an interval on a RandomAccessible and translate it such that the
	 * min corner is at the origin. It is the callers responsibility to ensure
	 * that the source RandomAccessible is defined in the specified interval.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param interval
	 *            the interval on source that should be cut out and translated
	 *            to the origin.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.views.OffsetInterval.class)
	public IntervalView<T> offsetInterval(final RandomAccessible<T> input,
			final Interval interval) {
		return (IntervalView<T>) ops().run(OffsetInterval.class, input,
				interval);
	}

	/**
	 * Define an interval on a RandomAccessible and translate it such that the
	 * min corner is at the origin. It is the callers responsibility to ensure
	 * that the source RandomAccessible is defined in the specified interval.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param offset
	 *            offset of min corner.
	 * @param dimension
	 *            size of the interval.
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.views.OffsetOriginSize.class)
	public IntervalView<T> offsetInterval(final RandomAccessible<T> input,
			final long[] offset, final long[] dimension) {
		return (IntervalView<T>) ops().run(OffsetOriginSize.class, input,
				offset, dimension);
	}

	/**
	 * Create view that is rotated by 90 degrees. The rotation is specified by
	 * the fromAxis and toAxis arguments.
	 * 
	 * If fromAxis=0 and toAxis=1, this means that the X-axis of the source view
	 * is mapped to the Y-Axis of the rotated view. That is, it corresponds to a
	 * 90 degree clock-wise rotation of the source view in the XY plane.
	 * 
	 * fromAxis=1 and toAxis=0 corresponds to a counter-clock-wise rotation in
	 * the XY plane.
	 */
	@OpMethod(op = net.imagej.ops.views.RotateAroundAxis.class)
	public MixedTransformView<T> rotate(final RandomAccessible<T> input,
			final int fromAxis, final int toAxis) {
		return (MixedTransformView<T>) ops().run(RotateAroundAxis.class, input,
				fromAxis, toAxis);
	}

	/**
	 * Sample only every <em>step<sub>d</sub></em><sup>th</sup> value of a
	 * source {@link RandomAccessible}. This is effectively an integer scaling
	 * transformation.
	 * 
	 * @param source
	 *            the source
	 * @param steps
	 *            the subsampling step sizes
	 * @return a subsampled {@link RandomAccessible}
	 * 
	 */
	@OpMethod(op = net.imagej.ops.views.SubsampleStepsForDims.class)
	public SubsampleView<T> subsample(final RandomAccessible<T> input,
			final long... steps) {
		return (SubsampleView<T>) ops().run(SubsampleStepsForDims.class, input,
				steps);
	}

	/**
	 * Define an interval on a RandomAccessible. It is the callers
	 * responsibility to ensure that the source RandomAccessible is defined in
	 * the specified interval.
	 * 
	 * @param randomAccessible
	 *            the source
	 * @param min
	 *            lower bound of interval
	 * @param max
	 *            upper bound of interval
	 * @return a RandomAccessibleInterval
	 */
	@OpMethod(op = net.imagej.ops.views.ViewMinMax.class)
	public IntervalView<T> view(final RandomAccessible<T> input,
			final long[] min, final long... max) {
		return (IntervalView<T>) ops().run(ViewMinMax.class, input, min, max);
	}

	@Override
	public String getName() {
		return "view";
	}

}
