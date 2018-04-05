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

package net.imagej.ops.map;

import net.imagej.ops.Ops;
import net.imagej.ops.thread.chunker.ChunkerOp;
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * A paralleled version of {@link MapNullaryComputer} over an
 * {@link IterableInterval}.
 * 
 * @author Leon Yang
 * @param <O> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW + 1)
public class MapNullaryII<O> extends
	AbstractMapNullaryComputer<O, IterableInterval<O>>
{

	@Override
	public void compute(IterableInterval<O> output) {
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				Maps.map(output, getOp(), startIndex, stepSize, numSteps);
			}
		}, output.size());
	}

}
