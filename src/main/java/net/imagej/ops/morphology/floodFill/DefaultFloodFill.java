/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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

package net.imagej.ops.morphology.floodFill;

import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromRAI;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fill.Filter;
import net.imglib2.algorithm.fill.FloodFill;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.Type;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn (University of Konstanz)
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Morphology.FloodFill.class)
public class DefaultFloodFill<T extends Type<T> & Comparable<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, Localizable, RandomAccessibleInterval<T>>
	implements Ops.Morphology.FloodFill
{

	@Parameter()
	private Shape structElement = new RectangleShape(1, false);

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createFunc;

	@Override
	public void initialize() {
		createFunc = RAIs.function(ops(), CreateImgFromRAI.class, in());
	}

	@Override
	public void compute(RandomAccessibleInterval<T> op0, Localizable loc,
		RandomAccessibleInterval<T> r)
	{
		final RandomAccess<T> op0c = op0.randomAccess();
		op0c.setPosition(loc);
		final T fillValue = op0c.get().copy();
		FloodFill.fill(Views.extendValue(op0, fillValue), Views.extendValue(r,
			fillValue), loc, fillValue, structElement,
			(Filter<Pair<T, T>, Pair<T, T>>) (t, u) -> !t.getB().valueEquals(u
				.getB()) && t.getA().valueEquals(u.getA()));
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(
		RandomAccessibleInterval<T> input1, Localizable input2)
	{
		return createFunc.calculate(input1);
	}
}
