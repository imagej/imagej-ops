package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.histogram.HistogramCreate;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate the histogram as a feature.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 */
@Plugin(type = Op.class, label = "Histogram Features")
public class HistogramFeatureSet<T extends RealType<T>> extends
        AbstractFeatureSet<Iterable<T>> {

    @Parameter
    private OpService ops;

    @Parameter
    private int numBins = 256;

    private HistogramCreate<T> op;

    @Override
    public List<Pair<String, DoubleType>> createOutput(Iterable<T> input) {
        return new ArrayList<Pair<String, DoubleType>>(numBins);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<Pair<String, DoubleType>> safeCompute(Iterable<T> input,
            List<Pair<String, DoubleType>> output) {
        output.clear();

        if (op == null) {
            op = ops.op(HistogramCreate.class, input, numBins);
        }

        op.run();
        op.getHistogram().countData(input);

        final Iterator<LongType> it = op.getHistogram().iterator();

        for (int i = 0; i < numBins; i++) {
            output.add(new ValuePair<String, DoubleType>("Histogram [" + i
                    + "]", new DoubleType(it.next().get())));
        }

        return output;
    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        
    }

}