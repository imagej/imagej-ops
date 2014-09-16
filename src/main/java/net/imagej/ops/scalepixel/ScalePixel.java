package net.imagej.ops.scalepixel;

import net.imagej.ops.Function;

/**
 * Base interface for "scale-pixel" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ScalePixel.NAME)
 * </pre>
 * 
 * @author Christian Dietz
 */
public interface ScalePixel<I, O> extends Function<I, O> {
	String NAME = "scale-pixel";
}
