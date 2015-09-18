package net.imagej.ops.geometric3d;

import org.junit.Test;

import net.imagej.ops.AbstractNamespaceTest;

public class Geometric3DNamespaceTest extends AbstractNamespaceTest {

	/**
	 * Tests that the ops of the {@code stats} namespace have corresponding
	 * type-safe Java method signatures declared in the {@link Geometric3DNamespace}
	 * class.
	 */
	@Test
	public void testCompleteness() {
		assertComplete("geometric3d", Geometric3DNamespace.class);
	}
	
}
