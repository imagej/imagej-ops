package net.imagej.ops.descriptors.geometric;

import java.awt.Polygon;

import net.imagej.ops.OutputOp;

public interface ConvexHull extends OutputOp<Polygon> {

    public final String NAME = "convexhull";
    public final String LABEL = "Convex Hull";

}
