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

import net.imagej.ops.Ops.Zernike;
import net.imagej.ops.Ops.Zernike.Magnitude;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;


/**
 * 
 * Get Magnitude of Zernike moment as feature
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 * @param <T>
 *            Input Type
  * @param <O>
 *            Output Type
 */
@Plugin(type = ZernikeFeature.class, label = "Zernike: Magnitude", name = Zernike.Magnitude.NAME)
public class DefaultMagnitudeFeature<T extends RealType<T>, O extends RealType<O>> extends AbstractZernikeFeature<T,O> implements Magnitude {

	@Override
	public void compute(IterableInterval<T> input, O output) {
		
		output.setReal(getZernikeMoment(input).getMagnitude());
	}

}
