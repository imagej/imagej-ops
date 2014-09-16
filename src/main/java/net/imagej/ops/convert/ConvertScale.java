package net.imagej.ops.convert;

import net.imagej.ops.Function;

/**
 * Base interface for "convert-scale" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ConvertScale.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface ConvertScale<I, O> extends Function<I, O> {
	String NAME = "convert-scale";
}
