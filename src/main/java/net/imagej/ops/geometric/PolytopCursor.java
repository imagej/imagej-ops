
package net.imagej.ops.geometric;

import java.util.Iterator;
import java.util.List;

import net.imglib2.Cursor;
import net.imglib2.RealLocalizable;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BoolType;

/**
 * {@link Cursor} for a {@link Polytop}, iterates over every vertex of the
 * {@link Polytop}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @param <T>
 */
public final class PolytopCursor<T extends BooleanType<T>> implements
	Cursor<T>
{

	private final List<RealLocalizable> vertices;
	private Iterator<RealLocalizable> verticesIterator;
	private RealLocalizable current;

	public PolytopCursor(final List<RealLocalizable> vertices) {
		this.vertices = vertices;
		this.verticesIterator = this.vertices.iterator();
	}

	@Override
	public void localize(final float[] position) {
		this.current.localize(position);
	}

	@Override
	public void localize(final double[] position) {
		this.current.localize(position);
	}

	@Override
	public float getFloatPosition(final int d) {
		return this.current.getFloatPosition(d);
	}

	@Override
	public double getDoublePosition(final int d) {
		return this.current.getDoublePosition(d);
	}

	@Override
	public int numDimensions() {
		return this.current.numDimensions();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		// we only iterate over the vertices, so we can always assume true
		return (T) new BoolType(true);
	}

	@Override
	public PolytopCursor<T> copy() {
		return new PolytopCursor<T>(this.vertices);
	}

	@Override
	public void jumpFwd(final long steps) {
		for (long i = 0; i < steps; i++) {
			fwd();
		}
	}

	@Override
	public void fwd() {
		this.current = this.verticesIterator.next();
	}

	@Override
	public void reset() {
		this.current = null;
		this.verticesIterator = this.vertices.iterator();
	}

	@Override
	public boolean hasNext() {
		return this.verticesIterator.hasNext();
	}

	@Override
	public T next() {
		fwd();
		return get();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported!");
	}

	@Override
	public void localize(final int[] position) {
		for (int d = 0; d < this.current.numDimensions(); d++) {
			position[d] = getIntPosition(d);
		}
	}

	@Override
	public void localize(final long[] position) {
		for (int d = 0; d < this.current.numDimensions(); d++) {
			position[d] = getLongPosition(d);
		}
	}

	@Override
	public int getIntPosition(final int d) {
		return (int) this.current.getDoublePosition(d);
	}

	@Override
	public long getLongPosition(final int d) {
		return (long) this.current.getDoublePosition(d);
	}

	@Override
	public Cursor<T> copyCursor() {
		return copy();
	}

}
