package net.imagej.ops.scale.pixel;

import net.imagej.ops.Function;

/**
 * Base interface for "generic-scale" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = ScalePixel.NAME)
 * </pre>
 * 
 * <p>
 * Scales values of the interval [oldMin, oldMax] to values of [newMin, newMax].
 * </p>
 * 
 * @author Christian Dietz
 */
public interface GenericScale<I, O> extends Function<I, O> {
	String NAME = "scale-pixel";
}
