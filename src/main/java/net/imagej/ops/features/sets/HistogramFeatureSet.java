package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.OpService;
import net.imagej.ops.features.DefaultFeatureResult;


import net.imagej.ops.features.FeatureResult;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.histogram.HistogramCreate;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate the histogram as a feature.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 */
@Plugin(type = FeatureSet.class, label = "Histogram Features")
public class HistogramFeatureSet<T extends RealType<T>> extends
        AbstractOutputFunction<Iterable<T>, List<FeatureResult>> implements
        FeatureSet<Iterable<T>> {

    @Parameter
    private OpService ops;

    @Parameter
    private int numBins = 256;

    private HistogramCreate<T> op;

    @Override
    public List<FeatureResult> createOutput(Iterable<T> input) {
        return new ArrayList<FeatureResult>(numBins);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<FeatureResult> safeCompute(Iterable<T> input,
            final List<FeatureResult> output) {
        output.clear();

        if (op == null) {
            op = ops.op(HistogramCreate.class, input, numBins);
        }

        op.run();
        op.getHistogram().countData(input);

        final Iterator<LongType> it = op.getHistogram().iterator();

        for (int i = 0; i < numBins; i++) {
            output.add(new DefaultFeatureResult("Histogram [" + i + "]",
                    (double) it.next().get()));
        }

        return output;
    }
}
