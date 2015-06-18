package net.imagej.ops.statistics.geometric.i;

import java.util.Iterator;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.LABEL, priority = Priority.VERY_LOW_PRIORITY)
public class DefaultAreaI<I extends RealType<I>, O extends RealType<O>>
        extends AbstractOutputFunction<Iterable<I>, O> implements
        AreaFeature<O>, Area {

    @Override
    public O createOutput(Iterable<I> input) {
        return OpUtils.<O> cast(new DoubleType());
    }

    @Override
    protected O safeCompute(Iterable<I> input, O output) {
        double sum = 0;

        Iterator<?> iterator = input.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            ++sum;
        }

        output.setReal(sum);
        return output;
    }
}
