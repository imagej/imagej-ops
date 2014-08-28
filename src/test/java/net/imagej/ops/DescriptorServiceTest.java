package net.imagej.ops;

import static org.junit.Assert.assertEquals;
import io.scif.img.ImgIOException;

import java.util.Iterator;

import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.descriptorsets.CentralImageMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.HuMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.ImageMomentsDescriptorSet;
import net.imagej.ops.descriptors.descriptorsets.NormalizedCentralImageMomentsDescriptorSet;
import net.imglib2.IterableInterval;
import net.imglib2.Pair;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

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
	private static final double SMALL_DELTA = 1e-15;

	/**
	 * Small number, used for assertEquals if a little error margin is allowed.
	 */
	private static final double BIG_DELTA = 1e-4;

	/**
	 * Don't change the seed value, otherwise the OpenCV results have to be
	 * recalculated.
	 */
	private static final long SEED = 1234567890L;

	/** Subclasses can override to create a context with different services. */
	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
				DescriptorService.class);
	}

	@Test
	public void testImageMoments() throws IllegalArgumentException,
			ModuleException, ImgIOException, IncompatibleTypeException {
		final ImageGenerator dataGenerator = new ImageGenerator(SEED);
		final long[] dim = new long[] { 100, 100 };

		ImageMomentsDescriptorSet<IterableInterval> imds = new ImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		imds.compile();

		// 1. check empty image
		imds.update(dataGenerator.getEmptyUnsignedByteImg(dim));
		for (Pair<String, DoubleType> pair : imds) {
			assertEquals(0, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		imds.update(dataGenerator.getConstantUnsignedByteImg(dim, 15));
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
		imds.update(dataGenerator.getConstantUnsignedByteImg(dim, 50));
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
		imds.update(dataGenerator.getConstantUnsignedByteImg(dim, 127));
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
		imds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		imds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		imds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		final ImageGenerator dataGenerator = new ImageGenerator(SEED);
		final long[] dim = new long[] { 100, 100 };

		CentralImageMomentsDescriptorSet<IterableInterval> cimds = new CentralImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		cimds.compile();

		// 1. check empty image
		cimds.update(dataGenerator.getEmptyUnsignedByteImg(dim));
		for (Pair<String, DoubleType> pair : cimds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		cimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 15));
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
		cimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 50));
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
		cimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 127));
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
		cimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		cimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		cimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		final ImageGenerator dataGenerator = new ImageGenerator(SEED);
		final long[] dim = new long[] { 100, 100 };

		NormalizedCentralImageMomentsDescriptorSet<IterableInterval> ncimds = new NormalizedCentralImageMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		ncimds.compile();

		// 1. check empty image
		ncimds.update(dataGenerator.getEmptyUnsignedByteImg(dim));
		for (Pair<String, DoubleType> pair : ncimds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		ncimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 15));
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
		ncimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 50));
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
		ncimds.update(dataGenerator.getConstantUnsignedByteImg(dim, 127));
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
		ncimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		ncimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		ncimds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		final ImageGenerator dataGenerator = new ImageGenerator(SEED);
		final long[] dim = new long[] { 100, 100 };

		HuMomentsDescriptorSet<IterableInterval> hmds = new HuMomentsDescriptorSet<IterableInterval>(
				context, IterableInterval.class);
		hmds.compile();

		// 1. check empty image
		hmds.update(dataGenerator.getEmptyUnsignedByteImg(dim));
		for (Pair<String, DoubleType> pair : hmds) {
			assertEquals(Double.NaN, pair.getB().getRealDouble(), SMALL_DELTA);
		}

		/*
		 * CONSTANT VALUES, RESULTS HARDCODED (TAKEN FROM OPENCV)
		 */

		// 2. constant value 15
		hmds.update(dataGenerator.getConstantUnsignedByteImg(dim, 15));
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
		hmds.update(dataGenerator.getConstantUnsignedByteImg(dim, 50));
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
		hmds.update(dataGenerator.getConstantUnsignedByteImg(dim, 127));
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
		hmds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		hmds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
		hmds.update(dataGenerator.getRandomUnsignedByteImg(dim));
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
}
