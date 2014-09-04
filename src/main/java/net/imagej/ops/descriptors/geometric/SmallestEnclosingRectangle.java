package net.imagej.ops.descriptors.geometric;

import java.awt.geom.Point2D;
import java.util.List;

import net.imagej.ops.OutputOp;

/**
 * Finds the smallest enclosing rectangle of an object using it's convex hull.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public interface SmallestEnclosingRectangle extends OutputOp<List<Point2D>> {

	public final String NAME = "smallestenclosingrectangle";
	public final String LABEL = "Smallest Enclosing Rectangle";

}
