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

package net.imagej.ops.threshold;

import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.composite.Composite;

/**
 * An <em>integral</em> threshold learner learns a {@link UnaryComputerOp} from
 * a rectangle neighborhood that originates from one or multiple integral
 * images.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
public interface IntegralThresholdLearner<I, O extends BooleanType<O>> extends
	UnaryFunctionOp<RectangleNeighborhood<Composite<DoubleType>>, UnaryComputerOp<I, O>>
{

	/**
	 * @return the orders of integral images that are required for a local
	 *         threshold method. For example [1,2] for the default as well as the
	 *         squared integral image.
	 */
	default int[] requiredIntegralImages() {
		return new int[] { 1, 2 };
	}

}
