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

package net.imagej.ops.special.computer;

import net.imagej.ops.special.AbstractBinaryOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract superclass for {@link BinaryComputerOp} implementations.
 * 
 * @author Curtis Rueden
 */
public abstract class AbstractBinaryComputerOp<I1, I2, O> extends
	AbstractBinaryOp<I1, I2, O> implements BinaryComputerOp<I1, I2, O>
{

	// -- Parameters --

	@Parameter(type = ItemIO.BOTH)
	private O out;

	@Parameter
	private I1 in1;

	@Parameter
	private I2 in2;

	// -- BinaryInput methods --

	@Override
	public I1 in1() {
		return in1;
	}

	@Override
	public I2 in2() {
		return in2;
	}

	@Override
	public void setInput1(final I1 input1) {
		in1 = input1;
	}

	@Override
	public void setInput2(final I2 input2) {
		in2 = input2;
	}

	// -- Output methods --

	@Override
	public O out() {
		return out;
	}

	// -- OutputMutable methods --

	@Override
	public void setOutput(final O output) {
		out = output;
	}

}
