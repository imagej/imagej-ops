package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment1Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment1Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment1Feature.NAME, label = HuMoment1Feature.LABEL)
public class DefaultHuMoment1Feature implements HuMoment1Feature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment02Feature<DoubleType> n02;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment20Feature<DoubleType> n20;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        out.setReal(n20.getOutput().getRealDouble()
                + n02.getOutput().getRealDouble());
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
