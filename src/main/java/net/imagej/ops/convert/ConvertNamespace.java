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

package net.imagej.ops.convert;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The convert namespace contains operations for converting between types.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Namespace.class)
public class ConvertNamespace extends AbstractNamespace {

	// -- Convert namespace ops --

	@OpMethod(op = Ops.Convert.Clip.class)
	public Object clip(final Object... args) {
		return ops().run(Ops.Convert.Clip.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.clip.ClipRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O clip(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.clip.ClipRealTypes.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Copy.class)
	public Object copy(final Object... args) {
		return ops().run(Ops.Convert.Clip.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.copy.CopyRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O copy(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.copy.CopyRealTypes.class, out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.ImageType.class)
	public Object imageType(final Object... args) {
		return ops().run(Ops.Convert.ImageType.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.convert.imageType.ConvertIterableIntervals.class)
	public <I extends RealType<I>, O extends RealType<O>> IterableInterval<O>
		imageType(final IterableInterval<O> out, final IterableInterval<I> in,
			final RealTypeConverter<I, O> typeConverter)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<O> result =
			(IterableInterval<O>) ops().run(
				net.imagej.ops.convert.imageType.ConvertIterableIntervals.class, out,
				in, typeConverter);
		return result;
	}

	@OpMethod(op = Ops.Convert.NormalizeScale.class)
	public Object normalizeScale(final Object... args) {
		return ops().run(Ops.Convert.NormalizeScale.class, args);
	}

	@OpMethod(
		op = net.imagej.ops.convert.normalizeScale.NormalizeScaleRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O normalizeScale(
		final O out, final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(
				net.imagej.ops.convert.normalizeScale.NormalizeScaleRealTypes.class,
				out, in);
		return result;
	}

	@OpMethod(op = Ops.Convert.Scale.class)
	public Object scale(final Object... args) {
		return ops().run(Ops.Convert.Scale.class, args);
	}

	@OpMethod(op = net.imagej.ops.convert.scale.ScaleRealTypes.class)
	public <I extends RealType<I>, O extends RealType<O>> O scale(final O out,
		final I in)
	{
		@SuppressWarnings("unchecked")
		final O result =
			(O) ops().run(net.imagej.ops.convert.scale.ScaleRealTypes.class, out, in);
		return result;
	}

	// -- Named methods --

	@Override
	public String getName() {
		return "convert";
	}

}
