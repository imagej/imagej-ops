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
import net.imglib2.ops.pointset.PointSetIterator;
import net.imglib2.type.numeric.RealType;

/**
 * BasicStatsFunction is a {@link Function} that will calculate basic statistics
 * from another input Function.
 * <p>
 * This Function can be seen as an example of a Function that computes multiple
 * output values at one time by providing an aggregating class. A simpler
 * preferred method is available via the {@link MeasurementService} API using
 * the measure() method that takes multiple arguments.
 * </p>
 * <p>
 * The advantage of using an aggregating class is that such custom Functions can
 * reuse their computations internally as needed to improve performance. And the
 * aggregating class can return values of different types via getter methods.
 * The MeasurementService is limited to a set of outputs of a single type. In
 * practice this is probably not much of a limitation.
 * </p>
 * 
 * @author Barry DeZonia
 * @param <T> The output type of the input Function.
 */
@Deprecated
public class BasicStatsFunction<T extends RealType<T>> implements
	Function<PointSet, BasicStats>
{

	// -- instance variables --

	private final Function<long[], T> otherFunc;
	private final T tmp;
	private double[] data;
	private PointSet lastPointSet;
	private PointSetIterator iter;

	// -- constructor --

	/**
	 * Creates a BasicStatsFunction on another {@link Function}.
	 * 
	 * @param func The other Function to compute the BasicStats of.
	 * @param tmp A variable of the type of the other Function that can be used
	 *          for temporary calculations.
	 */
	public BasicStatsFunction(final Function<long[], T> func, final T tmp) {
		this.otherFunc = func;
		this.tmp = tmp.createVariable();
		this.data = null;
		this.lastPointSet = null;
		this.iter = null;
	}

	// -- Function methods --

	/**
	 * Computes the {@link BasicStats} of the other {@link Function} over the
	 * input region {@PointSet}.
	 */
	@Override
	public void compute(final PointSet input, final BasicStats output) {
		if (iter == null || lastPointSet != input) {
			// TODO - use an Img<DoubleType> to break limitations
			data = new double[(int) input.size()];
			iter = input.iterator();
		}
		else iter.reset();
		lastPointSet = input;
		int i = 0;
		while (iter.hasNext()) {
			final long[] coord = iter.next();
			otherFunc.compute(coord, tmp);
			data[i++] = tmp.getRealDouble();
		}
		output.calcStats(data);
	}

	/**
	 * Creates a {@link BasicStats}. This is part of the {@link Function} api and
	 * helps support parallelization.
	 */
	@Override
	public BasicStats createOutput() {
		return new BasicStats();
	}

	/**
	 * Creates a copy of this BasicStatsFunction. This is part of the
	 * {@link Function} api and helps support parallelization.
	 */
	@Override
	public Function<PointSet, BasicStats> copy() {
		return new BasicStatsFunction<T>(otherFunc, tmp);
	}

}
