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

package net.imagej.ops.convert.normalizeScale;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.convert.scale.ScaleRealTypes;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Ops.Convert.NormalizeScale.class,
	name = Ops.Convert.NormalizeScale.NAME)
public class NormalizeScaleRealTypes<I extends RealType<I>, O extends RealType<O>>
	extends ScaleRealTypes<I, O> implements Ops.Convert.NormalizeScale, Contingent
{

	@Override
	public void checkInput(final I inType, final O outType) {
		outMin = outType.getMinValue();
	}

	@Override
	public void checkInput(final IterableInterval<I> in) {
		final Pair<I, I> minMax = ops().stats().minMax(in);
		final I inType = in.firstElement().createVariable();
		factor =
			1.0 / (minMax.getB().getRealDouble() - minMax.getA().getRealDouble()) *
				(inType.getMaxValue() - inType.getMinValue());

		inMin = minMax.getA().getRealDouble();

	}

	@Override
	public boolean conforms() {
		// only conforms if an input source has been provided and the scale factor
		// was calculated
		return factor != 0;
	}

}
