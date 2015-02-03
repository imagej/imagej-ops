package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.histogram.HistogramCreate;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
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
@Plugin(type = FeatureSet.class, label = "Histogram Features")
public class HistogramFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<Iterable<T>, LongType> {

	@Parameter
	private OpService ops;

	@Parameter
	private int numBins = 256;

	private HistogramCreate<T> op;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {

		if (op == null) {
			op = ops.op(HistogramCreate.class, getInput(), numBins);
		}

		op.run();
		op.getOutput().countData(getInput());

		final Map<OpRef<? extends Op>, Op> output = new HashMap<OpRef<? extends Op>, Op>();

		output.put(new OpRef<HistogramCreate>(HistogramCreate.class, numBins),
				op);

		setOutput(output);
	}

	@Override
	public List<Pair<String, LongType>> getFeatures(final Iterable<T> input) {
		compute(input);
		final List<Pair<String, LongType>> res = new ArrayList<Pair<String, LongType>>();
		int n = 0;
		for (LongType type : op.getOutput()) {
			res.add(new ValuePair<String, LongType>("Histogram [" + ++n + "]",
					type));
		}

		return res;
	}
}