package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.EccentricityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link EccentricityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = EccentricityFeature.NAME, label = EccentricityFeature.LABEL)
public class DefaultEccentricityFeature implements
        EccentricityFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private MajorAxisFeature<DoubleType> majorAxis;

    @Parameter(type = ItemIO.INPUT)
    private MinorAxisFeature<DoubleType> minorAxis;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;


    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        
        out.setReal(majorAxis.getOutput().getRealDouble()
                / minorAxis.getOutput().getRealDouble());
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
