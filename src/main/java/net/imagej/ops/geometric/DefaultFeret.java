
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Feret;

import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

/**
 * Generic implementation of {@link Feret}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Feret",
	name = Geometric2D.Feret.NAME)
public class DefaultFeret extends
	AbstractFunctionOp<Polygon, Pair<RealLocalizable, RealLocalizable>>implements
	GeometricOp<Polygon, Pair<RealLocalizable, RealLocalizable>>, Contingent,
	Geometric2D.Feret
{

	@Override
	public Pair<RealLocalizable, RealLocalizable> compute(Polygon input) {

		double distance = Double.NEGATIVE_INFINITY;
		int in0 = -1;
		int in1 = -1;

		for (int i = 0; i < input.getVertices().size(); i++) {
			for (int j = i + 1; j < input.getVertices().size(); j++) {
				RealLocalizable temp0 = input.getVertices().get(i);
				RealLocalizable temp1 = input.getVertices().get(j);

				double sum = 0;
				for (int k = 0; k < temp0.numDimensions(); k++) {
					sum += Math.pow(temp0.getDoublePosition(k) - temp1.getDoublePosition(
						k), 2);
				}
				sum = Math.sqrt(sum);

				if (sum > distance) {
					distance = sum;
					in0 = i;
					in1 = j;
				}
			}
		}

		return new ValuePair<RealLocalizable, RealLocalizable>(input.getVertices()
			.get(in0), input.getVertices().get(in1));
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
