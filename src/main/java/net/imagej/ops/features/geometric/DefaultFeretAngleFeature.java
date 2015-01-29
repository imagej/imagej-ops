package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretOp;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link FeretsAngleFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = FeretsAngleFeature.NAME, label = FeretsAngleFeature.LABEL)
public class DefaultFeretAngleFeature implements FeretsAngleFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonFeretOp feretResult;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        
        RealPoint p1 = feretResult.getOutput().getA();
        RealPoint p2 = feretResult.getOutput().getB();

        if (p1.getDoublePosition(0) == p2.getDoublePosition(0)) {
            out.setReal(90);
        }

        // tan alpha = opposite leg / adjacent leg
        // angle in radiants = atan(alpha)
        // angle in degree = atan(alpha) * (180/pi)
        final double opLeg = p2.getDoublePosition(1) - p1.getDoublePosition(1);
        final double adjLeg = p2.getDoublePosition(0) - p1.getDoublePosition(0);
        double degree = Math.atan((opLeg / adjLeg)) * (180.0 / Math.PI);
        if (adjLeg < 0) {
            degree = 180 - degree;
        }

        out.setReal(Math.abs(degree));
    }

    @Override
    public DoubleType getOutput() {
        return out;
    }

    @Override
    public void setOutput(DoubleType output) {
        this.out = output;
    }
}
