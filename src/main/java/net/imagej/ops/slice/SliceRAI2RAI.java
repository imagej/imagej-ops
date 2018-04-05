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

package net.imagej.ops.slice;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link SliceOp} implementation for {@link RandomAccessibleInterval} input and
 * {@link RandomAccessibleInterval} output.
 * 
 * <p>
 * The input {@link RandomAccessibleInterval} will be wrapped into a
 * {@link SlicesII}, so that the given Op can compute on a per-slice base.
 * </p>
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Slice.class, priority = Priority.VERY_HIGH)
public class SliceRAI2RAI<I, O>
		extends AbstractUnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
		implements SliceOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> {

	@Parameter
	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> op;

	@Parameter
	private int[] axisIndices;

	@Parameter(required = false)
	private boolean dropSingleDimensions = true;

	private UnaryComputerOp<SlicesII<I>, SlicesII<O>> mapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		mapper = (UnaryComputerOp) Computers.unary(ops(), Ops.Map.class, SlicesII.class, SlicesII.class, op);
	}

	@Override
	public void compute(final RandomAccessibleInterval<I> input, final RandomAccessibleInterval<O> output) {
		mapper.compute(new SlicesII<>(input, axisIndices, dropSingleDimensions),
				new SlicesII<>(output, axisIndices, dropSingleDimensions));
	}

}
