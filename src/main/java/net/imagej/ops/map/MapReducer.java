package net.imagej.ops.map;

import java.lang.reflect.Type;
import java.util.List;

import net.imagej.ops.OpReducer;
import net.imagej.ops.OpRef;
import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.Sampler;


public class MapReducer implements OpReducer {

	@Override
	public List<OpRef<?>> reduce(final OpRef<?> ref) {
		// for maps, can we do betteR? return a known-to-match OpCandidate?
		// each map could embed its own smarter acceptance of parameters
		// in other words: a map could accept arguments that do not actually
		// fit into its declared parameters
		// BUT: this is not as general as the reducer framework because
		// then each op is responsible for declaring its own "reductions"
		// which is less tenable from an extensibility perspective.
		ref.getArgs();
		return null;
	}

	// -- Helper methods --

	/** Gets an element (not necessarily the first element) of a container. */
	public Object element(final Object container) {
		// FIXME: Turn into plugins!
		if (container instanceof Iterable) {
			final Iterable<?> i = (Iterable<?>) container;
			return i.iterator().next();
		}
		if (container instanceof RandomAccessible) {
			return ((RandomAccessible<?>) container).randomAccess().get();
		}
		if (container instanceof RealRandomAccessible) {
			return ((RealRandomAccessible<?>) container).realRandomAccess().get();
		}
		if (container instanceof Sampler) {
			return ((Sampler<?>) container).get();
		}
		// And so on...
		return null;
	}

	public Type elementType(final Object container) {
		// TODO: Sometimes there is special type knowledge at runtime,
		// independent of any actual elements stored in the container.
		// Let's use that, if available.

		// NB: No special knowledge available.
		return element(container).getClass();
	}
}
