package net.imagej.ops.features.moments.centralmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment21Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link CentralMoment21Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CentralMoment21Feature.NAME, label = CentralMoment21Feature.LABEL)
public class DefaultCentralMoment21Feature implements CentralMoment21Feature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private CentralMomentsHelper centralMomentsHelper;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        out.setReal(centralMomentsHelper.getOutput().getCentralMoment21());
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
