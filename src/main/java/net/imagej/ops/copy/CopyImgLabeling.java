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

package net.imagej.ops.copy;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import org.scijava.plugin.Plugin;

/**
 * Copying {@link ImgLabeling} into another {@link ImgLabeling}
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Copy.ImgLabeling.class)
public class CopyImgLabeling<T extends IntegerType<T> & NativeType<T>, L>
		extends AbstractUnaryHybridCF<ImgLabeling<L, T>, ImgLabeling<L, T>>
		implements Ops.Copy.ImgLabeling, Contingent {
	
	
	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> raiCopyOp;
	private UnaryComputerOp<LabelingMapping<L>, LabelingMapping<L>> mappingCopyOp;
	private UnaryFunctionOp<ImgLabeling<L, T>, ImgLabeling<L, T>> outputCreator;


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		raiCopyOp = Computers.unary(ops(), Ops.Copy.RAI.class, in().getIndexImg() ,in().getIndexImg());
		mappingCopyOp = Computers.unary(ops(), Ops.Copy.LabelingMapping.class, in().getMapping(), in().getMapping());
		outputCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.ImgLabeling.class, ImgLabeling.class, in());
	}
	
	@Override
	public ImgLabeling<L, T> createOutput(final ImgLabeling<L, T> input) {
		return outputCreator.calculate(input);
	}

	
	@Override
	public void compute(final ImgLabeling<L, T> input,
			final ImgLabeling<L, T> output) {
		raiCopyOp.compute(input.getIndexImg(), output.getIndexImg());
		mappingCopyOp.compute(input.getMapping(), output.getMapping());
	}

	@Override
	public boolean conforms() {
		if (out() != null) {
			return Intervals.equalDimensions(in(), out())
					&& Util.getTypeFromInterval(in().getIndexImg()).getClass() == Util
							.getTypeFromInterval(out().getIndexImg())
							.getClass();
		}

		return true;
	}

}
