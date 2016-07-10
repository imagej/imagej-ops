package net.imagej.ops.topology;

import net.imagej.ops.AbstractNamespaceTest;
import org.junit.Test;

/**
 * Tests {@link TopologyNamespace}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class TopologyNamespaceTest extends AbstractNamespaceTest {

    /**
     * Tests that the ops of the {@code topology} namespace have corresponding
     * type-safe Java method signatures declared in the {@link TopologyNamespace}
     * class.
     */
    @Test
    public void testCompleteness() {
        assertComplete("topology", TopologyNamespace.class);
    }
}