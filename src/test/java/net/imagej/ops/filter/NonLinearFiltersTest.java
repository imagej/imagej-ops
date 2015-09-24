
package net.imagej.ops.filter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.filter.max.DefaultMaxFilter;
import net.imagej.ops.filter.max.MaxFilterOp;
import net.imagej.ops.filter.mean.DefaultMeanFilter;
import net.imagej.ops.filter.mean.MeanFilterOp;
import net.imagej.ops.filter.median.DefaultMedianFilter;
import net.imagej.ops.filter.median.MedianFilterOp;
import net.imagej.ops.filter.min.DefaultMinFilter;
import net.imagej.ops.filter.min.MinFilterOp;
import net.imagej.ops.filter.sigma.DefaultSigmaFilter;
import net.imagej.ops.filter.sigma.SigmaFilterOp;
import net.imagej.ops.filter.variance.DefaultVarianceFilter;
import net.imagej.ops.filter.variance.VarianceFilterOp;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.RectangleShape.NeighborhoodsIterableInterval;
import net.imglib2.img.Img;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests implementations of {@link MaxFilterOp}, {@link MeanFilterOp},
 * {@link MedianFilterOp}, {@link MinFilterOp}, {@link SigmaFilterOp},
 * {@link VarianceFilterOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */
public class NonLinearFiltersTest extends AbstractOpTest {

	Img<ByteType> in;
	Img<ByteType> out;
	RectangleShape shape;
	OutOfBoundsMirrorFactory<ByteType, Img<ByteType>> oobFactory =
		new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE);

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteTestImg(true, new long[] { 10, 10 });
		out = generateByteTestImg(false, new long[] { 10, 10 });
		shape = new RectangleShape(1, false);
	}

	/**
	 * @see MaxFilterOp
	 * @see DefaultMaxFilter
	 */
	@Test
	public void testMaxFilter() {
		ops.run(MaxFilterOp.class, out, in, shape, oobFactory);

		byte max = Byte.MIN_VALUE;

		NeighborhoodsIterableInterval<ByteType> neighborhoods =
			shape.neighborhoods(Views.interval(Views.extendMirrorSingle(in), in));
		for (ByteType t : neighborhoods.firstElement()) {
			max = (byte) Math.max(t.getInteger(), max);
		}
		assertEquals(out.firstElement().get(), max);
	}

	/**
	 * @see MeanFilterOp
	 * @see DefaultMeanFilter
	 */
	@Test
	public void testMeanFilter() {
		ops.run(MeanFilterOp.class, out, in, shape, oobFactory);

		double sum = 0.0;

		NeighborhoodsIterableInterval<ByteType> neighborhoods =
			shape.neighborhoods(Views.interval(Views.extendMirrorSingle(in), in));
		for (ByteType t : neighborhoods.firstElement()) {
			sum += t.getRealDouble();
		}

		assertEquals(Util.round(sum / 9.0), out.firstElement().get());
	}

	/**
	 * @see MedianFilterOp
	 * @see DefaultMedianFilter
	 */
	@Test
	public void testMedianFilter() {
		ops.run(MedianFilterOp.class, out, in, shape, oobFactory);

		ArrayList<ByteType> items = new ArrayList<ByteType>();
		NeighborhoodsIterableInterval<ByteType> neighborhoods =
			shape.neighborhoods(Views.interval(Views.extendMirrorSingle(in), in));
		for (ByteType t : neighborhoods.firstElement()) {
			items.add(t.copy());
		}

		Collections.sort(items);

		assertEquals(items.get(5).get(), out.firstElement().get());
	}

	/**
	 * @see MinFilterOp
	 * @see DefaultMinFilter
	 */
	@Test
	public void testMinFilter() {
		ops.run(MinFilterOp.class, out, in, shape, oobFactory);

		byte min = Byte.MAX_VALUE;

		NeighborhoodsIterableInterval<ByteType> neighborhoods =
			shape.neighborhoods(Views.interval(Views.extendMirrorSingle(in), in));
		for (ByteType t : neighborhoods.firstElement()) {
			min = (byte) Math.min(t.getInteger(), min);
		}
		assertEquals(min, out.firstElement().get());
	}

	/**
	 * @see SigmaFilterOp
	 * @see DefaultSigmaFilter
	 */
	@Test
	public void testSigmaFilter() {
		ops.run(SigmaFilterOp.class, out, in, shape, oobFactory, 1.0, 0.0);
	}

	/**
	 * @see VarianceFilterOp
	 * @see DefaultVarianceFilter
	 */
	@Test
	public void testVarianceFilter() {
		ops.run(VarianceFilterOp.class, out, in, shape, oobFactory);

		double sum = 0.0;
		double sumSq = 0.0;

		NeighborhoodsIterableInterval<ByteType> neighborhoods =
			shape.neighborhoods(Views.interval(Views.extendMirrorSingle(in), in));
		for (ByteType t : neighborhoods.firstElement()) {
			sum += t.getRealDouble();
			sumSq += t.getRealDouble()*t.getRealDouble();
		}

		assertEquals((byte)Util.round((sumSq - (sum * sum / 9)) / 8), out.firstElement().get());
	}

}
