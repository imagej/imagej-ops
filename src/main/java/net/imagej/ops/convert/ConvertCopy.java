package net.imagej.ops.convert;

import net.imagej.ops.Function;

/**
 * Base interface for "convert-copy" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ConvertCopy.NAME)
 * </pre>
 * 
 * <p>
 * Converts from <I> to <O> by just copying the values.
 * </p>
 * 
 * @author Christian Dietz
 */
public interface ConvertCopy<I, O> extends Function<I, O> {
	String NAME = "convert-copy";
}
