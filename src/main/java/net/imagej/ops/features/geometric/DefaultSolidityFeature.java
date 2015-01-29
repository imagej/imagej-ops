package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullAreaOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link SolidityFeature}. Use {@link FeatureService}
 * to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = SolidityFeature.NAME, label = SolidityFeature.LABEL)
public class DefaultSolidityFeature implements SolidityFeature<DoubleType> {

    @Parameter(type = ItemIO.INPUT)
    private PolygonAreaOp area;

    @Parameter(type = ItemIO.INPUT)
    private PolygonConvexHullAreaOp convexHullArea;

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Override
    public void run() {

        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(area.getOutput().getRealDouble()
                / convexHullArea.getOutput().getRealDouble());
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