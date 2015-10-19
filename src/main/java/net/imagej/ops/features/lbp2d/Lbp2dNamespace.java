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
package net.imagej.ops.features.lbp2d;

import java.util.ArrayList;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;

/**
 * 
 * Namespace for 2d local binary pattern feature
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@Plugin(type = Namespace.class)
public class Lbp2dNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "lbp2d";
	}
	
	@SuppressWarnings("unchecked")
	@OpMethod(op = net.imagej.ops.features.lbp2d.DefaultLbp2d.class)
	public <T extends RealType<T>> ArrayList<LongType> lbp2d(final RandomAccessibleInterval<T> in, final int distance, final int histogramSize) {
		final ArrayList<LongType> result =
			(ArrayList<LongType>) ops().run(net.imagej.ops.features.lbp2d.DefaultLbp2d.class, in, distance, histogramSize);
		return result;
	}

	@SuppressWarnings("unchecked")
	@OpMethod(op = net.imagej.ops.features.lbp2d.DefaultLbp2d.class)
	public <T extends RealType<T>>ArrayList<LongType> lbp2d(final long[] out, final RandomAccessibleInterval<T> in, final int distance, final int histogramSize) {
		final ArrayList<LongType> result =
			(ArrayList<LongType>) ops().run(net.imagej.ops.features.lbp2d.DefaultLbp2d.class, out, in, distance, histogramSize);
		return result;
	}



}
