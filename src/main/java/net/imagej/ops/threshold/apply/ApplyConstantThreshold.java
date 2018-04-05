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

package net.imagej.ops.threshold.apply;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Applies the given threshold value to every element along the given
 * {@link Iterable} input.
 *
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.Apply.class, priority = Priority.HIGH)
public class ApplyConstantThreshold<T extends RealType<T>> extends
	AbstractBinaryComputerOp<Iterable<T>, T, Iterable<BitType>> implements
	Ops.Threshold.Apply
{

	private BinaryComputerOp<T, T, BitType> applyThreshold;
	private UnaryComputerOp<Iterable<T>, Iterable<BitType>> mapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		applyThreshold = Computers.binary(ops(), ApplyThresholdComparable.class,
			BitType.class, in2(), in2());
		mapper = (UnaryComputerOp) Computers.unary(ops(), Ops.Map.class, out() == null
			? Iterable.class : out(), in1(), applyThreshold);
	}

	@Override
	public void compute(final Iterable<T> input1, final T input2,
		final Iterable<BitType> output)
	{
		applyThreshold.setInput2(input2);
		mapper.compute(input1, output);
	}

}
