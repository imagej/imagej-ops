
package imagej.ops.descriptor;

import imagej.ops.AbstractOpTest;
import imagej.ops.OpMatchingService;
import imagej.ops.OpService;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.ResolvedDescriptor;
import imagej.ops.descriptors.statistics.Kurtosis;
import imagej.ops.descriptors.statistics.Mean;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.plugin.Parameter;

public class DescriptorServiceTest extends AbstractOpTest {

	@Parameter
	private DescriptorService descService;

	private Img<ByteType> in;

	private Img<ByteType> in2;

	@Before
	public void init() {
		in = generateByteTestImg(true, 10, 10);
		in2 = generateByteTestImg(true, 100, 100);
		// descService = context.getService(DescriptorService.class);

	}

	/** Subclasses can override to create a context with different services. */
	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
			DescriptorService.class);
	}

	@Test
	public void meanTest() {

		final ResolvedDescriptor<Mean, Img> updater =
			descService.resolveDependencies(Mean.class, in.getClass());

		System.out.println(updater.update(in).getOutput());
		System.out.println(updater.update(in2).getOutput());
	}

	@Test
	public void kurtosisTest() {

		final ResolvedDescriptor<Kurtosis, Img> updater =
			descService.resolveDependencies(Kurtosis.class, in.getClass());

		System.out.println(updater.update(in).getOutput());
		System.out.println(updater.update(in2).getOutput());
	}
}
