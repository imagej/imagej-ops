package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.ConvexityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RugosityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullPerimeterOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link ConvexityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RugosityFeature.NAME)
public class DefaultRugosityFeature implements
        RugosityFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonPerimeterOp perimter;

    @Parameter(type = ItemIO.INPUT)
    private PolygonConvexHullPerimeterOp convexHullPerimeter;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {
        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(perimter.getOutput().getRealDouble()
                / convexHullPerimeter.getOutput().getRealDouble() );
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
