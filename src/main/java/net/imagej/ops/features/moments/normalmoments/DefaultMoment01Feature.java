package net.imagej.ops.features.moments.normalmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment01Feature;
import net.imagej.ops.features.moments.helper.NormalMomentsHelper;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Moment01Feature}. Use {@link FeatureService}
 * to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = Moment01Feature.NAME, label = Moment01Feature.LABEL)
public class DefaultMoment01Feature implements Moment01Feature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private NormalMomentsHelper momentsHelper;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(momentsHelper.getOutput().getMoment01());
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
