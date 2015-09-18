package net.imagej.ops.descriptor3d;

import net.imagej.ops.Op;

import org.scijava.plugin.Plugin;

/**
 * The {@link BitTypeVertexInterpolator} returns the point which is 
 * in the middle of the two input vertices. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 */
@Plugin(type = Op.class, name = "bittypevertexinterpolator")
public class BitTypeVertexInterpolator extends AbstractVertexInterpolator {

	@Override
	public void run() {
		output = new double[3];
		for (int i = 0; i < 3; i++) {
			output[i] = (p1[i] + p2[i])/2.0;
		}
	}

}