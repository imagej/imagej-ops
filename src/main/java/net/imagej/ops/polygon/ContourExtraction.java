package net.imagej.ops.polygon;

import java.awt.Polygon;

import net.imagej.ops.OutputOp;

public interface ContourExtraction extends OutputOp<Polygon> {

	final static String NAME = "contour";

	final static String LABEL = "Contour Extraction";
}
