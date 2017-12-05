
package net.imagej.ops.mask;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.mask.maskHigh.DefaultMaskHigh;
import net.imagej.ops.mask.maskLow.DefaultMaskLow;
import net.imagej.ops.mask.maskRange.DefaultMaskRange;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * The mask namespace contains ops for creating iterables where certain values
 * have been masked out
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Namespace.class)
public class MaskNamespace extends AbstractNamespace {

	// -- Mask high --
	@OpMethod(ops = DefaultMaskHigh.class)
	public <T, S extends Comparable<T>> Iterable<T> maskHigh(Iterable<T> in,
		S value)
	{
		@SuppressWarnings("unchecked")
		Iterable<T> result = (Iterable<T>) ops().run(DefaultMaskHigh.class, in,
			value);

		return result;
	}

	// -- Mask low --
	@OpMethod(ops = DefaultMaskLow.class)
	public <T, S extends Comparable<T>> Iterable<T> maskLow(Iterable<T> in,
		S value)
	{
		@SuppressWarnings("unchecked")
		Iterable<T> result = (Iterable<T>) ops().run(DefaultMaskLow.class, in,
			value);

		return result;
	}

	// -- Mask range --
	@OpMethod(ops = DefaultMaskRange.class)
	public <T, U extends Comparable<T>> Iterable<T> maskRange(Iterable<T> in,
		Pair<U, U> range)
	{
		@SuppressWarnings("unchecked")
		Iterable<T> result = (Iterable<T>) ops().run(DefaultMaskRange.class, in,
			range);

		return result;
	}

	// -- Namespace methods --
	@Override
	public String getName() {
		return "mask";
	}
}
