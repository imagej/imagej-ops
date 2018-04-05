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

package net.imagej.ops.threshold;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;

/**
 * Abstract superclass of {@link ApplyThresholdIterable} implementations that
 * operate on {@link Img} objects.
 *
 * @author Curtis Rueden
 * @author Christian Dietz (University of Konstanz)
 */
public abstract class AbstractApplyThresholdImg<T> extends
	AbstractApplyThresholdIterable<T, IterableInterval<T>, IterableInterval<BitType>>
{

	protected UnaryFunctionOp<IterableInterval<T>, Histogram1d<T>> histCreator;

	protected UnaryFunctionOp<IterableInterval<T>, Img<BitType>> imgCreator;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		applyThresholdComp = (BinaryComputerOp) Computers.binary(ops(), Ops.Threshold.Apply.class,
			out() == null ? IterableInterval.class : out(), in(), in().firstElement());
		histCreator = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Image.Histogram.class, Histogram1d.class, in());
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), new BitType());
	}

	// -- UnaryOutputFactory methods --

	@Override
	public Img<BitType> createOutput(final IterableInterval<T> input) {
		return imgCreator.calculate(input);
	}

}
