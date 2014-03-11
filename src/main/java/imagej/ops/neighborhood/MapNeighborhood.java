/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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

package imagej.ops.neighborhood;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;
import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.map.FunctionMap;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.localneighborhood.Neighborhood;
import net.imglib2.algorithm.region.localneighborhood.Shape;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates an {@link Function} for each {@link Neighborhood} on the in
 * {@link RandomAccessibleInterval}.
 * 
 * @author Christian Dietz
 * @author Martin Horn
 */
@Plugin(type = Op.class, name = FunctionMap.NAME, priority = Priority.LOW_PRIORITY)
public class MapNeighborhood<I, O> extends
	AbstractFunction<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements FunctionMap<Iterable<I>, O, Function<Iterable<I>, O>>
{

	@Parameter
	private Shape shape;

	@Parameter
	private OpService ops;

	@Parameter
	private Function<Iterable<I>, O> func;

	@Override
	public RandomAccessibleInterval<O> compute(
		final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		ops.run("map", output, shape.neighborhoodsSafe(input), func);
		// TODO: threaded map neighborhood
		// TODO: optimization with integral images, if there is a rectangular
		// neighborhood
		return output;
	}

	@Override
	public Function<Iterable<I>, O> getFunction() {
		return func;
	}

	@Override
	public void setFunction(Function<Iterable<I>, O> function) {
		func = function;
	}
}
