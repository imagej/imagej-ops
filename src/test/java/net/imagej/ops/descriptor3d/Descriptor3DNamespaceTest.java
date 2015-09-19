package net.imagej.ops.descriptor3d;

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.geometric3d.Geometric3DNamespace;

import org.junit.Test;

public class Descriptor3DNamespaceTest extends AbstractNamespaceTest {

	/**
	 * Tests that the ops of the {@code stats} namespace have corresponding
	 * type-safe Java method signatures declared in the {@link Geometric3DNamespace}
	 * class.
	 */
	@Test
	public void testCompleteness() {
		assertComplete("descriptor3d", Descriptor3DNamespace.class);
	}
}
