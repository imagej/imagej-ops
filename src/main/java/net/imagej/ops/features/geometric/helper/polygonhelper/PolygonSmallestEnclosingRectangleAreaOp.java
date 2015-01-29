package net.imagej.ops.features.geometric.helper.polygonhelper;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputOp;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "PolygonSmallestEnclosingRectangleAreaProvider")
public class PolygonSmallestEnclosingRectangleAreaOp implements
        OutputOp<DoubleType> {

    @Parameter(type = ItemIO.OUTPUT)
    private DoubleType out;

    @Parameter(type = ItemIO.INPUT)
    private PolygonSmallestEnclosingRectangleOp in;

    @Parameter(type = ItemIO.INPUT)
    private OpService ops;

    @Override
    public void run() {

        if (out == null) {
            out = new DoubleType();
        }

        out.setReal(((DoubleType) ops.run(Area.class, in.getOutput()))
                .getRealDouble());
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
