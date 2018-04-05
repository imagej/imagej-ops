/* #%L
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

package net.imagej.ops.filter.derivative;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the derivative (with sobel kernel) of an image in a given
 * dimension.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T>
 *            type of input
 */
@Plugin(type = Ops.Filter.PartialDerivative.class)
public class PartialDerivativeRAI<T extends RealType<T>> extends
		AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements Ops.Filter.PartialDerivative {

	@Parameter
	private int dimension;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAI;

	private BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> addOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> kernelBConvolveOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] kernelAConvolveOps;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		RandomAccessibleInterval<T> kernel = ops().create().kernelSobel(Util.getTypeFromInterval(in()));

		RandomAccessibleInterval<T> kernelA = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 0);

		RandomAccessibleInterval<T> kernelB = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 1);

		// add dimensions to kernel to rotate properly
		if (in().numDimensions() > 2) {
			RandomAccessible<T> expandedKernelA = Views.addDimension(kernelA);
			RandomAccessible<T> expandedKernelB = Views.addDimension(kernelB);
			for (int i = 0; i < in().numDimensions() - 3; i++) {
				expandedKernelA = Views.addDimension(expandedKernelA);
				expandedKernelB = Views.addDimension(expandedKernelB);
			}
			long[] dims = new long[in().numDimensions()];
			for (int j = 0; j < in().numDimensions(); j++) {
				dims[j] = 1;
			}
			dims[0] = 3;
			Interval kernelInterval = new FinalInterval(dims);
			kernelA = Views.interval(expandedKernelA, kernelInterval);
			kernelB = Views.interval(expandedKernelB, kernelInterval);
		}

		long[] dims = new long[in().numDimensions()];
		if (dimension == 0) {
			// FIXME hack
			kernelBConvolveOp = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), new Object[] { kernelB });
		} else {
			// rotate kernel B to dimension
			for (int j = 0; j < in().numDimensions(); j++) {
				if (j == dimension) {
					dims[j] = 3;
				} else {
					dims[j] = 1;
				}
			}

			Img<DoubleType> kernelInterval = ops().create().img(dims);

			RandomAccessibleInterval<T> rotatedKernelB = kernelB;
			for (int i = 0; i < dimension; i++) {
				rotatedKernelB = Views.rotate(rotatedKernelB, i, i + 1);
			}

			rotatedKernelB = Views.interval(rotatedKernelB, kernelInterval);
			kernelBConvolveOp = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), new Object[] { rotatedKernelB });
		}

		dims = null;

		// rotate kernel A to all other dimensions
		kernelAConvolveOps = new UnaryComputerOp[in().numDimensions()];
		if (dimension != 0) {
			kernelAConvolveOps[0] = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), new Object[] { kernelA });
		}
		RandomAccessibleInterval<T> rotatedKernelA = kernelA;
		for (int i = 1; i < in().numDimensions(); i++) {
			if (i != dimension) {
				dims = new long[in().numDimensions()];
				for (int j = 0; j < in().numDimensions(); j++) {
					if (i == j) {
						dims[j] = 3;
					} else {
						dims[j] = 1;
					}
				}
				Img<DoubleType> kernelInterval = ops().create().img(dims);
				for (int j = 0; j < i; j++) {
					rotatedKernelA = Views.rotate(rotatedKernelA, j, j + 1);
				}

				kernelAConvolveOps[i] = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(),
						new Object[] { Views.interval(rotatedKernelA, kernelInterval) });
				rotatedKernelA = kernelA;
			}
		}

		addOp = RAIs.binaryComputer(ops(), Ops.Math.Add.class, in(), in());
		createRAI = RAIs.function(ops(), Ops.Create.Img.class, in());
	}

	@Override
	public void compute(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {
		RandomAccessibleInterval<T> in = input;
		for (int i = input.numDimensions() - 1; i >= 0; i--) {
			RandomAccessibleInterval<T> derivative = createRAI.calculate(input);
			if (dimension == i) {
				kernelBConvolveOp.compute(Views.interval(Views.extendMirrorDouble(in), input), derivative);
			} else {
				kernelAConvolveOps[i].compute(Views.interval(Views.extendMirrorDouble(in), input), derivative);
			}
			in = derivative;
		}
		addOp.compute(output, in, output);
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		return createRAI.calculate(input);
	}
}
