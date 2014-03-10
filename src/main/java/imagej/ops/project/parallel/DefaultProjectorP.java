/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.project.parallel;

import imagej.ops.AbstractFunction;
import imagej.ops.Contingent;
import imagej.ops.Function;
import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.Parallel;
import imagej.ops.project.Projector;
import imagej.ops.threading.ChunkExecutable;
import imagej.ops.threading.ChunkExecutor;

import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "project", priority = Priority.LOW_PRIORITY + 1)
public class DefaultProjectorP<T, V> extends
	AbstractFunction<RandomAccessibleInterval<T>, IterableInterval<V>> implements
	Contingent, Parallel, Projector
{

	@Parameter
	private OpService opService;

	@Parameter
	private Function<Iterable<T>, V> method;

	// dimension which will be projected
	@Parameter
	private int dim;

	@Override
	public IterableInterval<V> compute(final RandomAccessibleInterval<T> input,
		final IterableInterval<V> output)
	{
		opService.run(ChunkExecutor.class, new ChunkExecutable() {

			@Override
			public void
				execute(final int min, final int stepSize, final int numSteps)
			{
				final RandomAccess<T> access = input.randomAccess();
				final Cursor<V> cursor = output.localizingCursor();
				cursor.jumpFwd(min);

				int ctr = 0;
				while (ctr < numSteps) {
					cursor.jumpFwd(stepSize);
					for (int d = 0; d < input.numDimensions(); d++) {
						if (d != dim) {
							access
								.setPosition(cursor.getIntPosition(d - d > dim ? -1 : 0), d);
						}
					}

					method.compute(new DimensionIterable(input.dimension(dim), access),
						cursor.get());
					ctr++;
				}
			}
		}, output.size());

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
