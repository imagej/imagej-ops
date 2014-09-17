/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.project;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.chunker.Chunker;
import net.imagej.ops.chunker.CursorBasedChunk;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Project.NAME, priority = Priority.LOW_PRIORITY + 1)
public class ParallelProjectRAI2II<T, V> extends AbstractProjectRAI2II<T, V> {

	@Parameter
	private OpService opService;

	@Override
	public IterableInterval<V> compute(final RandomAccessibleInterval<T> input,
			final IterableInterval<V> output) {
		opService.run(Chunker.class, new CursorBasedChunk() {

			@Override
			public void execute(int startIndex, final int stepSize,
					final int numSteps) {

				final int dim = getDim();
				final RandomAccess<T> access = input.randomAccess();
				final Cursor<V> cursor = output.localizingCursor();

				setToStart(cursor, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					for (int d = 0; d < input.numDimensions(); d++) {
						if (d != dim) {
							access.setPosition(
									cursor.getIntPosition(d - d > dim ? -1 : 0),
									d);
						}
					}

					getMethod()
							.compute(
									new ProjectionDimensionIterable<T>(input
											.dimension(dim), access, dim),
									cursor.get());

					cursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, output.size());

		return output;
	}

}
