package net.imagej.ops.descriptor3d;


import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.AbstractOp;
import net.imagej.ops.Op;

/**
 * This is the {@link AbstractVertexInterpolator}. A vertex interpolator
 * computes the real coordinates based on the pixel intensities. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public abstract class AbstractVertexInterpolator extends AbstractOp implements VertexInterpolator {
	
	@Parameter(type = ItemIO.INPUT)
	int[] p1;
	
	@Parameter(type = ItemIO.INPUT)
	int[] p2;
	
	@Parameter(type = ItemIO.INPUT)
	double p1Value;
	
	@Parameter(type = ItemIO.INPUT)
	double p2Value;
	
	@Parameter(type = ItemIO.OUTPUT)
	double[] output;
}