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

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This is a modification of Sauvola's thresholding method to deal with low
 * contrast images.
 * 
 * In this algorithm the threshold is computed as t =
 * mean*(1+p*exp(-q*mean)+k*((stdev/r)-1)) for an image that is normalized to
 * [0, 1].
 * 
 * Phansalkar recommends k = 0.25, r = 0.5, p = 2 and q = 10. In the current
 * implementation, the values of p and q are fixed but can be implemented as
 * additional parameters.
 * 
 * Originally implemented from Phansalkar's paper description by G. Landini
 * (http://fiji.sc/Auto_Local_Threshold#Phansalkar).
 * 
 * Phansalskar N. et al. Adaptive local thresholding for detection of nuclei in
 * diversity stained cytology images. International Conference on Communications
 * and Signal Processing (ICCSP), 2011, 218 - 220.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.LocalPhansalkar.class, name = Ops.Threshold.LocalPhansalkar.NAME)
public class LocalPhansalkar<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.LocalPhansalkar
{

	@Parameter(required = false)
	private double k = 0.25;

	@Parameter(required = false)
	private double r = 0.5;

	// FIXME: Faster calculation of mean and std-dev
	private UnaryComputerOp<Iterable<T>, DoubleType> mean;
	private UnaryComputerOp<Iterable<T>, DoubleType> stdDeviation;

	private double p = 2.0;
	private double q = 10.0;

	@Override
	public void initialize() {
		mean = Computers.unary(ops(), Mean.class, DoubleType.class, in().getB());
		stdDeviation = Computers.unary(ops(), StdDev.class, DoubleType.class, in().getB());
	}

	@Override
	public void compute1(final Pair<T, Iterable<T>> input, final BitType output) {

		final DoubleType meanValue = new DoubleType();
		mean.compute1(input.getB(), meanValue);

		final DoubleType stdDevValue = new DoubleType();
		stdDeviation.compute1(input.getB(), stdDevValue);

		double threshold = meanValue.get() * (1.0d + p * Math.exp(-q * meanValue.get()) + k * ((stdDevValue.get()/r) - 1.0));
		
		output.set(input.getA().getRealDouble() >= threshold);
	}

}
