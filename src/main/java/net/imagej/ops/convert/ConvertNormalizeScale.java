package net.imagej.ops.convert;

import net.imagej.ops.Function;

/**
 * Base interface for "convert-normalize-scale" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ConvertNormalizeScale.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface ConvertNormalizeScale<I, O> extends Function<I, O> {
	String NAME = "convert-normalize-scale";
}
