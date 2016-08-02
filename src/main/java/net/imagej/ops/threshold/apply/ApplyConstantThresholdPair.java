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

package net.imagej.ops.threshold.apply;

import java.util.Arrays;
import java.util.Comparator;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Applies the given threshold pair to every element along the given
 * {@link Iterable} input.
 *
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.Apply.class, priority = Priority.HIGH_PRIORITY)
public class ApplyConstantThresholdPair<T extends RealType<T>> extends
	AbstractBinaryComputerOp<Iterable<T>, Pair<T, T>, Iterable<BitType>>
	implements Ops.Threshold.Apply
{

	private UnaryComputerOp<T, BitType> applyThresholdCollection;

	private BinaryComputerOp<T, T, BitType> applyThresholdMin;
	private BinaryComputerOp<T, T, BitType> applyThresholdMax;

	private UnaryComputerOp<Iterable<T>, Iterable<BitType>> mapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		T type = in1().iterator().next();
		applyThresholdMin = Computers.binary(ops(), ApplyThresholdComparator.class,
			BitType.class, type, type, Comparator.naturalOrder());
		applyThresholdMax = Computers.binary(ops(), ApplyThresholdComparator.class,
			BitType.class, type, type, Comparator.reverseOrder());
		applyThresholdCollection = Computers.unary(ops(),
			ApplyThresholdCollection.class, BitType.class, type, Arrays.asList(
				applyThresholdMin, applyThresholdMax));
		mapper = (UnaryComputerOp) Computers.unary(ops(), Ops.Map.class,
			out() == null ? Iterable.class : out(), in1(), applyThresholdCollection);
	}

	@Override
	public void compute2(final Iterable<T> input1, final Pair<T, T> input2,
		final Iterable<BitType> output)
	{
		applyThresholdMin.setInput2(input2.getA());
		applyThresholdMax.setInput2(input2.getB());

		mapper.compute1(input1, output);
	}

}
