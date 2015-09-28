package net.imagej.ops.views;

import net.imagej.ops.FunctionOp;

/**
 * Simple marker interface for View ops.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public interface ViewOp<I, O> extends FunctionOp<I, O> {
	// NB: marker interface
}
