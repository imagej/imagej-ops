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
package net.imagej.ops.features.tamura2d;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

/**
 * 
 * Namespace for Tamura features
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@SuppressWarnings("unchecked")
@Plugin(type = Namespace.class)
public class TamuraNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "tamura2d";
	}
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultContrastFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O contrast (final RandomAccessibleInterval<I> in) {
		
		final O result = (O) ops().run(net.imagej.ops.features.tamura2d.DefaultContrastFeature.class, in);
		
		return result; 
	}
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultContrastFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O contrast (final O out, final RandomAccessibleInterval<I> in) {
		
		final O result = (O) ops().run(net.imagej.ops.features.tamura2d.DefaultContrastFeature.class, out, in);
		
		return result; 
	}
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultDirectionalityFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O directionality(final RandomAccessibleInterval<I> in, final int histogramSize) {
		final O result =
			(O) ops().run(net.imagej.ops.features.tamura2d.DefaultDirectionalityFeature.class, in, histogramSize);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultDirectionalityFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O directionality(final O out, final RandomAccessibleInterval<I> in, final int histogramSize) {
		final O result =
			(O) ops().run(net.imagej.ops.features.tamura2d.DefaultDirectionalityFeature.class, out, in, histogramSize);
		return result;
	}
	
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultCoarsenessFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O coarseness (final RandomAccessibleInterval<I> in) {
		
		final O result = (O) ops().run(net.imagej.ops.features.tamura2d.DefaultCoarsenessFeature.class, in);
		
		return result; 
	}
	
	@OpMethod(op = net.imagej.ops.features.tamura2d.DefaultCoarsenessFeature.class)
	public <I extends RealType<I>, O extends RealType<O>> O coarseness (final O out, final RandomAccessibleInterval<I> in) {
		
		final O result = (O) ops().run(net.imagej.ops.features.tamura2d.DefaultCoarsenessFeature.class, out, in);
		
		return result; 
	}
}
