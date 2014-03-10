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

package imagej.ops.map;

import imagej.ops.Function;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of an {@link InplaceMapper}
 * 
 * @author Christian Dietz
 * @param <A> type of values to be mapped
 * @param <I> {@link Iterable} of <A>s
 */
public abstract class AbstractInplaceMapper<A, I extends Iterable<A>>
	implements InplaceMapper<A>
{

	/**
	 * {@link Iterable} over <A> which will be used as input and output for the
	 * mapping.
	 */
	@Parameter(type = ItemIO.BOTH)
	protected I in;

	/**
	 * {@link Function} to be used for mapping
	 */
	@Parameter
	protected Function<A, A> func;

	@Override
	public Function<A, A> getFunction() {
		return func;
	}

	@Override
	public void setFunction(final Function<A, A> func) {
		this.func = func;
	}

}
