
package net.imagej.ops.mask.maskLow;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.type.Type;

import org.scijava.plugin.Plugin;

/**
 * Creates an Iterable from all the elements in the input that are less than the
 * given value
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Mask.MaskLow.class)
public class DefaultMaskLow<T extends Type<T>, S extends Comparable<T>> extends
	AbstractBinaryFunctionOp<Iterable<T>, S, Iterable<T>> implements
	Ops.Mask.MaskLow
{

	@Override
	public Iterable<T> compute2(Iterable<T> in, S value) {
		return StreamSupport.stream(in.spliterator(), false).filter(e -> value
			.compareTo(e) >= 0).map(Type::copy).collect(Collectors.toList());
	}
}
