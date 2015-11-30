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

package net.imagej.ops.logic;

import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractBinaryFunctionOp;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.BinaryFunctionOp;
import net.imglib2.type.logic.BoolType;

import org.scijava.plugin.Plugin;

/**
 * {@link BinaryFunctionOp} that performs a greater-than (>) comparison on two
 * {@link Comparable} objects.
 */
@Plugin(type = Ops.Logic.LessThan.class)
public class LessThanFunction<I> extends
	AbstractBinaryFunctionOp<Comparable<I>, I, BoolType> implements
	Ops.Logic.LessThan
{

	private BinaryComputerOp<Comparable<I>, I, BoolType> op;

	@Override
	public void initialize() {
		op =
			ops().binaryComputer(Ops.Logic.LessThan.class, BoolType.class, in1(),
				in2());
	}

	@Override
	public BoolType compute2(final Comparable<I> input1, final I input2) {
		final BoolType output = new BoolType();
		op.compute2(input1, input2, output);
		return output;
	}

}
