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

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.image.cooccurrenceMatrix.CooccurrenceMatrix2D;
import net.imagej.ops.image.cooccurrenceMatrix.MatrixOrientation;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for HaralickFeatures.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
public abstract class AbstractHaralickFeature<T extends RealType<T>> extends
	AbstractUnaryHybridCF<IterableInterval<T>, DoubleType> implements
	HaralickFeature<T>, Contingent
{

	@Parameter
	protected int numGreyLevels = 32;

	@Parameter
	protected int distance = 1;

	@Parameter
	protected MatrixOrientation orientation;

	private UnaryFunctionOp<IterableInterval<T>, double[][]> coocFunc;

	@Override
	public DoubleType createOutput(final IterableInterval<T> input) {
		return new DoubleType();
	}

	@Override
	public void initialize() {
		coocFunc = Functions.unary(ops(), Ops.Image.CooccurrenceMatrix.class,
			double[][].class, in(), numGreyLevels, distance, orientation);
	}

	/**
	 * Creates {@link CooccurrenceMatrix2D} from {@link IterableInterval} on
	 * demand, given the specified parameters. No caching!
	 * 
	 * @return the {@link CooccurrenceMatrix2D}
	 */
	protected double[][] getCooccurrenceMatrix(final IterableInterval<T> input) {
		return coocFunc.calculate(input);
	}

	@Override
	public boolean conforms() {
		return orientation.numDims() == in().numDimensions();
	}

}
