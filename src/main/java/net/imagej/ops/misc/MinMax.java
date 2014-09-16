package net.imagej.ops.misc;

import net.imagej.ops.Op;

/**
 * Base interface for "minmax" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = MinMax.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface MinMax<T> extends Op {
	String NAME = "minmax";
}
