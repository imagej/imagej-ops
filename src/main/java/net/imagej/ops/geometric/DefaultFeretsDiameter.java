
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Feret;
import net.imagej.ops.Ops.Geometric2D.FeretsDiameter;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

/**
 * Generic implementation of {@link FeretsDiameter}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Ferets Diameter",
	name = Geometric2D.FeretsDiameter.NAME)
public class DefaultFeretsDiameter<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.FeretsDiameter
{

	@SuppressWarnings("rawtypes")
	private FunctionOp<Polygon, Pair> function;

	@Override
	public void initialize() {
		function = ops().function(Feret.class, Pair.class, in());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(Polygon input, O output) {
		Pair<RealLocalizable, RealLocalizable> ferets = function.compute(input);

		RealLocalizable p1 = ferets.getA();
		RealLocalizable p2 = ferets.getB();

		output.setReal(Math.hypot(p1.getDoublePosition(0) - p2.getDoublePosition(0),
			p1.getDoublePosition(1) - p2.getDoublePosition(1)));
	}

}
