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

package net.imagej.ops.copy;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.IntegerType;

/**
 * The filter namespace contains ops that copy data.
 *
 * @author Christian Dietz, University of Konstanz
 */
public class CopyNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "copy";
	}

	@OpMethod(op = net.imagej.ops.copy.CopyImg.class)
	public <T extends NativeType<T>> Img<T> img(final Img<T> in) {
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(net.imagej.ops.copy.CopyImg.class,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyImg.class)
	public <T extends NativeType<T>> Img<T> img(final Img<T> out,
		final Img<T> in)
	{
		@SuppressWarnings("unchecked")
		final Img<T> result = (Img<T>) ops().run(net.imagej.ops.copy.CopyImg.class,
			out, in);
		return result;
	}

	// FIXME: do we really have to expose CopyArrayImg here? Shouldn't we rather
	// just expose CopyImg?
	@OpMethod(op = net.imagej.ops.copy.CopyArrayImg.class)
	public <T extends NativeType<T>, A> ArrayImg<T, A> img(
		final ArrayImg<T, A> in)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<T, A> result = (ArrayImg<T, A>) ops().run(
			net.imagej.ops.copy.CopyArrayImg.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyArrayImg.class)
	public <T extends NativeType<T>, A> ArrayImg<T, A> img(
		final ArrayImg<T, A> out, final ArrayImg<T, A> in)
	{
		@SuppressWarnings("unchecked")
		final ArrayImg<T, A> result = (ArrayImg<T, A>) ops().run(
			net.imagej.ops.copy.CopyArrayImg.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyImgLabeling.class)
	public <L, I extends IntegerType<I>> ImgLabeling<L, I> imgLabeling(
		final ImgLabeling<L, I> in)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result = (ImgLabeling<L, I>) ops().run(
			net.imagej.ops.copy.CopyImgLabeling.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyImgLabeling.class)
	public <L, I extends IntegerType<I>> ImgLabeling<L, I> imgLabeling(
		final ImgLabeling<L, I> out, final ImgLabeling<L, I> in)
	{
		@SuppressWarnings("unchecked")
		final ImgLabeling<L, I> result = (ImgLabeling<L, I>) ops().run(
			net.imagej.ops.copy.CopyImgLabeling.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyIterableInterval.class)
	public <T> IterableInterval<T> iterableInterval(
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.copy.CopyIterableInterval.class, in);
		return result;
	}

	// FIXME: Here We can't be sure that we find an apropriate op as there might
	// not exist an Op mapping from T to T.
	@OpMethod(op = net.imagej.ops.copy.CopyIterableInterval.class)
	public <T> IterableInterval<T> iterableInterval(final IterableInterval<T> out,
		final IterableInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.copy.CopyIterableInterval.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyLabelingMapping.class)
	public <L> LabelingMapping<L> labelingMapping(final LabelingMapping<L> in) {
		@SuppressWarnings("unchecked")
		final LabelingMapping<L> result = (LabelingMapping<L>) ops().run(
			net.imagej.ops.copy.CopyLabelingMapping.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyLabelingMapping.class)
	public <L> LabelingMapping<L> labelingMapping(final LabelingMapping<L> out,
		final LabelingMapping<L> in)
	{
		@SuppressWarnings("unchecked")
		final LabelingMapping<L> result = (LabelingMapping<L>) ops().run(
			net.imagej.ops.copy.CopyLabelingMapping.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.copy.CopyRAI.class)
	public <T> RandomAccessibleInterval<T> rai(
		final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.copy.CopyRAI.class,
				in);
		return result;
	}

	// FIXME: Potentially, the passed RAis are incompatible. how to handle this?
	@OpMethod(op = net.imagej.ops.copy.CopyRAI.class)
	public <T> RandomAccessibleInterval<T> rai(
		final RandomAccessibleInterval<T> out,
		final RandomAccessibleInterval<T> in)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(net.imagej.ops.copy.CopyRAI.class,
				out, in);
		return result;
	}

}
