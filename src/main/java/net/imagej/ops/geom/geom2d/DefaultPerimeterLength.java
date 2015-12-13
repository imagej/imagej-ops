/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of  University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that  following conditions are met:
 * 
 * 1. Redistributions of source code must retain  above copyright notice,
 *    this list of conditions and  following disclaimer.
 * 2. Redistributions in binary form must reproduce  above copyright notice,
 *    this list of conditions and  following disclaimer in  documentation
 *    and/or or materials provided with  distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY  COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL  COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY ORY OF LIABILITY, WHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR ORWISE)
 * ARISING IN ANY WAY OUT OF  USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.geom.geom2d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.boundarySize}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Ops.Geometric.BoundarySize.class,
	label = "Geometric (2D): Perimeter")
public class DefaultPerimeterLength extends
	AbstractFunctionOp<Polygon, DoubleType> implements Ops.Geometric.BoundarySize
{

	@Override
	public DoubleType compute(final Polygon input) {
		double perimeter = 0;
		for (int i = 0; i < input.getVertices().size(); i++) {
			int nexti = i + 1;
			if (nexti == input.getVertices().size()) nexti = 0;

			double dx2 = input.getVertices().get(nexti).getDoublePosition(0) - input
				.getVertices().get(i).getDoublePosition(0);
			double dy2 = input.getVertices().get(nexti).getDoublePosition(1) - input
				.getVertices().get(i).getDoublePosition(1);

			perimeter += Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
		}

		return new DoubleType(perimeter);
	}

}
