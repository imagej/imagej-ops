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

package net.imagej.ops.features.haralick;

import net.imagej.ops.Ops;
import net.imagej.ops.features.haralick.helper.CoocHXY;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Implementation of Information Measure of Correlation 1 Haralick Feature
 * 
 * @author Andreas Graumann (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Ops.Haralick.ICM1.class,
	label = "Haralick: Information Measure of Correlation 1")
public class DefaultICM1<T extends RealType<T>> extends
	AbstractHaralickFeature<T> implements Ops.Haralick.ICM1
{

	private UnaryFunctionOp<double[][], double[]> coocHXYFunc;
	private UnaryFunctionOp<IterableInterval<T>, DoubleType> entropy;

	@Override
	public void initialize() {
		super.initialize();
		coocHXYFunc = Functions.unary(ops(), CoocHXY.class, double[].class,
			double[][].class);
		entropy = Functions.unary(ops(), Ops.Haralick.Entropy.class, DoubleType.class, in(),
			numGreyLevels, distance, orientation);
	}

	@Override
	public void compute(final IterableInterval<T> input,
		final DoubleType output)
	{
		final double[][] matrix = getCooccurrenceMatrix(input);

		final double[] coochxy = coocHXYFunc.calculate(matrix);

		final double res = (entropy.calculate(input).get() - coochxy[2]) / (coochxy[0] > coochxy[1]
			? coochxy[0] : coochxy[1]);

		output.set(res);
	}

}
