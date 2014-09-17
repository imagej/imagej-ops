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
 * <p>
 * Converts some <I> to another <O>. The values are copied. If a value of the
 * input is lower/higher than the output minimum/maximum, it will be set to the
 * minimum/maximum of the output.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public interface ConvertClip<I, O> extends Function<I, O> {
	String NAME = "convert-clip";
}
