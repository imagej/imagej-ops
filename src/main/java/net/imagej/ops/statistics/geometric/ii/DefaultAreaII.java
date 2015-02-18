package net.imagej.ops.statistics.geometric.ii;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.LABEL, priority = Priority.FIRST_PRIORITY)
public class DefaultAreaII<I extends RealType<I>, O extends RealType<O>>
        extends AbstractOutputFunction<IterableInterval<I>, O> implements
        AreaFeature<O>, Area {

    @Override
    public O createOutput(IterableInterval<I> input) {
        return OpUtils.<O> cast(new DoubleType());
    }

    @Override
    protected O safeCompute(IterableInterval<I> input, O output) {
        output.setReal(input.size());
        return output;
    }

}
