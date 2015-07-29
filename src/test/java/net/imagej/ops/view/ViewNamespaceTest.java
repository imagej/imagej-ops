package net.imagej.ops.view;

import org.junit.Test;

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.views.ViewNamespace;

/**
 * Tests that the ops of the logic namespace have corresponding type-safe Java
 * method signatures declared in the {@link ViewNamespace} class.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class ViewNamespaceTest extends AbstractNamespaceTest {

	/**
	 * Tests that the ops of the {@code view} namespace have corresponding
	 * type-safe Java method signatures declared in the {@link ViewNamespace}
	 * class.
	 */
	@Test
	public void testCompleteness() {
		assertComplete("view", ViewNamespace.class);
	}
}