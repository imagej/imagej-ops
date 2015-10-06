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

package net.imagej.ops.threshold.apply;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

/**
 * Applies the given threshold value to every element along the given
 * {@link Iterable} input.
 *
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.Apply.class, name = Ops.Threshold.Apply.NAME,
	priority = Priority.HIGH_PRIORITY)
public class ApplyConstantThreshold<T extends RealType<T>> extends
	AbstractComputerOp<Iterable<T>, Iterable<BitType>> implements
	Ops.Threshold.Apply
{

	@Parameter
	private T threshold;
	private ComputerOp<T, BitType> applyThreshold;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		applyThreshold = (ComputerOp<T, BitType>) ops().computer(
			ApplyThresholdComparable.class, BitType.class, threshold.getClass(),
			threshold);
	}

	@Override
	public void compute(final Iterable<T> input, final Iterable<BitType> output) {
		// TODO: Use ops.map(...) once multithreading of BitTypes is fixed.
		ops().map(output, input, applyThreshold);
	}

}
