
package net.imagej.ops.resolver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Computer;
import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.Priority;

/**
 * Tests for the {@link OpResolverService}.
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class OpResolverTest extends AbstractOpTest {

	private OpResolverService resolver;
	private Img<ByteType> inputA;
	private ArrayImg<FloatType, FloatArray> inputB;

	@Before
	public void init() {
		resolver =
			new Context(OpService.class, OpMatchingService.class,
				OpResolverService.class).getService(OpResolverService.class);

		inputA = generateByteTestImg(false, new long[] { 2 });
		inputB = generateFloatArrayTestImg(false, new long[] { 2 });
	}

	@SuppressWarnings("unchecked")
	@Test
	public <T extends RealType<T>> void testResolvingByOpRef() {
		final Computer<Img<T>, ParentOp> resolved =
			resolver.resolve((Img<T>) inputA, new OpRef<ParentOp>(ParentOp.class));

		assertNotNull(resolved);

		// compute for first input (assuming highest priority op was chosen)
		assertTrue(resolved.compute((Img<T>) inputA).getOutput().get() == 1 + Priority.HIGH_PRIORITY);

		// compute for first input again. should have been cached (assuming highest
		// priority op was chosen)
		assertTrue(resolved.compute((Img<T>) inputA).getOutput().get() == 1 + Priority.HIGH_PRIORITY);

		// compute for second input (assuming highest priority op was chosen)
		assertTrue(resolved.compute((Img<T>) inputB).getOutput().get() == 3 + Priority.HIGH_PRIORITY);
	}

	@Test
	public void testResolvingByRefSet() {

		final OpRef<ParentOp> parent = new OpRef<ParentOp>(ParentOp.class);

		// Enforce low priority op...
		final OpRef<ChildOneLowPriority> childOne =
			new OpRef<ChildOneLowPriority>(ChildOneLowPriority.class);

		Set<OpRef<?>> refSet = new HashSet<OpRef<?>>();
		refSet.add(parent);
		refSet.add(childOne);

		final Computer<Img<ByteType>, Map<OpRef<?>, Op>> resolved =
			resolver.resolve(inputA, refSet);

		assertNotNull(resolved);
		// compute for first input
		assertTrue(((ParentOp) resolved.compute(inputA).get(parent)).getOutput()
			.get() == 2);

	}

	@Test
	public void testOutputOp() {
		final Computer<Img<ByteType>, DoubleType> resolved =
			resolver.<Img<ByteType>, DoubleType> resolve(DoubleType.class, inputA,
				ParentOp.class);

		assertNotNull(resolved);

		assertTrue(resolved.compute(inputA).get() == 1 + Priority.HIGH_PRIORITY);
	}
}
