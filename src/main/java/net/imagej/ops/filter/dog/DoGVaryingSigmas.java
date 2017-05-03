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

package net.imagej.ops.filter.dog;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.chain.UHCFViaUHCFAllSame;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Util;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Difference of Gaussians (DoG) implementation where sigmas can vary by
 * dimension.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Filter.DoG.class)
public class DoGVaryingSigmas<T extends NumericType<T> & NativeType<T>> extends
	UHCFViaUHCFAllSame<RandomAccessibleInterval<T>> implements Ops.Filter.DoG,
	Contingent
{

	@Parameter
	private ThreadService ts;

	@Parameter
	private double[] sigmas1;

	@Parameter
	private double[] sigmas2;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> fac;

	@Override
	public UnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		createWorker(final RandomAccessibleInterval<T> t)
	{
		final T type = Util.getTypeFromInterval(t);
		return RAIs.hybrid(ops(), Ops.Filter.DoG.class, t, //
			RAIs.computer(ops(), Ops.Filter.Gauss.class, t, sigmas1, fac), //
			RAIs.computer(ops(), Ops.Filter.Gauss.class, t, sigmas2, fac), //
			RAIs.function(ops(), Ops.Create.Img.class, t), //
			RAIs.function(ops(), Ops.Create.Img.class, t, type));
	}

	@Override
	public boolean conforms() {
		return sigmas1.length == sigmas2.length &&
			sigmas1.length == in().numDimensions();
	}
}
