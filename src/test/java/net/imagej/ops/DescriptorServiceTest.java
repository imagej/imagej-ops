package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import io.scif.img.ImgIOException;

import java.util.Iterator;

import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.descriptorsets.CentralImageMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.FirstOrderStatisticsSet;
import net.imagej.ops.descriptors.descriptorsets.GeometricDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.HaralickDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.HuMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.ImageMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.NormalizedCentralImageMomentsDescriptorSet;
import net.imagej.ops.histogram.CooccurrenceMatrix.MatrixOrientation;
import net.imglib2.IterableInterval;
import net.imglib2.Pair;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.module.ModuleException;

@SuppressWarnings({ "rawtypes" })
public class DescriptorServiceTest<T extends RealType<T> & NativeType<T>>
		extends AbstractOpTest {

	/**
	 * Really small number, used for assertEquals with floating or double
	 * values.
	 */
	private static final double SMALL_DELTA = 1e-10;

	/**
	 * Medium small number, used for assertEquals with very little error margin.
	 */
	private static final double MEDIUM_DELTA = 1e-7;

	/**
	 * Small number, used for assertEquals if a little error margin is allowed.
	 */
	private static final double BIG_DELTA = 1e-3;

	/**
	 * Don't change the seed value, otherwise the OpenCV results have to be
	 * recalculated.
	 */
	private static final long SEED = 1234567890L;

	private static Img<UnsignedByteType> empty;
	private static Img<UnsignedByteType> constant1;
	private static Img<UnsignedByteType> constant2;
	private static Img<UnsignedByteType> constant3;
	private static Img<UnsignedByteType> random1;
	private static Img<UnsignedByteType> random2;
	private static Img<UnsignedByteType> random3;

	@BeforeClass
	public static void setup() {
		ImageGenerator dataGenerator = new ImageGenerator(SEED);
		long[] dim = new long[] { 100, 100 };

		empty = dataGenerator.getEmptyUnsignedByteImg(dim);
		constant1 = dataGenerator.getConstantUnsignedByteImg(dim, 15);
		constant2 = dataGenerator.getConstantUnsignedByteImg(dim, 50);
		constant3 = dataGenerator.getConstantUnsignedByteImg(dim, 127);
		random1 = dataGenerator.getRandomUnsignedByteImg(dim);
		random2 = dataGenerator.getRandomUnsignedByteImg(dim);
		random3 = dataGenerator.getRandomUnsignedByteImg(dim);

	}

	/** Subclasses can override to create a context with different services. */
	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
				DescriptorService.class);
	}

	@Test
	public void testGeometricFeatures() throws IllegalArgumentException,
			ModuleException, ImgIOException, IncompatibleTypeException {

		GeometricDescriptorSet<IterableInterval> gds = new GeometricDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		gds.compile();

		/*
		 * VALUES HARDCODED (TAKEN FROM IMAGEJ 1)
		 */
		gds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(constant3);
		{

			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}

		gds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = gds.iterator();
			assertEquals("Area", 10000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - X", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Center of Gravity - Y", 49.5, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Eccentricity", 1, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Diameter", 141.42135623731, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Feret Angle", 45, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P0 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P1 [Y]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [X]", empty.dimension(0) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P2 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [X]", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Convex Hull P3 [Y]", empty.dimension(1) - 1, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Perimeter", 400, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Circularity", 0.785398163397448, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
		}
	}

	@Test
	public void testImageMoments() throws IllegalArgumentException,
			ModuleException, ImgIOException, IncompatibleTypeException {

		ImageMomentsDescriptorSet<IterableInterval> imds = new ImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		imds.compile();

		// 1. check empty image
		imds.update(empty);
		for (Pair<String, DoubleType> pair : imds) {
			assertEquals(0, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		imds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 150000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 7425000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 7425000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 3.675375E8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 3. constant value 50
		imds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 500000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 2.475E7,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 2.475E7,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 1.225125E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 4. constant value 127
		imds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 1270000.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 6.2865E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 6.2865E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 3.1118175E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 5. first random image
		imds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 1277534.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 6.3018047E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 6.3535172E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 3.12877962E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		imds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 1261968.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 6.2906168E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 6.2773664E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 3.131106708E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		imds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = imds.iterator();
			assertEquals("m00", 1260717.0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m10", 6.2609528E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m01", 6.2381352E7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("m11", 3.094771733E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	@Test
	public void testCentralImageMoments() throws IllegalArgumentException,
			ModuleException, ImgIOException, IncompatibleTypeException {

		CentralImageMomentsDescriptorSet<IterableInterval> cimds = new CentralImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		cimds.compile();

		// 1. check empty image
		cimds.update(empty);
		for (Pair<String, DoubleType> pair : cimds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		cimds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu02", 1.249875E8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu20", 1.249875E8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		cimds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu02", 4.16625E8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu20", 4.16625E8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		cimds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu02", 1.0582275E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu20", 1.0582275E9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("mu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("mu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 5. first random image
		cimds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", -5275876.956702232, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu02", 1.0694469880269928E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu20", 1.0585772432642083E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu12", 5478324.271270752, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu21", -2.1636455685491943E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu30", 1.735560232991333E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu03", -4.0994213161157227E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
		}

		// 6. second random image
		cimds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", 1985641.887743473, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu02", 1.0444253652870817E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu20", 1.0505536958456001E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu12", 3.3325524259147644E7, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu21", 1.7981380145100403E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu30", -5.859344304091187E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu03", -3.1838951029937744E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
		}

		// 7. third random image
		cimds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = cimds.iterator();
			assertEquals("mu11", -3201090.973862648, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu02", 1.049499997040432E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu20", 1.0472611196493301E9, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu12", 3.7481829594758606E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu21", -1.2771085513909912E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu30", 5.317554360357666E7, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("mu03", -2.4841700113293457E8, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
		}
	}

	@Test
	public void testNormalizedCentralImageMoments()
			throws IllegalArgumentException, ModuleException, ImgIOException,
			IncompatibleTypeException {

		NormalizedCentralImageMomentsDescriptorSet<IterableInterval> ncimds = new NormalizedCentralImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		ncimds.compile();

		// 1. check empty image
		ncimds.update(empty);
		for (Pair<String, DoubleType> pair : ncimds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		ncimds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu02", 0.005555, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 0.005555, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		ncimds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu02", 0.0016665, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 0.0016665, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		ncimds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu02", 6.561023622047244E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 6.561023622047244E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu21", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu30", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("nu03", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 5. first random image
		ncimds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", -3.2325832933879204E-6, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu02", 6.552610106398286E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 6.486010078361372E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 2.969727272701925E-9, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu21", -1.1728837022440002E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu30", 9.408242926327751E-8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu03", -2.22224218245127E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		ncimds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", 1.2468213798789547E-6, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu02", 6.558140635357379E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 6.596621559891105E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 1.8627566987422293E-8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu21", 1.0050835526982639E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu30", -3.275127127125994E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu03", -1.779663505088374E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		ncimds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = ncimds.iterator();
			assertEquals("nu11", -2.0140154540164537E-6, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu02", 6.603090103618989E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu20", 6.589003863327336E-4, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu12", 2.1002778837545406E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu21", -7.156221760357673E-8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu30", 2.9796682657891092E-8, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("nu03", -1.3919937715662872E-7, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	@Test
	public void testHuMoments() throws IllegalArgumentException,
			ModuleException, ImgIOException, IncompatibleTypeException {

		HuMomentsDescriptorSet<IterableInterval> hmds = new HuMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		hmds.compile();

		// 1. check empty image
		hmds.update(empty);
		for (Pair<String, DoubleType> pair : hmds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		hmds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.01111, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h3", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h4", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h5", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h6", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h7", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		hmds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.003333,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("h2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h3", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h4", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h5", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h6", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h7", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		hmds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.001312204724409449, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h3", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h4", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h5", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h6", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("h7", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 5. first random image
		hmds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.001303862018475966, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h2", 8.615401633994056e-11, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h3", 2.406124306990366e-14, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h4", 1.246879188175627e-13, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h5", -6.610443880647384e-27, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h6", 1.131019166855569e-18, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h7", 1.716256940536518e-27, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		hmds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.001315476219524848, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h2", 2.102606974299642e-11, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h3", 3.769040615498671e-13, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h4", 1.014097742761106e-13, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h5", -1.240493232542567e-27, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h6", 4.633829896116251e-19, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h7", -1.978715514076355e-26, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		hmds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hmds.iterator();
			assertEquals("h1", 0.001319209396694632, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h2", 1.820925465161031e-11, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h3", 3.660424299346814e-13, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h4", 1.019362267322889e-13, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h5", 1.294306832897462e-26, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h6", 3.887536000931268e-19, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("h7", -1.483900227169219e-26, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	/**
	 * Test for the first order statistic methods. The results to verify our
	 * implementation were taken from MATLAB.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws ModuleException
	 */
	@Test
	public void testFirstOrderStatistics() throws IllegalArgumentException,
			ModuleException {
		FirstOrderStatisticsSet<IterableInterval> fosds = new FirstOrderStatisticsSet<IterableInterval>(
				context, IterableInterval.class);
		fosds.compile();

		// 1. check empty image, everything should be 0
		fosds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Minimum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Mean", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Median", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Standard Deviation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum of Inverses", Double.POSITIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", Double.NEGATIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 2. constant value 15
		fosds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 14.9999999999985, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 15, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Minimum", 15, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 14.9999999999985, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Mean", 15, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Median", 15, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Standard Deviation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 150000, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum of Inverses", 666.666666666766, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 2250000, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", 27080.5020110211, iterator.next()
					.getB().getRealDouble(), 1);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		fosds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 49.9999999999878, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 50, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Minimum", 50, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 49.9999999999929, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Mean", 50, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Median", 50, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Standard Deviation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 500000, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum of Inverses", 200.000000000029, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 25000000, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", 39120.230054279, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		fosds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 126.999999999896, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 127,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Minimum", 127,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Harmonic Mean", 127.000000000011, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Mean", 127, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Median", 127, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", Double.NaN, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Standard Deviation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 1270000,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Inverses", 78.7401574803079, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 161290000, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", 48441.8708645777, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 5. first random image
		fosds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 254,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Minimum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", 1.79464849937733, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("Mean", 127.7534, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 5437.93418843998, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", -507.810691261427, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 53069780.9168701, iterator
					.next().getB().getRealDouble(), MEDIUM_DELTA);
			assertEquals("Median", 128, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", -0.00126634173185859, iterator.next()
					.getB().getRealDouble(), BIG_DELTA);
			assertEquals("Standard Deviation", 73.7460374274008, iterator
					.next().getB().getRealDouble(), 0.01);
			assertEquals("Sum", 1277534,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Inverses", Double.POSITIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 217588654, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", Double.NEGATIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 5438.4780362436, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		fosds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 254,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Minimum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", 1.80497222449748, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("Mean", 126.1968, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 5381.94026975999, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 7698.92644262249, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 52281527.8011748, iterator
					.next().getB().getRealDouble(), MEDIUM_DELTA);
			assertEquals("Median", 125, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", 0.0194994274666443, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("Standard Deviation", 73.3653768313893, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 1261968,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Inverses", Double.POSITIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 213075726, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", Double.NEGATIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 5382.47851761176, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		fosds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = fosds.iterator();

			assertEquals("Geometric Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Maximum", 254,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Minimum", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Harmonic Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Kurtosis", 1.79895091953959, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("Mean", 126.0717, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 1 About Mean", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 2 About Mean", 5427.93915911003, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 3 About Mean", 6519.9232182735, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Moment 4 About Mean", 53001633.7692662, iterator
					.next().getB().getRealDouble(), MEDIUM_DELTA);
			assertEquals("Median", 125, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Skewness", 0.0163038445053382, iterator.next().getB()
					.getRealDouble(), BIG_DELTA);
			assertEquals("Standard Deviation", 73.6782329274444, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum", 1260717,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Inverses", Double.POSITIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Squares", 213220127, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum of Logs", Double.NEGATIVE_INFINITY, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 5428.48200731076, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	/**
	 * Test for Haralick. The results to verify our implementation were taken
	 * from the KNIME Image Features Node.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws ModuleException
	 */
	@Test
	public void testHaralickHorizontalFeature()
			throws IllegalArgumentException, ModuleException {
		HaralickDescriptorSet<IterableInterval> hds = new HaralickDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		hds.compile();
		hds.updateParameterDistance(1);
		hds.updateParameterNrGrayLevels(8);
		hds.updateParameterOrientation(MatrixOrientation.HORIZONTAL);

		// 1. empty image
		hds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM KNIME)
		 */

		// 2. constant value 15
		hds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		hds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		hds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 5. first random image
		hds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201346699, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 369.1571031787, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 56.8141540828, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.2726262626, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0123827841, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8476756085, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 2.95770352654046E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9295886956, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0008158104, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0565857962, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3288476115, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 8.0443434343, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4348598585, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.0702558821, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0857205362, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		hds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201525406, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 370.5048602141, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 56.5374605181, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 7.8781818182, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", 0.0199937766, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8381955201, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 1.93421667571414E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9262165423, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0007184896, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0530844958, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3421490122, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9688888889, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4435465582, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.1996381594, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0194549944, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		hds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201946638, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 353.3155921219, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 54.0902444282, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.2066666667, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0098849512, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8531269678, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 7.11236625150491E-017, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9241095239, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0007175723, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0530364098, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3348860418, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9567676768, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4355708296, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.0460097541, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0631691052, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	/**
	 * Test for Haralick. The results to verify our implementation were taken
	 * from the KNIME Image Features Node.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws ModuleException
	 */
	@Test
	public void testHaralickDiagonalFeature() throws IllegalArgumentException,
			ModuleException {

		HaralickDescriptorSet<IterableInterval> hds = new HaralickDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		hds.compile();
		hds.updateParameterDistance(1);
		hds.updateParameterNrGrayLevels(8);
		hds.updateParameterOrientation(MatrixOrientation.DIAGONAL);

		// 1. empty image
		hds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM KNIME)
		 */

		// 2. constant value 15
		hds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		hds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		hds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 5. first random image
		hds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201395694, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 372.0901144312, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 56.8411803578, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.1684521988, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", 0.0015698512, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8484551654, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 1.67400815431762E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9292013066, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0007909654, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0557157477, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3320004334, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 8.0407101316, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4434314877, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.1941390325, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0906478078, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		hds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201418076, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 359.6104611987, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 55.5157249328, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.049382716, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0007370857, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.841692822, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", -1.96891114523368E-016,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9263569627, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0005938005, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0482641539, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3343027612, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9646974798, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4328974142, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.037525286, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0217270005, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		hds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0202370818, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 340.5262710759, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 52.9390627707, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.3704724008, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0304984492, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8562357445, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 1.36175792864179E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9225802035, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0012456886, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0698384415, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3300269405, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9548005306, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4261132258, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 7.8750103699, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0613706927, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	/**
	 * Test for Haralick. The results to verify our implementation were taken
	 * from the KNIME Image Features Node.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws ModuleException
	 */
	@Test
	public void testHaralickVerticalFeature() throws IllegalArgumentException,
			ModuleException {
		HaralickDescriptorSet<IterableInterval> hds = new HaralickDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		hds.compile();
		hds.updateParameterDistance(1);
		hds.updateParameterNrGrayLevels(8);
		hds.updateParameterOrientation(MatrixOrientation.VERTICAL);

		// 1. empty image
		hds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM KNIME)
		 */

		// 2. constant value 15
		hds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		hds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		hds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 5. first random image
		hds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201282012, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 378.5250972118, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 57.7187036066, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.1213131313, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", 0.0072420286, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8481433333, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", -3.46944695195361E-016,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9293375224

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0007689469, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0549366137, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3335612737, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 8.0388888889, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.445227128, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.2398007856, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0902784792, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		hds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201257219, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 352.9619982121, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 54.4697160513, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.0704040404, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0030877414

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8445797087, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 1.39645239816133E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9276447621, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0005270198, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0454789064, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3354475016, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9653535354, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.432451205, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.0207188144, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0227807137

			, iterator.next().getB().getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		hds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201885063, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 354.2317532091, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 54.4050704004, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.1535353535, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0038398298, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8496057904, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", -5.46437894932694E-017,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9238075018, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0005382938, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0459399699, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3336761061, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.963030303, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4358200711, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.091158494, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0611734619, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}

	/**
	 * Test for Haralick. The results to verify our implementation were taken
	 * from the KNIME Image Features Node.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws ModuleException
	 */
	@Test
	public void testHaralickAntiDiagonalFeature()
			throws IllegalArgumentException, ModuleException {

		HaralickDescriptorSet<IterableInterval> hds = new HaralickDescriptorSet<IterableInterval>(
				context, IterableInterval.class);

		hds.compile();
		hds.updateParameterDistance(1);
		hds.updateParameterNrGrayLevels(8);
		hds.updateParameterOrientation(MatrixOrientation.ANTIDIAGONAL);

		// 1. empty image
		hds.update(empty);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM KNIME)
		 */

		// 2. constant value 15
		hds.update(constant1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 3. constant value 50
		hds.update(constant2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		// 4. constant value 127
		hds.update(constant3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Cluster Promenence", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Correlation", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", -0.00000001, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("ICM2", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("IFDM", 1, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
			assertEquals("Sum Average", 2, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", -0.00000001, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 0, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 0, iterator.next().getB().getRealDouble(),
					SMALL_DELTA);
		}

		/*
		 * RANDOM VALUES, RESULTS HARDCODED (TAKEN FROM MATLAB)
		 */

		// 5. first random image
		hds.update(random1);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201046119, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 367.6122693633, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 56.5948864451, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.2808897051, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0122764487, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8544510522, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 3.02709246557953E-016, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9299310951, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0004193099, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0405811458, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3298830828, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 8.0411182532, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4362642308, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.0800356437, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.0902313372, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 6. second random image
		hds.update(random2);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0201537897, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 351.8612043331, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 54.576880637, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.1396796245, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0121076497, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8448083148, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", 9.0205620750794E-017, iterator
					.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9261770252, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0006810316, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0516833437, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3298978814, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.963983267, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4286792781, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 7.9449327715, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.021153099, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}

		// 7. third random image
		hds.update(random3);
		{
			Iterator<Pair<String, DoubleType>> iterator = hds.iterator();
			assertEquals("ASM", 0.0202223305, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Cluster Promenence", 355.1061476894, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Clustershade", 54.3439086966, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Contrast", 8.2134476074, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Correlation", -0.0112067807, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Entropy", 1.8462455101, iterator.next()
					.getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Difference Variance", -4.18068357710411E-016,
					iterator.next().getB().getRealDouble(), SMALL_DELTA);
			assertEquals("Entropy", 3.9230149085, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM1", -0.0010212571, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("ICM2", 0.0632499448, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("IFDM", 0.3262952982, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Average", 7.9544944393, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Entropy", 2.4344556994, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Sum Variance", 8.0313952168, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
			assertEquals("Variance", 4.061210706, iterator.next().getB()
					.getRealDouble(), SMALL_DELTA);
		}
	}
}
