package net.imagej.ops.project;

import java.util.Iterator;

import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Contingent;
import net.imagej.ops.Function;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

public abstract class AbstractProjectRAI2II<I, O> extends
		AbstractFunction<RandomAccessibleInterval<I>, IterableInterval<O>>
		implements ProjectRAI2II<I, O>, Contingent {

	@Parameter
	private Function<Iterable<I>, O> method;

	@Parameter
	private int dim;

	@Override
	public boolean conforms() {
		// TODO this first check is too simple, but for now ok
		return getOutput() != null
				&& getInput().numDimensions() == getOutput().numDimensions() + 1
				&& getInput().numDimensions() > dim;
	}

	final class ProjectionDimensionIterable<T> implements Iterable<T> {

		private final long size;
		private final RandomAccess<T> access;
		private int dim;

		public ProjectionDimensionIterable(final long size,
				final RandomAccess<T> access, final int dim) {
			this.size = size;
			this.access = access;
			this.dim = dim;
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

	protected Function<Iterable<I>, O> getMethod() {
		return method;
	}

	protected int getDim() {
		return dim;
	}
}
