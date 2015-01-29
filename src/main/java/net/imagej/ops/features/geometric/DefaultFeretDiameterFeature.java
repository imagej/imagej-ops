package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretOp;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link FeretsDiameterFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = FeretsDiameterFeature.NAME, label = FeretsDiameterFeature.LABEL)
public class DefaultFeretDiameterFeature implements
        FeretsDiameterFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonFeretOp feretResult;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {

        if(out == null){
            out = new DoubleType();
        }
        
        RealPoint p1 = feretResult.getOutput().getA();
        RealPoint p2 = feretResult.getOutput().getB();

        out.setReal(Math.hypot(
                p1.getDoublePosition(0) - p2.getDoublePosition(0),
                p1.getDoublePosition(1) - p2.getDoublePosition(1)));
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
