package net.imagej.ops.convert;

import net.imagej.ops.Function;

/**
 * Base interface for "convert-clip" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ConvertClip.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface ConvertClip<I, O> extends Function<I, O> {
	String NAME = "convert-clip";
}
