
package net.imagej.ops.mask.maskRange;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.type.Type;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * Creates an Iterable from all the elements in the input that are in the given
 * range of values
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Mask.MaskRange.class)
public class DefaultMaskRange<T extends Type<T>, U extends Comparable<T>>
	extends AbstractBinaryFunctionOp<Iterable<T>, Pair<U, U>, Iterable<T>>
	implements Ops.Mask.MaskRange
{

	@Override
	public Iterable<T> compute2(Iterable<T> iterable, Pair<U, U> range) {
		final Stream<T> inStream = StreamSupport.stream(iterable.spliterator(),
			false);
		final Stream<T> filtered = inStream.filter(e -> range.getA().compareTo(
			e) <= 0 && range.getB().compareTo(e) >= 0);

		return filtered.map(Type::copy).collect(Collectors.toList());
	}
}
