
package net.imagej.ops.math;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import net.imagej.ops.Ops;
import net.imagej.ops.benchmark.AbstractOpBenchmark;
import net.imagej.ops.math.divide.DivideHandleZero;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

@BenchmarkOptions(benchmarkRounds = 20, warmupRounds = 20)
public class MathBenchmarkTest extends AbstractOpBenchmark {

	ArrayImg<FloatType, ?> img1;
	ArrayImg<FloatType, ?> img2;
	ArrayImg<FloatType, ?> img3;
	ArrayImg<FloatType, ?> imgzero;

	float[] float1;
	float[] float2;
	float[] float3;
	float[] float4;

	Img<ByteType> byteimg;

	long x = 5000;
	long y = 5000;
	long size = x * y;

	/** Needed for JUnit-Benchmarks */
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	private ArrayImg<FloatType, ?> createConstantImg(long[] dims,
		float constant)
	{
		// create an input
		ArrayImg<FloatType, ?> img = new ArrayImgFactory<FloatType>().create(dims,
			new FloatType());

		for (final FloatType value : img) {
			value.setReal(constant);
		}

		return img;

	}

	@Before
	public void setUpImgs() {
		// create an input
		img1 = createConstantImg(new long[] { x, y }, 1.0f);
		img2 = createConstantImg(new long[] { x, y }, 2.0f);
		img3 = createConstantImg(new long[] { x, y }, 3.0f);
		imgzero = createConstantImg(new long[] { x, y }, 0.0f);

		byteimg = new ArrayImgFactory<ByteType>().create(new long[] { 20000,
			20000 }, new ByteType());

		float1 = new float[(int) size];
		float2 = new float[(int) size];
		float3 = new float[(int) size];
		float4 = new float[(int) size];

		for (int i = 0; i < size; i++) {
			float1[i] = 1.0f;
			float2[i] = 2.0f;
			float3[i] = 3.0f;
			float4[i] = 0.0f;

		}

	}

	@Test
	public void testDivide() {
		ops.run(Ops.Math.Divide.class, img3, img2, img1);
	}

	/*@Test
	public void testDivideMap() {
		ops.run(MapIterableIntervalToIterableInterval.class, img3, img2, ops.create(
			Divide.class));
	}

	@Plugin(type = Ops.Math.Divide.class)
	public static class Divide<T extends RealType<T>> extends
		AbstractUnaryComputerOp<T, T> implements Ops.Math.Divide
	{

		@Override
		public void compute1(final T input, final T output) {

			output.div(input);
		}
	}*/

	@Test
	public void testDivideIterableIntervalToImg() {
		ops.run(IIToIIOutputII.Divide.class, img3, img2, img1);
	}

	@Test
	public void testDivideHandleZero() {
		ops.run(DivideHandleZero.class, img3, img2, img1);
	}

	@Test
	public void testDivideExplicit() {

		for (int i = 0; i < size; i++) {
			float3[i] = float2[i] / float1[i];
		}
	}

	@Test
	public void testDivideWithCursorExplicit() {
		final Cursor<FloatType> cursor = img1.cursor();
		final Cursor<FloatType> cursorI = img2.cursor();
		final Cursor<FloatType> cursorO = img3.cursor();

		while (cursor.hasNext()) {
			cursor.fwd();
			cursorI.fwd();
			cursorO.fwd();

			cursorO.get().set(cursorI.get());
			cursorO.get().div(cursor.get());
		}

	}

}
