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

package net.imagej.ops.coloc;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.coloc.pValue.PValueResult;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The coloc namespace contains ops that facilitate colocalization analysis.
 *
 * @author Curtis Rueden
 * @author Ellen T Arena
 */
@Plugin(type = Namespace.class)
public class ColocNamespace extends AbstractNamespace {

	// -- icq --

	@OpMethod(op = net.imagej.ops.coloc.icq.LiICQ.class)
	public <T extends RealType<T>, U extends RealType<U>> Double icq(
		final Iterable<T> image1, final Iterable<T> image2, final DoubleType mean1,
		final DoubleType mean2)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.icq.LiICQ.class, image1, image2, mean1, mean2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.icq.LiICQ.class)
	public <T extends RealType<T>, U extends RealType<U>> Double icq(
		final Iterable<T> image1, final Iterable<T> image2, final DoubleType mean1)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.icq.LiICQ.class, image1, image2, mean1);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.icq.LiICQ.class)
	public <T extends RealType<T>, U extends RealType<U>> Double icq(
		final Iterable<T> image1, final Iterable<T> image2)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.icq.LiICQ.class, image1, image2);
		return result;
	}

	// -- kendallTau --

	@OpMethod(op = net.imagej.ops.coloc.kendallTau.KendallTauBRank.class)
	public <T extends RealType<T>, U extends RealType<U>> Double kendallTau(
		final Iterable<T> image1, final Iterable<U> image2)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.kendallTau.KendallTauBRank.class, image1, image2);
		return result;
	}

	// -- maxTKendallTau --

	@OpMethod(op = net.imagej.ops.coloc.maxTKendallTau.MTKT.class)
	public <T extends RealType<T>, U extends RealType<U>> Double maxTKendallTau(
		final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.maxTKendallTau.MTKT.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.maxTKendallTau.MTKT.class)
	public <T extends RealType<T>, U extends RealType<U>> Double maxTKendallTau(
		final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2,
		final long seed)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.maxTKendallTau.MTKT.class, in1, in2, seed);
		return result;
	}

	// -- pearsons --

	@OpMethod(op = net.imagej.ops.coloc.pearsons.DefaultPearsons.class)
	public <T extends RealType<T>, U extends RealType<U>> Double pearsons(
		final Iterable<T> image1, final Iterable<U> image2)
	{
		final Double result = (Double) ops().run(
			net.imagej.ops.coloc.pearsons.DefaultPearsons.class, image1, image2);
		return result;
	}

	// -- pValue --

	@OpMethod(op = net.imagej.ops.coloc.pValue.DefaultPValue.class)
	public <T extends RealType<T>, U extends RealType<U>> PValueResult pValue(
		final PValueResult out, final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2,
		final BinaryFunctionOp<Iterable<T>, Iterable<U>, Double> op)
	{
		final PValueResult result = (PValueResult) ops().run(
			net.imagej.ops.coloc.pValue.DefaultPValue.class, out, in1, in2, op);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.pValue.DefaultPValue.class)
	public <T extends RealType<T>, U extends RealType<U>> PValueResult pValue(
		final PValueResult out, final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2,
		final BinaryFunctionOp<Iterable<T>, Iterable<U>, Double> op,
		final int nrRandomizations)
	{
		final PValueResult result = (PValueResult) ops().run(
			net.imagej.ops.coloc.pValue.DefaultPValue.class, out, in1, in2, op,
			nrRandomizations);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.pValue.DefaultPValue.class)
	public <T extends RealType<T>, U extends RealType<U>> PValueResult pValue(
		final PValueResult out, final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2,
		final BinaryFunctionOp<Iterable<T>, Iterable<U>, Double> op,
		final int nrRandomizations, final Dimensions psfSize)
	{
		final PValueResult result = (PValueResult) ops().run(
			net.imagej.ops.coloc.pValue.DefaultPValue.class, out, in1, in2, op,
			nrRandomizations, psfSize);
		return result;
	}

	@OpMethod(op = net.imagej.ops.coloc.pValue.DefaultPValue.class)
	public <T extends RealType<T>, U extends RealType<U>> PValueResult pValue(
		final PValueResult out, final RandomAccessibleInterval<T> in1,
		final RandomAccessibleInterval<U> in2,
		final BinaryFunctionOp<Iterable<T>, Iterable<U>, Double> op,
		final int nrRandomizations, final Dimensions psfSize, final long seed)
	{
		final PValueResult result = (PValueResult) ops().run(
			net.imagej.ops.coloc.pValue.DefaultPValue.class, out, in1, in2, op,
			nrRandomizations, psfSize, seed);
		return result;
	}

	// -- Namespace methods --

	@Override
	public String getName() {
		return "coloc";
	}
}
