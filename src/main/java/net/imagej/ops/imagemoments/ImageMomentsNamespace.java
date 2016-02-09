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

package net.imagej.ops.imagemoments;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The image moments namespace contains ops related to image moments.
 *
 * @author Daniel Seebacher, University of Konstanz.
 */
@SuppressWarnings("unchecked")
@Plugin(type = Namespace.class)
public class ImageMomentsNamespace extends AbstractNamespace {

	@OpMethod(ops = {
		net.imagej.ops.imagemoments.centralmoments.IterableCentralMoment00.class,
		net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment00.class })
	public <I extends RealType<I>, O extends RealType<O>> O centralMoment00(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.ImageMoments.CentralMoment00.class, in);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.imagemoments.centralmoments.IterableCentralMoment00.class,
		net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment00.class })
	public <I extends RealType<I>, O extends RealType<O>> O centralMoment00(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.ImageMoments.CentralMoment00.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment01.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment01(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment01.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment01.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment01(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment01.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment02.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment02(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment02.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment02.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment02(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment02.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment03.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment03(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment03.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment03.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment03(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment03.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment10.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment10(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment10.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment10.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment10(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment10.class,
					out, in);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.imagemoments.centralmoments.IterableCentralMoment11.class,
		net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment11.class })
	public <I extends RealType<I>, O extends RealType<O>> O centralMoment11(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.ImageMoments.CentralMoment11.class, in);
		return result;
	}

	@OpMethod(ops = {
		net.imagej.ops.imagemoments.centralmoments.IterableCentralMoment11.class,
		net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment11.class })
	public <I extends RealType<I>, O extends RealType<O>> O centralMoment11(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.Ops.ImageMoments.CentralMoment11.class, out,
				in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment12.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment12(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment12.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment12.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment12(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment12.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment20.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment20(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment20.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment20.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment20(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment20.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment21.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment21(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment21.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment21.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment21(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment21.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment30.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment30(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment30.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment30.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O centralMoment30(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.centralmoments.DefaultCentralMoment30.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment00.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment00(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment00.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment00.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment00(final O out,
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment00.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment01.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment01(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment01.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment01.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment01(final O out,
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment01.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment10.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment10(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment10.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment10.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment10(final O out,
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment10.class,
				out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment11.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment11(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment11.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.moments.DefaultMoment11.class)
	public <I extends RealType<I>, O extends RealType<O>> O moment11(final O out,
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.moments.DefaultMoment11.class,
				out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment02.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment02(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment02.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment02.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment02(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment02.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment03.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment03(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment03.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment03.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment03(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment03.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment11.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment11(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment11.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment11.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment11(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment11.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment12.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment12(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment12.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment12.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment12(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment12.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment20.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment20(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment20.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment20.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment20(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment20.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment21.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment21(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment21.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment21.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment21(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment21.class,
					out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment30.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment30(
			final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment30.class,
					in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment30.class)
	public
		<I extends RealType<I>, O extends RealType<O>> O normalizedCentralMoment30(
			final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops()
				.run(
					net.imagej.ops.imagemoments.normalizedcentralmoments.DefaultNormalizedCentralMoment30.class,
					out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment1.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment1(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment1.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment1.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment1(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment1.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment2.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment2(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment2.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment2.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment2(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment2.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment3.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment3(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment3.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment3.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment3(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment3.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment4.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment4(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment4.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment4.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment4(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment4.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment5.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment5(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment5.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment5.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment5(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment5.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment6.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment6(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment6.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment6.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment6(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment6.class, out,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment7.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment7(
		final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment7.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.imagemoments.hu.DefaultHuMoment7.class)
	public <I extends RealType<I>, O extends RealType<O>> O huMoment7(
		final O out, final IterableInterval<I> in)
	{
		final O result =
			(O) ops().run(net.imagej.ops.imagemoments.hu.DefaultHuMoment7.class, out,
				in);
		return result;
	}

	// -- Named methods --
	@Override
	public String getName() {
		return "imagemoments";
	}
}
