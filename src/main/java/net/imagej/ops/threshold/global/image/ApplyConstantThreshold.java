/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.threshold.global.image;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.map.MapI2I;
import net.imagej.ops.threshold.global.pixel.ApplyThresholdComparable;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Applies the given threshold value to every element along the given
 * {@link Iterable} input.
 * 
 * @author Martin Horn
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Ops.Threshold.NAME,
	priority = Priority.HIGH_PRIORITY)
public class ApplyConstantThreshold<T extends RealType<T>> extends
	AbstractStrictFunction<Iterable<T>, Iterable<BitType>> implements
	Ops.Threshold
{

	@Parameter
	private T threshold;

	@Parameter
	private OpService ops;

	@Override
	public Iterable<BitType> compute(final Iterable<T> input,
		final Iterable<BitType> output)
	{

		final Object applyThreshold =
			ops.op(ApplyThresholdComparable.class, BitType.class, threshold
				.getClass(), threshold);

		// TODO: Use ops.map(...) once multithreading of BitTypes is fixed.
		ops.run(MapI2I.class, output, input, applyThreshold);

		return output;
	}

}
