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
package net.imagej.ops.features.zernike;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Namespace for Zernike features
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
@SuppressWarnings("unchecked")
@Plugin(type = Namespace.class)
public class ZernikeNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "zernike";
	}
	
	@OpMethod(op = net.imagej.ops.features.zernike.DefaultPhaseFeature.class)
	public <T extends RealType<T>, O extends RealType<O>> O phase(final IterableInterval<T> in, final int order,
			final int repitition) {
		final O result = (O) ops().run(net.imagej.ops.features.zernike.DefaultPhaseFeature.class, in, order,
				repitition);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.features.zernike.DefaultPhaseFeature.class)
	public <T extends RealType<T>, O extends RealType<O> > O phase(final O out, final IterableInterval<T> in, final int order,
			final int repitition) {
		final O result = (O) ops().run(net.imagej.ops.features.zernike.DefaultPhaseFeature.class, out, in, order,
				repitition);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.features.zernike.DefaultMagnitudeFeature.class)
	public <T extends RealType<T>, O extends RealType<O>> O magnitude(final IterableInterval<T> in, final int order,
			final int repitition) {
		final O result = (O) ops().run(net.imagej.ops.features.zernike.DefaultMagnitudeFeature.class, in, order,
				repitition);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.features.zernike.DefaultMagnitudeFeature.class)
	public <T extends RealType<T>, O extends RealType<O>> O magnitude(final O out, final IterableInterval<T> in, final int order,
			final int repitition) {
		final O result = (O) ops().run(net.imagej.ops.features.zernike.DefaultMagnitudeFeature.class, out, in, order,
				repitition);
		return result;
	}

}
