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

package net.imagej.ops.geom.geom3d;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.geom.AbstractBoundarySizeConvexHull;
import net.imagej.ops.geom.geom3d.mesh.Mesh;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric.BoundarySizeConvexHull.NAME,
	label = "Geometric (3D): Surface AreaO Convex Hull",
	priority = Priority.VERY_HIGH_PRIORITY)
public class BoundarySizeConvexHullMesh extends
	AbstractBoundarySizeConvexHull<Mesh>
{

	public BoundarySizeConvexHullMesh() {
		super(Mesh.class);
	}

}
