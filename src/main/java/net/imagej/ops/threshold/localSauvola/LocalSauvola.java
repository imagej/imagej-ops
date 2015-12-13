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

package net.imagej.ops.threshold.localSauvola;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.Ops.Stats.StdDev;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.UnaryComputerOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This is a modification of Niblack's thresholding method. In contrast to the
 * recommendation on parameters in the publication, this implementation operates
 * on normalized images (to the [0, 1] range). Hence, the r parameter defaults
 * to half the possible standard deviation in a normalized image, namely 0.5
 * 
 * Sauvola J. and Pietaksinen M. (2000) "Adaptive Document Image Binarization"
 * Pattern Recognition, 33(2): 225-236
 * 
 * http://www.ee.oulu.fi/mvg/publications/show_pdf.php?ID=24
 * 
 * Original ImageJ1 implementation by Gabriel Landini.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.LocalSauvola.class, name = Ops.Threshold.LocalSauvola.NAME)
public class LocalSauvola<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.LocalSauvola
{

	@Parameter(required = false)
	private double k = 0.5d;

	@Parameter(required = false)
	private double r = 0.5d;

	// FIXME: Faster calculation of mean and std-dev.
	private UnaryComputerOp<Iterable<T>, DoubleType> mean;
	private UnaryComputerOp<Iterable<T>, DoubleType> stdDeviation;

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

		double threshold = meanValue.get() * (1.0d + k * ((Math.sqrt(stdDevValue.get())/r) - 1.0));

		output.set(input.getA().getRealDouble() >= threshold);
	}

}
