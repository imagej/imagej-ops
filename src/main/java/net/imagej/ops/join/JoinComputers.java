/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.join;

import java.util.List;

import net.imagej.ops.BufferFactory;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;

/**
 * A join operation which joins a list of {@link ComputerOp}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
public interface JoinComputers<A, C extends ComputerOp<A, A>> extends
	ComputerOp<A, A>, Ops.Join
{

	/**
	 * @return {@link BufferFactory} used to create intermediate results
	 */
	BufferFactory<A, A> getBufferFactory();

	/**
	 * Sets the {@link BufferFactory} which is used to create intermediate
	 * results.
	 * 
	 * @param bufferFactory used to create intermediate results
	 */
	void setBufferFactory(BufferFactory<A, A> bufferFactory);

	/**
	 * @return {@link List} of {@link ComputerOp}s which are joined by this op
	 */
	List<? extends C> getOps();

	/**
	 * Sets the {@link ComputerOp}s which are joined in this op.
	 * 
	 * @param ops joined in this op
	 */
	void setOps(List<? extends C> ops);

}
