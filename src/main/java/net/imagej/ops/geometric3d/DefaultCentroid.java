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
package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.Centroid;
import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;

import org.scijava.plugin.Plugin;

/**
 * This {@link Op} computes the centroid of a {@link IterableRegion} (Label).
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 * @param <B> a Boolean Type
 */
@Plugin(type = Op.class, name = Descriptor3D.Centroid.NAME)
public class DefaultCentroid<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, double[]> implements Centroid {

	@Override
	public double[] compute(final IterableRegion<B> input) {
		int numDimensions = input.numDimensions();
		double[] output = new double[numDimensions];
		Cursor<Void> c = input.localizingCursor();
		while (c.hasNext()) {
			c.fwd();
			double[] pos = new double[numDimensions];
			c.localize(pos);
			for (int i = 0; i < output.length; i++) {
				output[i] += pos[i];
			}
		}

		for (int i = 0; i < output.length; i++) {
			output[i] = output[i] / input.size();
		}
		
		return output;
	}

}
