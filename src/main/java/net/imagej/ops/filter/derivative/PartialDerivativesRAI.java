/* #%L
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

package net.imagej.ops.filter.derivative;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.RealComposite;

import org.scijava.plugin.Plugin;

/**
 * Convenience op for partial derivatives. Calculates all partial derivatives
 * using a separated sobel kernel and returns a {@link CompositeIntervalView}.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T>
 *            type of input
 */

@Plugin(type = Ops.Filter.AllPartialDerivatives.class)
public class PartialDerivativesRAI<T extends RealType<T>>
		extends AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, CompositeIntervalView<T, RealComposite<T>>>
		implements Ops.Filter.AllPartialDerivatives {

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] derivativeFunctions;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		derivativeFunctions = new UnaryFunctionOp[in().numDimensions()];
		for (int i = 0; i < in().numDimensions(); i++) {
			derivativeFunctions[i] = RAIs.function(ops(), Ops.Filter.PartialDerivative.class, in(), i);
		}
	}

	@Override
	public CompositeIntervalView<T, RealComposite<T>> calculate(RandomAccessibleInterval<T> input) {
		List<RandomAccessibleInterval<T>> derivatives = new ArrayList<>();
		for (int i = 0; i < derivativeFunctions.length; i++) {
			RandomAccessibleInterval<T> derivative = derivativeFunctions[i].calculate(input);
			derivatives.add(derivative);
		}

		RandomAccessibleInterval<T> stacked = Views.stack(derivatives);
		return Views.collapseReal(stacked);
	}
}
