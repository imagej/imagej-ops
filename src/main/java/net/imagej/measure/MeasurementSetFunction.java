/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

package net.imagej.measure;

import net.imglib2.ops.function.Function;
import net.imglib2.ops.pointset.PointSet;

/**
 * MeasurementSetFunction is used by the {@link MeasurementService} to measure
 * multiple {@link Function}s at one time. It is a {@link Function} that changes
 * the output values in a {@link MeasurementSet} when its output value is
 * computed.
 * 
 * @author Barry DeZonia
 * @param <T> The type of output data calculated by functions in the
 *          MeasurementSet.
 */
@Deprecated
public class MeasurementSetFunction<T> implements
	Function<PointSet, MeasurementSet<T>>
{

	// -- instance variables --

	private final MeasurementSet<T> set;

	// -- MeasurementSetFunction methods --

	/**
	 * Constructs a {@link MeasurementSetFunction} from a {@link MeasurementSet}.
	 * A MeasurementSet is a collection of {@link Function}s and associated output
	 * values.
	 */
	public MeasurementSetFunction(final MeasurementSet<T> set) {
		this.set = set;
	}

	// -- Function methods --

	/**
	 * Fills all the outputs within a {@link MeasurementSet} from a given
	 * {@link PointSet}.
	 */
	@Override
	public void compute(final PointSet input, final MeasurementSet<T> output) {
		for (int i = 0; i < output.getNumMeasurements(); i++) {
			final T variable = output.getVariable(i);
			output.getFunction(i).compute(input, variable);
		}
	}

	/**
	 * Creates a new empty {@link MeasurementSet}. This is part of the Function
	 * api and is useful for parallelization.
	 */
	@Override
	public MeasurementSet<T> createOutput() {
		return set.create();
	}

	/**
	 * Creates a copy of this {@link MeasurementSetFunction}. This is part of the
	 * {@link Function} api and is useful for parallelization.
	 */
	@Override
	public MeasurementSetFunction<T> copy() {
		return new MeasurementSetFunction<T>(set.create());
	}

}
