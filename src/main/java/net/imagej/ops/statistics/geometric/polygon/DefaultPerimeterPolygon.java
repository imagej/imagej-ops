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
        int sumdx = 0;
        int sumdy = 0;
        int nCorners = 0;
        double dx1 = input.getPoint(0).getDoublePosition(0)
                - input.getPoint(input.size() - 1).getDoublePosition(0);
        double dy1 = input.getPoint(0).getDoublePosition(1)
                - input.getPoint(input.size() - 1).getDoublePosition(1);

        double side1 = Math.abs(dx1) + Math.abs(dy1); // one of these is 0
        boolean corner = false;
        int nexti;
        double dx2, dy2, side2;
        for (int i = 0; i < input.size(); i++) {
            nexti = i + 1;
            if (nexti == input.size())
                nexti = 0;

            dx2 = input.getPoint(nexti).getDoublePosition(0)
                    - input.getPoint(i).getDoublePosition(0);
            dy2 = input.getPoint(nexti).getDoublePosition(1)
                    - input.getPoint(i).getDoublePosition(1);

            sumdx += Math.abs(dx1);
            sumdy += Math.abs(dy1);
            side2 = Math.abs(dx2) + Math.abs(dy2);
            if (side1 > 1 || !corner) {
                corner = true;
                nCorners++;
            } else
                corner = false;
            dx1 = dx2;
            dy1 = dy2;
            side1 = side2;
        }

        double perimeter = sumdx + sumdy - (nCorners * (2 - Math.sqrt(2)));

        output.setReal(perimeter);
        return output;
    }

}