
package net.imagej.ops.cached;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.scijava.command.CommandInfo;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.HybridOp;
import net.imagej.ops.Ops.Stats.Min;
import net.imagej.ops.cached.CachedOpEnvironment;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * JUnit-Tests for the {@link CachedOpEnvironment}.
 * 
 * Overriding with customOps is tested implicitly
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CachedOpEnvironmentTest extends AbstractOpTest {

	static int ctr = 0;

	private CachedOpEnvironment env;

	private Img<ByteType> imgA, imgB;

	private FunctionOp<Img<ByteType>, DoubleType> func;

	private HybridOp<Img<ByteType>, DoubleType> hybrid;

	@Before
	public void initCustomOps() {
		final ArrayList<CommandInfo> customOps = new ArrayList<CommandInfo>();
		customOps.add(new CommandInfo(MyMin.class));

		env = new CachedOpEnvironment(ops, customOps);

		imgA = generateByteTestImg(true, new long[] { 10, 10 });
		imgB = generateByteTestImg(true, new long[] { 10, 10 });

		func = env.function(Min.class, DoubleType.class, imgA);
		hybrid = env.hybrid(Min.class, DoubleType.class, imgA);
	}

	@Test
	public void testCachingFunctionOp() {
		ctr = 0;

		// Calling it twice should result in the same result
		assertEquals(1.0, func.compute(imgA).get(), 0.0);
		assertEquals(1.0, func.compute(imgA).get(), 0.0);

		// Should be increased
		assertEquals(2.0, func.compute(imgB).getRealDouble(), 0.0);
	}

	@Test
	public void testCachingHybrid() {
		ctr = 0;

		// Calling it twice should result in the same result
		assertEquals(1.0, hybrid.compute(imgA).get(), 0.0);
		assertEquals(1.0, hybrid.compute(imgA).get(), 0.0);

		// Should be increased
		assertEquals(2.0, hybrid.compute(imgB).getRealDouble(), 0.0);
	}

	// some specialized ops to track number of counts
	public static class MyMin extends AbstractHybridOp<Img<ByteType>, DoubleType>
		implements Min
	{

		@Override
		public DoubleType createOutput(final Img<ByteType> input) {
			return new DoubleType();
		}

		@Override
		public void compute(final Img<ByteType> input, final DoubleType output) {
			ctr++;
			output.set(ctr);
		}

	}

}
