package net.imagej.ops;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.cached.CachedOpEnvironment;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation2D;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

public class CachedOpEnvironmentExample extends AbstractOpTest {

	@Test
	public void example() {
		final IterableInterval<ByteType> img = generateByteArrayTestImg(true, new long[] { 15, 15 });
		final DoubleType mean = new DoubleType();

		ops.stats().mean(mean, img);
		System.out.println("Mean is: " + mean.get());

		// sometimes we want to reuse ops
		final UnaryComputerOp<Iterable<ByteType>, DoubleType> meanOp = Computers.unary(ops, Mean.class, mean, img);

		for (int i = 0; i < 10; i++) {
			meanOp.compute1(img, mean);
			System.out.println("Mean is: " + mean.get());
		}

		// and sometimes we want to simply cache stuff
		// sometimes we want to reuse ops
		final CachedOpEnvironment cachedEnv = new CachedOpEnvironment(ops);
		final UnaryFunctionOp<Iterable<ByteType>, RealType> meanOpCached = Functions.unary(cachedEnv, Mean.class,
				RealType.class, img);

		for (int i = 0; i < 10; i++) {
			System.out.println("Mean is cached: " + meanOpCached.compute1(img).getRealDouble());
		}

		// you don't believe me?
		final CachedOpEnvironment cachedEnvEnhanced = new CachedOpEnvironment(ops, myInfos());
		final UnaryFunctionOp<Iterable<ByteType>, RealType> meanOpCached2 = Functions.unary(cachedEnvEnhanced,
				Mean.class, RealType.class, img);

		for (int i = 0; i < 10; i++) {
			System.out.println("Mean is cached: " + meanOpCached2.compute1(img).getRealDouble());
		}

		// the cool thing is
		DoubleType asm = cachedEnv.haralick().asm(img, 32, 1, MatrixOrientation2D.ANTIDIAGONAL);

		System.out.println(asm.get());

		// no need to recalc cooccurrence matrix for img. right?
		DoubleType clusterpromenence = cachedEnv.haralick().clusterpromenence(img, 32, 1,
				MatrixOrientation2D.ANTIDIAGONAL);
		System.out.println(clusterpromenence.get());
	}

	private List<OpInfo> myInfos() {
		final ArrayList<OpInfo> ownOps = new ArrayList<OpInfo>();
		ownOps.add(new OpInfo(MySuperMean.class));
		return ownOps;
	}

	public static class MySuperMean<T extends RealType<T>> extends AbstractUnaryFunctionOp<Iterable<T>, DoubleType>
			implements Ops.Stats.Mean {

		@Override
		public DoubleType compute1(Iterable<T> input) {
			System.out.println("I'm CALLED and I RETURN STUPID stuff");

			DoubleType type = new DoubleType();
			type.setReal(42);

			return type;
		}
	}
}
