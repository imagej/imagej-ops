package net.imagej.ops.statistics.geometric.polygon;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculates the area of a polygon
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultAreaPolygon<I extends Polygon, O extends RealType<O>>
        extends AbstractOutputFunction<Polygon, O> implements AreaFeature<O>, Area {

    @Override
    public O createOutput(Polygon input) {
        return OpUtils.<O> cast(new DoubleType());
    }

    @Override
    protected O safeCompute(Polygon input, O output) {
        double sum = 0;
        for (int i = 0; i < input.size() - 1; i++) {

            RealPoint p0 = input.getPoint(i);
            RealPoint p1 = input.getPoint(i + 1);

            double p0_x = p0.getDoublePosition(0);
            double p0_y = p0.getDoublePosition(1);
            double p1_x = p1.getDoublePosition(0);
            double p1_y = p1.getDoublePosition(1);

            sum += p0_x * p1_y - p0_y * p1_x;
        }

        output.setReal(Math.abs(sum) / 2d);
        return output;
    }

}