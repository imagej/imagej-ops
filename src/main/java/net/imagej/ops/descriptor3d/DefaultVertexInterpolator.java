package net.imagej.ops.descriptor3d;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.VertexInterpolator;

/**
 * Linearly interpolate the position where an isosurface cuts an edge
 * between two vertices, each with their own scalar value
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Op.class, name = Descriptor3D.VertexInterpolator.NAME)
public class DefaultVertexInterpolator extends AbstractVertexInterpolator implements VertexInterpolator {

	
	@Parameter(type = ItemIO.INPUT)
	double isolevel;

	@Override
	public void run() {
		output = new double[3];

		if (Math.abs(isolevel - p1Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p1[i];
			}
		} else if (Math.abs(isolevel - p2Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p2[i];
			}
		} else if (Math.abs(p1Value - p2Value) < 0.00001) {
			for (int i = 0; i < 3; i++) {
				output[i] = p1[i];
			}
		} else {
			double mu = (isolevel - p1Value) / (p2Value - p1Value);

			output[0] = p1[0] + mu * (p2[0] - p1[0]);
			output[1] = p1[1] + mu * (p2[1] - p1[1]);
			output[2] = p1[2] + mu * (p2[2] - p1[2]);
		}
	}

}
