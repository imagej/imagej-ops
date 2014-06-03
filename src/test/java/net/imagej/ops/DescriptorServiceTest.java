package net.imagej.ops;

import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.descriptorsets.HaralickDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.HistogramDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.ZernikeDescriptorSet;
import net.imagej.ops.histogram.CooccurrenceMatrix.MatrixOrientation;
import net.imglib2.IterableInterval;
import net.imglib2.Pair;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.module.ModuleException;

public class DescriptorServiceTest extends AbstractOpTest {

	private Img<UnsignedByteType> in;

	private Img<UnsignedByteType> in2;

	@Before
	public void init() {
		in = generateUnsignedByteTestImg(true, 10, 10);
		in2 = generateUnsignedByteTestImg(true, 100, 100);
	}

	/** Subclasses can override to create a context with different services. */
	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
				DescriptorService.class);
	}

	// @Test
	// public void firstOrderStatisticsTest() {
	//
	// final FirstOrderStatisticsSet set = new FirstOrderStatisticsSet(context);
	//
	// // make it ready for
	// set.compileFor(in.getClass());
	//
	// // First image
	// set.update(in);
	//
	// for (final Pair<String, Double> res : set) {
	// System.out.println("First: " + res.getA() + " " + res.getB());
	// }
	//
	// // Second image
	// set.update(in2);
	// for (final Pair<String, Double> res : set) {
	// System.out.println("Second: " + res.getA() + " " + res.getB());
	// }
	//
	// }

	@SuppressWarnings("rawtypes")
	@Test
	public void haralickTest() throws IllegalArgumentException, ModuleException {

		final HaralickDescriptorSet<IterableInterval> desc = new HaralickDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		desc.compile();
		desc.update(in);

		desc.updateParameters(32, 1, MatrixOrientation.VERTICAL);

		System.out.println("### FIRST RUN ####");
		for (final Pair<String, DoubleType> res : desc) {
			System.out.println(res.getA() + " " + res.getB().get());
		}

		System.out.println("\n");
		desc.updateParameters(32, 1, MatrixOrientation.HORIZONTAL);

		System.out.println("### SECOND RUN ####");
		for (final Pair<String, DoubleType> res : desc) {
			System.out.println(res.getA() + " " + res.getB().get());
		}
	}

	@Test
	public void histogramTest() throws IllegalArgumentException,
			ModuleException {

		final HistogramDescriptorSet<IterableInterval> desc = new HistogramDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		desc.compile();

		desc.updateNumBins(10);
		desc.update(in);

		for (final Pair<String, DoubleType> res : desc) {
			System.out.println("First: " + res.getA() + " " + res.getB().get());
		}
	}

	@Test
	public void zernikeTest() throws IllegalArgumentException, ModuleException {

		@SuppressWarnings("rawtypes")
		final ZernikeDescriptorSet<IterableInterval> desc = new ZernikeDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		desc.compile();

		desc.updateOrder(1);
		desc.update(in);

		for (final Pair<String, DoubleType> res : desc) {
			System.out.println(res.getA() + " " + res.getB().get());
		}
	}
}
