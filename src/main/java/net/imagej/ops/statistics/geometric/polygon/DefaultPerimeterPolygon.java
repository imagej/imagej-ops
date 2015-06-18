package net.imagej.ops.statistics.geometric.polygon;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imagej.ops.statistics.GeometricOps.Perimeter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * 
 * Computes the perimeter of a polygon.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = Perimeter.NAME, label = Perimeter.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultPerimeterPolygon<I extends Polygon, O extends RealType<O>>
		extends AbstractOutputFunction<Polygon, O> implements
		PerimeterFeature<O>, Perimeter {

	@Override
	public O createOutput(Polygon input) {
		return OpUtils.<O> cast(new DoubleType());
	}

	@Override
	protected O safeCompute(Polygon input, O output) {

		double perimeter = 0;
		for (int i = 0; i < input.size(); i++) {
			int nexti = i + 1;
			if (nexti == input.size())
				nexti = 0;

			double dx2 = input.getPoint(nexti).getDoublePosition(0)
					- input.getPoint(i).getDoublePosition(0);
			double dy2 = input.getPoint(nexti).getDoublePosition(1)
					- input.getPoint(i).getDoublePosition(1);

			perimeter += Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
		}

		output.setReal(perimeter);
		return output;
	}

}