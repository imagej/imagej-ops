package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.ElongationFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link ElongationFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = ElongationFeature.NAME, label = ElongationFeature.LABEL)
public class DefaultElongationFeature implements ElongationFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private MinorAxisFeature<DoubleType> minorAxis;

    @Parameter(type = ItemIO.INPUT)
    private MajorAxisFeature<DoubleType> majorAxis;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(1 - (minorAxis.getOutput().getRealDouble() / majorAxis
                .getOutput().getRealDouble()));
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
