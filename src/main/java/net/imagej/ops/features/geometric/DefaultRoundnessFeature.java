package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link RoundnessFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RoundnessFeature.NAME, label = RoundnessFeature.LABEL)
public class DefaultRoundnessFeature implements
        RoundnessFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private AreaFeature<DoubleType> area;

    @Parameter(type = ItemIO.INPUT)
    private MajorAxisFeature<DoubleType> majorAxis;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(4 * (area.getOutput().getRealDouble() / (Math.PI * Math
                .pow(majorAxis.getOutput().getRealDouble(), 2))));
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
