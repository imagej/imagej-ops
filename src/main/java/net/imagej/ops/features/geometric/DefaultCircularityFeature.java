package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link CircularityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CircularityFeature.NAME , label = CircularityFeature.LABEL)
public class DefaultCircularityFeature implements
        CircularityFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonAreaOp area;

    @Parameter(type = ItemIO.INPUT)
    private PolygonPerimeterOp perimter;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }
        
        out.setReal(4
                * Math.PI
                * (area.getOutput().getRealDouble() / Math.pow(
                        perimter.getOutput().getRealDouble(), 2)));
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
