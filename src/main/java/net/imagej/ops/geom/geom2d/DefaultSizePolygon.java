/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops.geom.geom2d;

import java.awt.geom.Area;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Specific implementation of {@link Area} for a Polygon2D.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.Size.class, label = "Geometric (2D): Size",
	priority = Priority.VERY_HIGH - 1)
public class DefaultSizePolygon extends AbstractUnaryHybridCF<Polygon2D, DoubleType>
	implements Ops.Geometric.Size
{

	@Override
	public void compute(Polygon2D input, DoubleType output) {
		double sum = 0;
		final int numVertices = input.numVertices();
		for (int i = 0; i < numVertices; i++) {

			final RealLocalizable p0 = input.vertex(i);
			final RealLocalizable p1 = input.vertex((i + 1) % numVertices);

			final double p0_x = p0.getDoublePosition(0);
			final double p0_y = p0.getDoublePosition(1);
			final double p1_x = p1.getDoublePosition(0);
			final double p1_y = p1.getDoublePosition(1);

			sum += p0_x * p1_y - p0_y * p1_x;
		}
		output.set(Math.abs(sum) / 2d);
	}
	
	@Override
	public DoubleType createOutput(Polygon2D input) {
		return new DoubleType();
	}

}
