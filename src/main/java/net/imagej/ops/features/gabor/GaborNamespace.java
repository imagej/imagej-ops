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

package net.imagej.ops.features.gabor;

import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Namespace for Gabor Features
 * 
 * @author Daniel Seebacher, University of Konstanz
 */
@Plugin(type = Namespace.class)
public class GaborNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "gabor";
	}

	@OpMethod(op = net.imagej.ops.features.gabor.GaborFeatures.class)
	public double[] feature(final Img<DoubleType> img, final List<List<Img<DoubleType>>> gaborArray, final int d1,
			final int d2) {
		final double[] result = (double[]) ops().run(net.imagej.ops.features.gabor.GaborFeatures.class, img, gaborArray,
				d1, d2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.features.gabor.GaborFilterBank.class)
	public List<List<Img<DoubleType>>> filterBank(final int u, final int v, final long m, final long n) {
		final List<List<Img<DoubleType>>> result = (List<List<Img<DoubleType>>>) ops()
				.run(net.imagej.ops.features.gabor.GaborFilterBank.class, u, v, m, n);
		return result;
	}
}
