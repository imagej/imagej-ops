package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.RectangularityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleAreaOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link RectangularityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RectangularityFeature.NAME, label = RectangularityFeature.LABEL)
public class DefaultRectangularityFeature implements
        RectangularityFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonAreaOp area;

    @Parameter(type = ItemIO.INPUT)
    private PolygonSmallestEnclosingRectangleAreaOp serArea;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(area.getOutput().getRealDouble()
                / serArea.getOutput().getRealDouble());
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
