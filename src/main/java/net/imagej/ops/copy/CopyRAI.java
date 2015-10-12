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

package net.imagej.ops.copy;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Create;
import net.imagej.ops.Ops.Map;
import net.imagej.ops.RAIs;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Copies a {@link RandomAccessibleInterval} into another
 * {@link RandomAccessibleInterval}
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 */
@Plugin(type = Ops.Copy.RAI.class, name = Ops.Copy.RAI.NAME, priority = 1.0)
public class CopyRAI<T> extends
	AbstractHybridOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Copy.RAI, Contingent
{

	@Parameter
	protected OpService ops;

	private ComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> mapComputer;

	private FunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createFunc;

	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return createFunc.compute(input);
	}

	@Override
	public void initialize() {
		final Class<?> outTypeClass =
			out() == null ? Type.class : Util.getTypeFromInterval(out()).getClass();
		final T inType = Util.getTypeFromInterval(in());
		final ComputerOp<T, ?> typeComputer =
			ops.computer(Ops.Copy.Type.class, outTypeClass, inType);
		mapComputer = RAIs.computer(ops(), Map.class, in(), typeComputer);
		createFunc = RAIs.function(ops(), Create.Img.class, in(), inType);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		mapComputer.compute(input, output);
	}

	@Override
	public boolean conforms() {
		if (out() == null) return true;
		return Intervals.equalDimensions(in(), out());
	}

}
