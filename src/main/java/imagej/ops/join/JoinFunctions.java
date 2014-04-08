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

package imagej.ops.join;

import imagej.ops.Function;
import imagej.ops.OutputFactory;

import java.util.List;

/**
 * A join operation which joins a list of {@link Function}s.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
public interface JoinFunctions<A, F extends Function<A, A>> extends
	Function<A, A>, Join
{

	/**
	 * @return {@link OutputFactory} used to create intermediate results
	 */
	OutputFactory<A, A> getBufferFactory();

	/**
	 * Sets the {@link OutputFactory} which is used to create intermediate
	 * results.
	 * 
	 * @param bufferFactory used to create intermediate results
	 */
	void setBufferFactory(OutputFactory<A, A> bufferFactory);

	/**
	 * @return {@link List} of {@link Function}s which are joined in this
	 *         {@link Join}
	 */
	List<? extends F> getFunctions();

	/**
	 * Sets the {@link Function}s which are joined in this {@link Join}.
	 * 
	 * @param functions joined in this {@link Join}
	 */
	void setFunctions(List<? extends F> functions);

}
