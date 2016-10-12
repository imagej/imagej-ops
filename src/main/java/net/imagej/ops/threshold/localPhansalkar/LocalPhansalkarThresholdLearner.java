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

package net.imagej.ops.threshold.localPhansalkar;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.threshold.ThresholdLearner;
import net.imglib2.IterableInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * <p>
 * This is a modification of Sauvola's thresholding method to deal with low
 * contrast images. In this algorithm the threshold is computed as t =
 * mean*(1+p*exp(-q*mean)+k*((stdev/r)-1)) for an image that is normalized to
 * [0, 1].
 * </p>
 * <p>
 * Phansalkar recommends k = 0.25, r = 0.5, p = 2 and q = 10. In the current
 * implementation, the values of p and q are fixed but can be implemented as
 * additional parameters.
 * </p>
 * <p>
 * <a href="http://fiji.sc/Auto_Local_Threshold#Phansalkar">Originally
 * implemented</a> from Phansalkar's paper description by G. Landini.
 * </p>
 * <p>
 * <i>Phansalkar N. et al. Adaptive local thresholding for detection of nuclei
 * in diversity stained cytology images. International Conference on
 * Communications and Signal Processing (ICCSP), 2011, 218 - 220.
 * <a href="http://dx.doi.org/10.1109/ICCSP.2011.5739305">
 * doi:10.1109/ICCSP.2011.5739305</a></i>
 * </p>
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
@Plugin(type = Op.class)
public class LocalPhansalkarThresholdLearner<I extends RealType<I>, O extends BooleanType<O>>
	extends AbstractUnaryFunctionOp<IterableInterval<I>, UnaryComputerOp<I, O>>
	implements ThresholdLearner<I, O>
{

	@Parameter(required = false)
	private double k = 0.25;

	@Parameter(required = false)
	private double r = 0.5;

	private double p = 2.0;
	private double q = 10.0;

	private UnaryComputerOp<Iterable<I>, DoubleType> mean;
	private UnaryComputerOp<Iterable<I>, DoubleType> stdDeviation;

	@Override
	public UnaryComputerOp<I, O> compute1(final IterableInterval<I> input) {
		if (mean == null) {
			mean = Computers.unary(ops(), Ops.Stats.Mean.class, new DoubleType(),
				input);
		}

		if (stdDeviation == null) {
			stdDeviation = Computers.unary(ops(), Ops.Stats.StdDev.class,
				new DoubleType(), input);
		}

		final DoubleType meanValue = new DoubleType();
		mean.compute1(input, meanValue);

		final DoubleType stdDevValue = new DoubleType();
		stdDeviation.compute1(input, stdDevValue);

		double threshold = meanValue.get() * (1.0d + p * Math.exp(-q * meanValue
			.get()) + k * ((stdDevValue.get() / r) - 1.0));

		UnaryComputerOp<I, O> predictorOp = new AbstractUnaryComputerOp<I, O>() {

			@Override
			public void compute1(I in, O out) {
				out.set(in.getRealDouble() >= threshold);
			}
		};
		predictorOp.setEnvironment(ops());
		predictorOp.initialize();

		return predictorOp;
	}

}
