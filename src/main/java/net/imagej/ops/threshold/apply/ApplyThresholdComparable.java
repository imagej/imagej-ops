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

import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryComputerOp;
import net.imagej.ops.threshold.ApplyThreshold;
import net.imglib2.type.logic.BitType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Applies a threshold value to the given comparable object, producing a
 * {@link BitType} set to 1 iff the object compares above the threshold.
 *
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Threshold.Apply.class)
public class ApplyThresholdComparable<T> extends
	AbstractUnaryComputerOp<Comparable<? super T>, BitType> implements
	ApplyThreshold<Comparable<? super T>, BitType>
{

	@Parameter
	private T threshold;

	@Override
	public void compute1(final Comparable<? super T> input, final BitType output) {
		output.set(input.compareTo(threshold) > 0);
	}

}
