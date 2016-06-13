package net.imagej.ops.learning;

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.filter.FilterNamespace;

import org.junit.Test;

/**
 * Tests {@link FilterNamespace}.
 * 
 * @author Curtis Rueden
 */
public class LearningNamespaceTest extends AbstractNamespaceTest {

	/**
	 * Tests that the ops of the {@code filter} namespace have corresponding
	 * type-safe Java method signatures declared in the {@link FilterNamespace}
	 * class.
	 */
	@Test
	public void testCompleteness() {
		assertComplete("learning", LearningNamespace.class);
	}

}
