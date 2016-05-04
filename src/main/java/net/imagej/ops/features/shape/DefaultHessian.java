/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.features.shape;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.CompositeChannelConverter;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;
import net.imglib2.view.composite.Composite;

import org.scijava.plugin.Plugin;

/**
 * Computes the Hessian for an input {@link RandomAccessibleInterval}.
 * 
 * The entries of the matrix are currently represented by entries of a
 * {@link Composite}. The order of the entries is:
 * <ul>
 * <li>d_{x_dim1, x_dim1}</li>
 * <li>d_{x_dim1, x_dim2}</li>
 * <li>...</li>
 * <li>d_{x_dim1, x_dimN}</li>
 * <li>...</li>
 * <li>d_{x_dimN, x_dim1}</li>
 * <li>...</li>
 * <li>d_{x_dimN, x_dimN}</li>
 * </ul>
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Filter.Hessian.class)
public class DefaultHessian<T extends RealType<T>> extends
		AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<? extends Composite<T>>>
		implements Ops.Filter.Hessian {

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<Composite<T>>> partialDerivatives;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		partialDerivatives = (UnaryFunctionOp) Functions.unary(ops(), Ops.Filter.AllPartialDerivatives.class,
				RandomAccessibleInterval.class, RandomAccessibleInterval.class);
	}

	@Override
	public RandomAccessibleInterval<? extends Composite<T>> compute1(RandomAccessibleInterval<T> input) {
		T inputType = Util.getTypeFromInterval(input);
		
		List<RandomAccessibleInterval<T>> partialDs = new ArrayList<>();
		
		// partialDerivatives on input
		RandomAccessibleInterval<Composite<T>> firstOrderPartialDerivatives = partialDerivatives.compute1(input);		
		for (int i=0; i<input.numDimensions(); i++) {
			CompositeChannelConverter<T, Composite<T>> conv = new CompositeChannelConverter<>(i);
			RandomAccessibleInterval<T> firstOrderPartialDerivative = Converters.convert(firstOrderPartialDerivatives, conv, inputType);
			
			RandomAccessibleInterval<Composite<T>> secondOrderPartialDerivatives = partialDerivatives.compute1(firstOrderPartialDerivative);
			for (int j=0; j<input.numDimensions(); j++) {
				CompositeChannelConverter<T, Composite<T>> conv2 = new CompositeChannelConverter<>(j);
				RandomAccessibleInterval<T> secondOrderPartialDerivative = Converters.convert(secondOrderPartialDerivatives, conv2, inputType);
				partialDs.add(secondOrderPartialDerivative);
			}
		}

		RandomAccessibleInterval<? extends Composite<T>> collapsed = Views.collapse(Views.stack(partialDs));
		
		// TODO Convert to RAI<MatrixType>
		
		return collapsed;
	}

}
