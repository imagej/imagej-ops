package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.MinorMajorAxisOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link MinorAxisFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = MinorAxisFeature.NAME, label = MinorAxisFeature.LABEL)
public class DefaultMinorAxisFeature implements MinorAxisFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private MinorMajorAxisOp axisProvider;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if(out == null){
            out = new DoubleType();
        }
        
        out.setReal(axisProvider.getOutput().getA());
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
