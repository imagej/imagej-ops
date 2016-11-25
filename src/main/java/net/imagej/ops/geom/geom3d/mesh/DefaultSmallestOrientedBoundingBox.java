package net.imagej.ops.geom.geom3d.mesh;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.smallestBoundingBox}.
 * 
 * This paper looks promising: http://clb.demon.fi/minobb/minobb.html 
 * c++ implementation:
 * https://github.com/juj/MathGeoLib/blob/master/src/Geometry/OBB.cpp (Licensed
 * under the Apache License, Version 2.0) example:
 * http://clb.demon.fi/minobb/test/ModelView.html
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Ops.Geometric.SmallestEnclosingBoundingBox.class, label = "Geometric (3D): Smallest OBB")
public class DefaultSmallestOrientedBoundingBox extends AbstractUnaryFunctionOp<Mesh, Mesh>
		implements Contingent, Ops.Geometric.SmallestEnclosingBoundingBox {

	@Override
	public Mesh calculate(Mesh input) {
		return null;
	}

	@Override
	public boolean conforms() {
		return false;
	}

}
