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

package net.imagej.ops.deconvolve;

import net.imagej.ops.Ops;
import net.imagej.ops.math.IIToIIOutputII;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Implements update step for Richardson-Lucy algorithm on
 * {@link RandomAccessibleInterval}. See: <blockquote>Lucy, L. B. (1974).
 * "An iterative technique for the rectification of observed distributions"
 * </blockquote>
 * 
 * @author Brian Northan
 * @param <T> Type of {@link RandomAccessibleInterval} upon which to operate.
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyUpdate.class,
	priority = Priority.HIGH)
public class RichardsonLucyUpdate<T extends RealType<T>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Deconvolve.RichardsonLucyUpdate
{

	private BinaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> mul =
		null;

	@Override
	public void initialize() {

		// TODO: comment in when problem with initialize is fixed
		// mul = (BinaryHybridCF)Hybrids.binaryCF(ops(),
		// IIToIIOutputII.Multiply.class,
		// RandomAccessibleInterval.class, RandomAccessibleInterval.class,
		// RandomAccessibleInterval.class);
	}

	/**
	 * performs update step of the Richardson Lucy Algorithm
	 */
	@Override
	public void compute(RandomAccessibleInterval<T> correction,
		RandomAccessibleInterval<T> estimate)
	{

		// TODO: delte these lines when problem in initialization is fixed
		if (mul == null) {

			mul = Hybrids.binaryCF(ops(), IIToIIOutputII.Multiply.class, estimate,
				correction, estimate);
		}

		mul.compute(estimate, correction, estimate);
	}

}
