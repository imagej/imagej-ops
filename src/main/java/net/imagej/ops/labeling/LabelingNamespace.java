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

package net.imagej.ops.labeling;

import java.util.Iterator;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Op;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.numeric.IntegerType;

/**
 * Namespace for {@link Op}s related to Labelings.
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class LabelingNamespace extends AbstractNamespace {

	@OpMethod(op = net.imagej.ops.LabelingOps.CCA.class)
	public <T extends IntegerType<T>, L, I extends IntegerType<I>>
		ImgLabeling<L, I> cca(Object... args)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result =
			(ImgLabeling<L, I>) ops().run(net.imagej.ops.LabelingOps.CCA.class, args);
		return result;
	}

	@OpMethod(op = net.imagej.ops.labeling.cca.DefaultCCA.class)
	public <T extends IntegerType<T>, L, I extends IntegerType<I>>
		ImgLabeling<L, I> cca(final ImgLabeling<L, I> out,
			final RandomAccessibleInterval<T> in, final StructuringElement element,
			final Iterator<L> labelGenerator)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result =
			(ImgLabeling<L, I>) ops().run(
				net.imagej.ops.labeling.cca.DefaultCCA.class, out, in, element,
				labelGenerator);

		return result;
	}

	@OpMethod(op = net.imagej.ops.labeling.cca.DefaultCCA.class)
	public <T extends IntegerType<T>, L, I extends IntegerType<I>>
		ImgLabeling<L, I> cca(final RandomAccessibleInterval<T> in,
			final StructuringElement element)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result =
			(ImgLabeling<L, I>) ops().run(
				net.imagej.ops.labeling.cca.DefaultCCA.class, in, element);

		return result;
	}

	@OpMethod(op = net.imagej.ops.labeling.cca.DefaultCCA.class)
	public <T extends IntegerType<T>, L, I extends IntegerType<I>>
		ImgLabeling<L, I> cca(final ImgLabeling<L, I> out,
			final RandomAccessibleInterval<T> in, final StructuringElement element)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result =
			(ImgLabeling<L, I>) ops().run(
				net.imagej.ops.labeling.cca.DefaultCCA.class, out, in, element, null);

		return result;
	}

	@Override
	public String getName() {
		return "labeling";
	}

}
