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

package net.imagej.ops.features.haralick;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Namespace for Haralick Features
 * 
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = Namespace.class)
public class HaralickNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "haralick";
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultASM.class)
	public <T extends RealType<T>> DoubleType asm(final IterableInterval<T> in,
		final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultASM.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultASM.class)
	public <T extends RealType<T>> DoubleType asm(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultASM.class, out, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultClusterPromenence.class)
	public <T extends RealType<T>> DoubleType clusterpromenence(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultClusterPromenence.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultClusterPromenence.class)
	public <T extends RealType<T>> DoubleType clusterpromenence(
		final DoubleType out, final IterableInterval<T> in, final int numGreyLevels,
		final int distance, final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultClusterPromenence.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultClusterShade.class)
	public <T extends RealType<T>> DoubleType clustershade(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultClusterShade.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultClusterShade.class)
	public <T extends RealType<T>> DoubleType clustershade(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultClusterShade.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultContrast.class)
	public <T extends RealType<T>> DoubleType contrast(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultContrast.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultContrast.class)
	public <T extends RealType<T>> DoubleType contrast(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultContrast.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultCorrelation.class)
	public <T extends RealType<T>> DoubleType correlation(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultCorrelation.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultCorrelation.class)
	public <T extends RealType<T>> DoubleType correlation(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultCorrelation.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultDifferenceEntropy.class)
	public <T extends RealType<T>> DoubleType differenceentropy(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultDifferenceEntropy.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultDifferenceEntropy.class)
	public <T extends RealType<T>> DoubleType differenceentropy(
		final DoubleType out, final IterableInterval<T> in, final int numGreyLevels,
		final int distance, final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultDifferenceEntropy.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultDifferenceVariance.class)
	public <T extends RealType<T>> DoubleType differencevariance(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultDifferenceVariance.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultDifferenceVariance.class)
	public <T extends RealType<T>> DoubleType differencevariance(
		final DoubleType out, final IterableInterval<T> in, final int numGreyLevels,
		final int distance, final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultDifferenceVariance.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultEntropy.class)
	public <T extends RealType<T>> DoubleType entropy(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultEntropy.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultEntropy.class)
	public <T extends RealType<T>> DoubleType entropy(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultEntropy.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultICM1.class)
	public <T extends RealType<T>> DoubleType icm1(final IterableInterval<T> in,
		final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultICM1.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultICM1.class)
	public <T extends RealType<T>> DoubleType icm1(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultICM1.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultICM2.class)
	public <T extends RealType<T>> DoubleType icm2(final IterableInterval<T> in,
		final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultICM2.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultICM2.class)
	public <T extends RealType<T>> DoubleType icm2(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultICM2.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultIFDM.class)
	public <T extends RealType<T>> DoubleType ifdm(final IterableInterval<T> in,
		final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultIFDM.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultIFDM.class)
	public <T extends RealType<T>> DoubleType ifdm(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultIFDM.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultMaxProbability.class)
	public <T extends RealType<T>> DoubleType maxprobability(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultMaxProbability.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultMaxProbability.class)
	public <T extends RealType<T>> DoubleType maxprobability(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultMaxProbability.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumAverage.class)
	public <T extends RealType<T>> DoubleType sumaverage(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumAverage.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumAverage.class)
	public <T extends RealType<T>> DoubleType sumaverage(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumAverage.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumEntropy.class)
	public <T extends RealType<T>> DoubleType sumentropy(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumEntropy.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumEntropy.class)
	public <T extends RealType<T>> DoubleType sumentropy(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumEntropy.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumVariance.class)
	public <T extends RealType<T>> DoubleType sumvariance(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumVariance.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultSumVariance.class)
	public <T extends RealType<T>> DoubleType sumvariance(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultSumVariance.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultTextureHomogeneity.class)
	public <T extends RealType<T>> DoubleType texturehomogeneity(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultTextureHomogeneity.class, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.features.haralick.DefaultTextureHomogeneity.class)
	public <T extends RealType<T>> DoubleType texturehomogeneity(
		final DoubleType out, final IterableInterval<T> in, final int numGreyLevels,
		final int distance, final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultTextureHomogeneity.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultVariance.class)
	public <T extends RealType<T>> DoubleType variance(
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultVariance.class, in, numGreyLevels,
			distance, orientation);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.haralick.DefaultVariance.class)
	public <T extends RealType<T>> DoubleType variance(final DoubleType out,
		final IterableInterval<T> in, final int numGreyLevels, final int distance,
		final MatrixOrientation orientation)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.features.haralick.DefaultVariance.class, out, in,
			numGreyLevels, distance, orientation);
		return result;
	}

}
