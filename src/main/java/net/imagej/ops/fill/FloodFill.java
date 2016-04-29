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

package net.imagej.ops.fill;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Dimensions;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fill.Filter;
import net.imglib2.algorithm.fill.Writer;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Fills a {@link RandomAccessibleInterval} using flood-fill algorithm.
 * 
 * @author Leon Yang
 * @See {@link net.imglib2.algorithm.fill.FloodFill}
 */
@Plugin(type = Ops.Fill.Flood.class)
public class FloodFill<T extends Type<T>, U extends Type<U>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<U>>
	implements Ops.Fill.Flood
{

	@Parameter
	private Localizable seed;

	@Parameter(required = false)
	private T seedLabel;

	@Parameter
	private U fillLable;

	@Parameter
	private Shape shape;

	@Parameter
	private Filter<Pair<T, U>, Pair<T, U>> filter;

	@Parameter(required = false)
	private Writer<U> writer;

	private UnaryFunctionOp<Dimensions, Img<U>> imgCreator;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		imgCreator = (UnaryFunctionOp) Computers.unary(ops(), Ops.Create.Img.class,
			Img.class, RandomAccessibleInterval.class, fillLable);
	}

	@Override
	public RandomAccessibleInterval<U> createOutput(
		final RandomAccessibleInterval<T> in)
	{
		return imgCreator.compute1(in);
	}

	@Override
	public void compute1(final RandomAccessibleInterval<T> in,
		final RandomAccessibleInterval<U> out)
	{
		if (seedLabel == null) {
			net.imglib2.algorithm.fill.FloodFill.fill(in, out, seed, fillLable, shape,
				filter);
		}
		else if (writer == null) {
			net.imglib2.algorithm.fill.FloodFill.fill(in, out, seed, seedLabel,
				fillLable, shape, filter);
		}
		else {
			net.imglib2.algorithm.fill.FloodFill.fill(in, out, seed, seedLabel,
				fillLable, shape, filter, writer);
		}
	}
}
