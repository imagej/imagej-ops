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
package net.imagej.ops.geom;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.geom.helper.Polytope;
import net.imagej.ops.geom.helper.ThePolygon;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link BoundarySize}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Perimeter", name = Geometric.BoundarySize.NAME)
public class DefaultPerimeter extends AbstractFunctionOp<ThePolygon, DoubleType>
		implements
		Geometric.BoundarySize {

	@Override
	public DoubleType compute(final ThePolygon input) {
		double perimeter = 0;
		for (int i = 0; i < input.getVertices().size(); i++) {
			int nexti = i + 1;
			if (nexti == input.getVertices().size())
				nexti = 0;

			double dx2 = input.getVertices().get(nexti).getDoublePosition(0)
					- input.getVertices().get(i).getDoublePosition(0);
			double dy2 = input.getVertices().get(nexti).getDoublePosition(1)
					- input.getVertices().get(i).getDoublePosition(1);

			perimeter += Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
		}

		return new DoubleType(perimeter);
	}

}
