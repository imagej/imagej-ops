package net.imagej.ops;

import io.scif.img.ImgIOException;
import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.descriptorsets.FirstOrderStatisticsSet;
import net.imagej.ops.descriptors.descriptorsets.GeometricDescriptorSet;
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

    @SuppressWarnings("rawtypes")
    @Test
    public void firstOrderStatisticsTest() throws IllegalArgumentException,
    ModuleException, ImgIOException {

	final FirstOrderStatisticsSet<IterableInterval> desc = new FirstOrderStatisticsSet<IterableInterval>(
		context, IterableInterval.class);

	desc.compile();
	desc.update(in);

	for (final Pair<String, DoubleType> results : desc) {
	    System.out.println(results.getA() + " " + results.getB());
	}
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void haralickTest() throws IllegalArgumentException, ModuleException {

	final HaralickDescriptorSet<IterableInterval> desc = new HaralickDescriptorSet<IterableInterval>(
		context, IterableInterval.class);

	desc.compile();
	desc.update(in);

	desc.updateParameterNrGrayLevels(32);
	desc.updateParameterDistance(1);
	desc.updateParameterOrientation(MatrixOrientation.VERTICAL);

	System.out.println("### FIRST RUN ####");
	for (final Pair<String, DoubleType> res : desc) {
	    System.out.println(res.getA() + " " + res.getB().get());
	}

	System.out.println("\n");
	desc.updateParameterNrGrayLevels(32);
	desc.updateParameterDistance(1);
	desc.updateParameterOrientation(MatrixOrientation.HORIZONTAL);

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

    @Test
    public void geometricTest() throws IllegalArgumentException,
    ModuleException {
	@SuppressWarnings("rawtypes")
	final GeometricDescriptorSet<IterableInterval> geomDescs = new GeometricDescriptorSet<IterableInterval>(
		context, IterableInterval.class);

	geomDescs.compile();

	geomDescs.update(in);

	for (final Pair<String, DoubleType> pair : geomDescs) {
	    System.out.println(pair.getA() + " " + pair.getB().get());
	}
    }
}
