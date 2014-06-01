package net.imagej.ops;

import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.descriptorsets.CoocParameter;
import net.imagej.ops.descriptors.descriptorsets.FirstOrderStatisticsSet;
import net.imagej.ops.descriptors.descriptorsets.HaralickDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.HistogramDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.ZernikeDescriptorSet;
import net.imglib2.Pair;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

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

	@Test
	public void firstOrderStatisticsTest() {

		final FirstOrderStatisticsSet set = new FirstOrderStatisticsSet(context);

		// make it ready for
		set.compileFor(in.getClass());

		// First image
		set.update(in);

		for (final Pair<String, Double> res : set) {
			System.out.println("First: " + res.getA() + " " + res.getB());
		}

		// Second image
		set.update(in2);
		for (final Pair<String, Double> res : set) {
			System.out.println("Second: " + res.getA() + " " + res.getB());
		}

	}

	@Test
	public void haralickTest() {

		final HaralickDescriptorSet desc = new HaralickDescriptorSet(context);
		desc.compileFor(in.getClass());

		// we need to set parameters.
		final CoocParameter coocParameter = new CoocParameter();
		coocParameter.setDistance(1);
		coocParameter.setOrientation("HORIZONTAL");
		coocParameter.setNrGrayLevels(32);

		desc.update(coocParameter);

		// First image
		desc.update(in);

		for (final Pair<String, Double> res : desc) {
			System.out.println("First: " + res.getA() + " " + res.getB());
		}
	}

	@Test
	public void histogramTest() {

		final HistogramDescriptorSet desc = new HistogramDescriptorSet(context,
				256);
		desc.compileFor(in.getClass());

		// First image
		desc.update(in);

		for (final Pair<String, Double> res : desc) {
			System.out.println("First: " + res.getA() + " " + res.getB());
		}
	}

	@Test
	public void zernikeTest() {

		final ZernikeDescriptorSet desc = new ZernikeDescriptorSet(context);
		desc.compileFor(in.getClass());

		desc.update(in);
		desc.update(desc.new ZernikeParameter(1));

		for (final Pair<String, Double> res : desc) {
			System.out.println("First: " + res.getA() + " " + res.getB());
		}
	}
}
