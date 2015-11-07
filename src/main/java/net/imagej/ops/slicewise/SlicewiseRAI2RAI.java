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

package net.imagej.ops.slicewise;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link SlicewiseOp} implementation for {@link RandomAccessibleInterval} input
 * and {@link RandomAccessibleInterval} output. </br>
 * The input {@link RandomAccessibleInterval} will be wrapped into a
 * {@link Hyperslice}, so that the given Op can compute on a per-slice base.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Slicewise.class, priority = Priority.VERY_HIGH_PRIORITY)
public class SlicewiseRAI2RAI<I, O> extends
	AbstractComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements
	SlicewiseOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	@Parameter
	private ComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> op;

	@Parameter
	private int[] axisIndices;

	@Parameter(required = false)
	private boolean dropSingleDimensions = true;

	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		ops().run(Ops.Map.class, new Hyperslice(ops(), output, axisIndices,
			dropSingleDimensions), new Hyperslice(ops(), input, axisIndices,
				dropSingleDimensions), op);
	}

}
