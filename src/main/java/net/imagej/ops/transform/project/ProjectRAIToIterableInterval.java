/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.transform.project;

import java.util.Iterator;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Transform.Project.class, priority = Priority.LOW)
public class ProjectRAIToIterableInterval<T, V> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, IterableInterval<V>>
	implements Contingent, Ops.Transform.Project
{

	@Parameter
	private UnaryComputerOp<Iterable<T>, V> method;

	// dimension which will be projected
	@Parameter
	private int dim;

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final IterableInterval<V> output)
	{
		final Cursor<V> cursor = output.localizingCursor();
		final RandomAccess<T> access = input.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			for (int d = 0; d < input.numDimensions(); d++) {
				if (d != dim) {
					access.setPosition(cursor.getIntPosition(d - (d > dim ? -1 : 0)), d);
				}
			}

			method.compute(new DimensionIterable(input.dimension(dim), access),
				cursor.get());
		}
	}

	@Override
	public boolean conforms() {
		// TODO this first check is too simple, but for now ok
		return in().numDimensions() == out().numDimensions() + 1 &&
			in().numDimensions() > dim;
	}

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
