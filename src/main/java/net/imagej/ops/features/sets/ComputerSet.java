/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.features.sets;

import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.UnaryFunctionOp;

/**
 * An {@link ComputerSet} holds different {@link Computers} which can be
 * computed on the same input and generate outputs of the same type.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <I>
 *            type of the common input
 * @param <O>
 *            type of the common output
 */
public interface ComputerSet<I, O> extends UnaryFunctionOp<I, Map<String, O>> {
	
	/**
	 * The input type of the {@link Computers} of this {@link ComputerSet}.
	 *
	 * @return the input type
	 */
	Class<I> getInType();

	/**
	 * The {@link Computers} which define this {@link ComputerSet}.
	 *
	 * @return all {@link Computers} of this {@link ComputerSet}
	 */
	Class<? extends Op>[] getComputers();
	
	/**
	 * An array of the names of all {@link Computers} which will be executed.
	 *
	 * @return the names of all active {@link Computers}.
	 */
	String[] getComputerNames();
	
	/**
	 * Get all active {@link Computers} of this {@link ComputerSet}.
	 *
	 * @return the active {@link Computers}
	 */
	List<Class<? extends Op>> getActiveComputers();

}
