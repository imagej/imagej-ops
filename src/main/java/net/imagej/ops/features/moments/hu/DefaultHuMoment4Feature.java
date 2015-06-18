package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment4Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment4Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment4Feature.NAME, label = HuMoment4Feature.LABEL)
public class DefaultHuMoment4Feature implements HuMoment4Feature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment30Feature<DoubleType> n30;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment12Feature<DoubleType> n12;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment21Feature<DoubleType> n21;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment03Feature<DoubleType> n03;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(Math.pow(n30.getOutput().getRealDouble()
                + n12.getOutput().getRealDouble(), 2)
                + Math.pow(n21.getOutput().getRealDouble()
                        + n03.getOutput().getRealDouble(), 2));
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
