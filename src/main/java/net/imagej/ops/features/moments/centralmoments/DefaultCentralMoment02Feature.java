package net.imagej.ops.features.moments.centralmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment02Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link CentralMoment02Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CentralMoment02Feature.NAME, label = CentralMoment02Feature.LABEL)
public class DefaultCentralMoment02Feature implements
        CentralMoment02Feature<DoubleType> {
    @Parameter(type = ItemIO.INPUT)
    private CentralMomentsHelper centralMomentsHelper;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        out.setReal(centralMomentsHelper.getOutput().getCentralMoment02());
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
