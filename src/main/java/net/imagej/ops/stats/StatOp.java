
package net.imagej.ops.stats;

import com.sun.jdi.DoubleType;

import net.imagej.ops.HybridOp;

/**
 * marker interface for statistic ops.
 *
 * @author Daniel Seebacher, University of Konstanz
 */
public interface StatOp<I> extends HybridOp<I, DoubleType> {
	// NB: marker interface
}
