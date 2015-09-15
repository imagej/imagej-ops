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
package net.imagej.ops.features.haralick;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.image.cooccurrencematrix.CooccurrenceMatrix2D;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;

/**
 * Abstract class for Haralick2DFeatures.
 * 
 * @author Christian Dietz, University of Konstanz
 *
 * @param <T>
 */
public abstract class AbstractHaralickFeature<T extends RealType<T>> extends
	AbstractHybridOp<IterableInterval<T>, DoubleType> implements
	HaralickFeature<T>, Contingent
{

	@Parameter
	protected int numGreyLevels = 32;

	@Parameter
	protected int distance = 1;

	@Parameter
	protected MatrixOrientation orientation;

	@Override
	public DoubleType createOutput(final IterableInterval<T> input) {
		return new DoubleType();
	}

	/**
	 * Creates {@link CooccurrenceMatrix2D} from {@link IterableInterval} on
	 * demand, given the specified parameters. No caching!
	 * 
	 * @return the {@link CooccurrenceMatrix2D}
	 */
	protected double[][] getCooccurrenceMatrix(final IterableInterval<T> input) {
		return ops().image().cooccurrencematrix(input, numGreyLevels,
			distance, orientation);
	}

	@Override
	public boolean conforms() {
		return orientation.numDims() == in().numDimensions();
	}

}
