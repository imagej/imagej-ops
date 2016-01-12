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

package net.imagej.ops.deconvolve;

import org.scijava.Priority;
import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.filter.IterativeNonCirculantFFTFilterRAI;
import net.imagej.ops.math.divide.DivideHandleZero;
import net.imagej.ops.special.AbstractUnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Non-circulant Richardson Lucy algorithm for (@link RandomAccessibleInterval).
 * Boundary conditions are handled by the scheme described at:
 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */

@Plugin(type = Op.class, name = "rlnoncirculant",
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyNonCirculantRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends IterativeNonCirculantFFTFilterRAI<I, O, K, C>
{

	@Parameter(required = false)
	private StatusService status;

	/**
	 * Op that computes Richardson Lucy update
	 */
	@Parameter
	private AbstractUnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> update;

	@Override
	public void performIterations() {

		createReblurred();

		for (int i = 0; i < getMaxIterations(); i++) {

			System.out.println("RL Iteration: " + i);

			if (status != null) {
				status.showProgress(i, getMaxIterations());
			}

			// compute correction factor
			ops().run(RichardsonLucyCorrection.class, getRAIExtendedReblurred(), in(),
				getRAIExtendedReblurred(), getFFTInput(), getFFTKernel());

			// perform update
			update.compute1(getRAIExtendedReblurred(), getRAIExtendedEstimate());

			// normalize for non-circulant deconvolution
			ops().run(DivideHandleZero.class, getRAIExtendedEstimate(),
				getRAIExtendedEstimate(), getNormalization());

			// accelerate
			if (getAccelerator() != null) {
				getAccelerator().Accelerate(getRAIExtendedEstimate());
			}

			createReblurred();

		}
	}

}
