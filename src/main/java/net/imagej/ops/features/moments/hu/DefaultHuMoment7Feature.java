package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment2Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment7Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment7Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment7Feature.NAME, label = HuMoment7Feature.LABEL)
public class DefaultHuMoment7Feature implements HuMoment7Feature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment02Feature<DoubleType> n02;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment03Feature<DoubleType> n03;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment11Feature<DoubleType> n11;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment12Feature<DoubleType> n12;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment20Feature<DoubleType> n20;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment21Feature<DoubleType> n21;

    @Parameter(type = ItemIO.INPUT)
    private NormalizedCentralMoment30Feature<DoubleType> n30;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal((3 * n21.getOutput().getRealDouble() - n03.getOutput()
                .getRealDouble())
                * (n30.getOutput().getRealDouble() + n12.getOutput()
                        .getRealDouble())
                * (Math.pow(n30.getOutput().getRealDouble()
                        + n12.getOutput().getRealDouble(), 2) - 3 * Math.pow(
                        n21.getOutput().getRealDouble()
                                + n03.getOutput().getRealDouble(), 2))
                - (n30.getOutput().getRealDouble() - 3 * n12.getOutput()
                        .getRealDouble())
                * (n21.getOutput().getRealDouble() + n03.getOutput()
                        .getRealDouble())
                * (3 * Math.pow(n30.getOutput().getRealDouble()
                        + n12.getOutput().getRealDouble(), 2) - Math.pow(n21
                        .getOutput().getRealDouble()
                        + n03.getOutput().getRealDouble(), 2)));
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
