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
package net.imagej.ops.geom.geom3d.mesh;

import net.imagej.ops.Ops;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Linearly interpolate the position where an isosurface cuts an edge
 * between two vertices, each with their own scalar value
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Ops.Geometric.VertexInterpolator.class)
public class DefaultVertexInterpolator extends AbstractVertexInterpolator {

	@Parameter(type = ItemIO.INPUT)
	double isolevel;
	
	@Override
	public void run() {
		output = new double[3];

		if (Math.abs(isolevel - p1Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p1[i];
			}
		} else if (Math.abs(isolevel - p2Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p2[i];
			}
		} else if (Math.abs(p1Value - p2Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p1[i];
			}
		} else {
			double mu = (isolevel - p1Value) / (p2Value - p1Value);

			output[0] = p1[0] + mu * (p2[0] - p1[0]);
			output[1] = p1[1] + mu * (p2[1] - p1[1]);
			output[2] = p1[2] + mu * (p2[2] - p1[2]);
		}
	}

	@Override
	public void setIsoLevel(double d) {
		isolevel = d;
	}

}
