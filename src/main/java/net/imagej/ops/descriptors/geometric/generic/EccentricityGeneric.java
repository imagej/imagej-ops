package net.imagej.ops.descriptors.geometric.generic;

import java.awt.geom.Point2D;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.geometric.Eccentricity;
import net.imagej.ops.descriptors.geometric.SmallestEnclosingRectangle;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Eccentricity}. Use {@link DescriptorService}
 * to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, label = Eccentricity.LABEL, name = Eccentricity.NAME, priority = Priority.FIRST_PRIORITY)
public class EccentricityGeneric implements Eccentricity {

	@Parameter(type = ItemIO.INPUT)
	private SmallestEnclosingRectangle result;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void run() {

		// find length and width
		List<Point2D> output = result.getOutput();

		// find x difference and y difference
		double dx = 0;
		double dy = 0;

		for (int i = 1; i < output.size(); i++) {
			double temp_dx = Math.abs(output.get(0).getX()
					- output.get(i).getX()) + 1;
			double temp_dy = Math.abs(output.get(0).getY()
					- output.get(i).getY()) + 1;

			if (temp_dx > dx) {
				dx = temp_dx;
			}

			if (temp_dy > dy) {
				dy = temp_dy;
			}
		}

		double eccentricity = 0;
		if (dx > dy) {
			eccentricity = dx / dy;
		} else {
			eccentricity = dy / dx;
		}

		out = new DoubleType(eccentricity);
	}
}
