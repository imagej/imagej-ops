
package imagej.ops.project;

import imagej.ops.AbstractFunction;
import imagej.ops.Contingent;
import imagej.ops.Function;
import imagej.ops.Op;

import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "project", priority = Priority.LOW_PRIORITY)
public class DefaultProjector<T, V> extends
	AbstractFunction<RandomAccessibleInterval<T>, IterableInterval<V>> implements
	Contingent
{

	@Parameter
	private Function<Iterable<T>, V> method;

	// dimension which will be projected
	@Parameter
	private int dim;

	@Override
	public IterableInterval<V> compute(final RandomAccessibleInterval<T> input,
		final IterableInterval<V> output)
	{

		final Cursor<V> cursor = output.localizingCursor();
		final RandomAccess<T> access = input.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			for (int d = 0; d < input.numDimensions(); d++) {
				if (d != dim) {
					access.setPosition(cursor.getIntPosition(d - d > dim ? -1 : 0), d);
				}
			}

			method.compute(new DimensionIterable(input.dimension(dim), access),
				cursor.get());
		}

		return output;
	}

	@Override
	public boolean conforms() {
		// TODO this first check is too simple, but for now ok
		return getInput().numDimensions() == getOutput().numDimensions() + 1 &&
			getInput().numDimensions() > dim;
	};

	final class DimensionIterable implements Iterable<T> {

		private final long size;
		private final RandomAccess<T> access;

		public DimensionIterable(final long size, final RandomAccess<T> access) {
			this.size = size;
			this.access = access;
		}

		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {

				int k = -1;

				@Override
				public boolean hasNext() {
					return k < size - 1;
				}

				@Override
				public T next() {
					k++;
					access.setPosition(k, dim);
					return access.get();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("Not supported");
				}
			};
		}
	}
}
