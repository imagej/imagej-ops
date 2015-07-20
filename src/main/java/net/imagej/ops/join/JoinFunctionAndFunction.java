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

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;

/**
 * A join operation which joins two {@link ComputerOp}s. The resulting function
 * will take the input of the first {@link ComputerOp} as input and the output of
 * the second {@link ComputerOp} as the output.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Curtis Rueden
 */
public interface JoinFunctionAndFunction<A, B, C, F1 extends ComputerOp<A, B>, F2 extends ComputerOp<B, C>>
	extends ComputerOp<A, C>, Ops.Join
{

	/**
	 * @return first {@link ComputerOp} to be joined
	 */
	F1 getFirst();

	/**
	 * @param first {@link ComputerOp} to be joined
	 */
	void setFirst(F1 first);

	/**
	 * @return second {@link ComputerOp} to be joined
	 */
	F2 getSecond();

	/**
	 * @param second {@link ComputerOp} to be joined
	 */
	void setSecond(F2 second);

}
