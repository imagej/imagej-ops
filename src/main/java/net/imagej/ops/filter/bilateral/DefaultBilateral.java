/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.filter.bilateral;

import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhoodFactory;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhoodSkipCenter;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;

/**
 * Performs a bilateral filter on an image
 * 
 * @author Gabe Selzer
 * @param <I>
 * @param <O>
 */

@Plugin(type = Ops.Filter.Bilateral.class, priority = Priority.NORMAL_PRIORITY)
public class DefaultBilateral<I extends RealType<I>, O extends RealType<O>>
	extends AbstractUnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
	implements Ops.Filter.Bilateral
{

	public final static int MIN_DIMS = 2;

	public final static int MAX_DIMS = 2;

	@Parameter
	private double m_sigmaR;

	@Parameter
	private double m_sigmaS;

	@Parameter
	private int m_radius;

	private static double gauss(final double x, final double sigma) {
		final double mu = 0.0;
		return (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp((-0.5 * (x - mu) *
			(x - mu)) / (sigma * sigma));
	}

	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		if (input.numDimensions() != 2) {
			throw new IllegalArgumentException("Input must be two dimensional");
		}

		final long[] size = new long[input.numDimensions()];
		input.dimensions(size);

		final RandomAccess<O> cr = output.randomAccess();
		final Cursor<I> cp = Views.iterable(input).localizingCursor();
		final long[] p = new long[input.numDimensions()];
		final int[] q = new int[input.numDimensions()];
		final long[] mi = new long[input.numDimensions()];
		final long[] ma = new long[input.numDimensions()];
		final long mma1 = input.max(0);
		final long mma2 = input.max(1);
		Neighborhood<I> rn;
		Cursor<I> cq;
		while (cp.hasNext()) {
			cp.fwd();
			cp.localize(p);
			double d;
			cp.localize(mi);
			cp.localize(ma);
			mi[0] = Math.max(0, mi[0] - m_radius);
			mi[1] = Math.max(0, mi[1] - m_radius);
			ma[0] = Math.min(mma1, ma[0] + m_radius);
			ma[1] = Math.min(mma2, ma[1] + m_radius);
			final Interval in = new FinalInterval(mi, ma);
			RectangleNeighborhoodFactory<I> fac = RectangleNeighborhood.factory();
			rn = fac.create(p, mi, ma, in, input.randomAccess());
			cq = rn.localizingCursor();
			double s, v = 0.0;
			double w = 0.0;
			do {
				cq.fwd();
				cq.localize(q);
				d = ((p[0] - q[0] - mi[0]) * (p[0] - q[0] - mi[0])) + ((p[1] - q[1] -
					mi[1]) * (p[1] - q[1] - mi[1]));
				d = Math.sqrt(d);//distance between pixels
				s = gauss(d, m_sigmaS);//spatial kernel

				d = Math.abs(cp.get().getRealDouble() - cq.get().getRealDouble());//intensity difference
				s *= gauss(d, m_sigmaR);//range kernel, then exponent addition

				v += s * cq.get().getRealDouble();
				w += s;
			} while (cq.hasNext());
			cr.setPosition(p);
			cr.get().setReal(v / w);
		}

	}
}
