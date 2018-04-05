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

package net.imagej.ops.labeling.cca;

import java.util.Iterator;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Default Implementation wrapping {@link ConnectedComponents} of
 * ImgLib2-algorithms.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Labeling.CCA.class, priority = 1.0)
public class DefaultCCA<T extends IntegerType<T>, L, I extends IntegerType<I>>
	extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, ImgLabeling<L, I>>
	implements Contingent, Ops.Labeling.CCA
{

	@Parameter
	private ThreadService threads;

	@Parameter
	private StructuringElement se;

	@Parameter(required = false)
	private Iterator<L> labelGenerator;

	private UnaryFunctionOp<Interval, ImgLabeling<L, I>> imgLabelingCreator;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		imgLabelingCreator = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Create.ImgLabeling.class, ImgLabeling.class, in());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final ImgLabeling<L, I> output)
	{
		if (labelGenerator == null) {
			labelGenerator = (Iterator<L>) new DefaultLabelIterator();
		}

		ConnectedComponents.labelAllConnectedComponents(input, output,
			labelGenerator, se, threads.getExecutorService());
	}

	@Override
	public ImgLabeling<L, I> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return imgLabelingCreator.calculate(input);
	}

	@Override
	public boolean conforms() {
		if (out() == null) return true;
		return Intervals.equalDimensions(in(), out());
	}

	/*
	 * Simple Default LabelIterator providing integer labels, starting from zero.
	 */
	class DefaultLabelIterator implements Iterator<Integer> {

		private Integer i = 0;

		@Override
		public boolean hasNext() {
			return i < Integer.MAX_VALUE;
		}

		@Override
		public Integer next() {
			return i++;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
