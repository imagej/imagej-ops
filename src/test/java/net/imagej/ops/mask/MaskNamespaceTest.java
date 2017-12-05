
package net.imagej.ops.mask;

import net.imagej.ops.AbstractNamespaceTest;

import org.junit.Test;

/**
 * Tests {@link MaskNamespace}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class MaskNamespaceTest extends AbstractNamespaceTest {

	/**
	 * Tests that the ops of the {@code mask} namespace have corresponding
	 * type-safe Java method signatures declared in the {@link MaskNamespace}
	 * class.
	 */
	@Test
	public void testCompleteness() {
		assertComplete("mask", MaskNamespace.class);
	}

}
