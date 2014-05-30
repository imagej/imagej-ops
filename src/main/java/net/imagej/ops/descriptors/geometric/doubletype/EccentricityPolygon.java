/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.descriptors.geometric.doubletype;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.descriptors.geometric.Eccentricity;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Calculating {@link Eccentricity} on {@link Polygon}. TODO: REVIEW
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Eccentricity.class, name = Eccentricity.NAME, label = Eccentricity.LABEL)
public class EccentricityPolygon extends AbstractFunction<Polygon, DoubleType>
		implements Eccentricity {

	// TODO REVIEW. This implementation is not correct!!

	@Override
	public DoubleType compute(final Polygon input, DoubleType output) {
		if(output == null){
			output = new DoubleType();
		}

		final Rectangle2D rec = input.getBounds2D();

		output.set((rec.getWidth() > rec.getHeight()) ? rec.getWidth()
				/ rec.getHeight() : rec.getHeight() / rec.getWidth());

		return output;
	}

}
