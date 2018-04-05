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

package net.imagej.ops.morphology.topHat;

import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.Maps;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * Computes the top-hat of a {@link RandomAccessibleInterval} using a
 * {@link List} of {@link Shape}s. It is the caller's responsibility to provide
 * a {@link RandomAccessibleInterval} with enough padding for the output.
 * 
 * @author Leon Yang
 * @param <T> element type
 * @see net.imglib2.algorithm.morphology.TopHat
 */
@Plugin(type = Ops.Morphology.TopHat.class)
public class ListTopHat<T extends RealType<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>>
	implements Ops.Morphology.TopHat, Contingent
{

	private BinaryHybridCF<RandomAccessibleInterval<T>, List<Shape>, IterableInterval<T>> openComputer;
	private BinaryInplaceOp<? super IterableInterval<T>, IterableInterval<T>> subtractor;

	@Override
	public boolean conforms() {
		return in1() == null || in2() == null || out() == null || //
			Maps.compatible(in1(), out());
	}

	@Override
	public void initialize() {
		openComputer = Hybrids.binaryCF(ops(), Ops.Morphology.Open.class, out(),
			in1(), in2());

		if (out() == null) setOutput(createOutput());

		final T type = Util.getTypeFromInterval(in1());
		subtractor = Inplaces.binary(ops(), Ops.Map.class, out(), Views.iterable(
			in()), Inplaces.binary(ops(), Ops.Math.Subtract.class, type, type));
	}

	@Override
	public IterableInterval<T> createOutput(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		return openComputer.createOutput(in1, in2);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> in1,
		final List<Shape> in2, final IterableInterval<T> out)
	{
		openComputer.compute(in1, in2, out);
		subtractor.mutate2(Views.iterable(in1), out);
	}
}
