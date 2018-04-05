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

import java.util.Iterator;

import net.imagej.ops.Ops;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link MapUnaryComputer} from {@link Iterable} input to {@link Iterable} output.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <EI> element type of inputs
 * @param <EO> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW - 1)
public class MapIterableToIterable<EI, EO> extends
	AbstractMapComputer<EI, EO, Iterable<EI>, Iterable<EO>>
{

	@Override
	public void compute(final Iterable<EI> input, final Iterable<EO> output) {
		final Iterator<EI> inCursor = input.iterator();
		final Iterator<EO> outCursor = output.iterator();

		while (inCursor.hasNext()) {
			getOp().compute(inCursor.next(), outCursor.next());
		}
	}
}
