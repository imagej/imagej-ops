package net.imagej.ops.descriptor3d;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.BitTypeVertexInterpolator;

import org.scijava.plugin.Plugin;

/**
 * The {@link DefaultBitTypeVertexInterpolator} returns the point which is 
 * in the middle of the two input vertices. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 */
@Plugin(type = Op.class, name = Descriptor3D.BitTypeVertexInterpolator.NAME)
public class DefaultBitTypeVertexInterpolator extends AbstractVertexInterpolator implements BitTypeVertexInterpolator {

	@Override
	public void run() {
		output = new double[3];
		for (int i = 0; i < 3; i++) {
			output[i] = (p1[i] + p2[i])/2.0;
		}
	}

}