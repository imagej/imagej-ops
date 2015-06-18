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
import net.imagej.ops.features.LabeledFeatures;
import net.imagej.ops.histogram.HistogramCreate;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate the histogram as a feature.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 */
@Plugin(type = FeatureSet.class, label = "Histogram Features", description = "Calculates the Histogram Features")
public class HistogramFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<Iterable<T>, Histogram1d<T>> implements
		LabeledFeatures<Iterable<T>, LongType> {

	@Parameter
	private OpService ops;

	@Parameter(type = ItemIO.INPUT, label = "Number of Bins", description = "The number of bins of the histogram", min = "1", max = "2147483647", stepSize = "1")
	private int numBins = 256;

	private HistogramCreate<T> op;

	private Map<OpRef<? extends Op>, Histogram1d<T>> output;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {

		if (op == null) {
			op = ops.op(HistogramCreate.class, getInput(), numBins);
			output = new HashMap<OpRef<? extends Op>, Histogram1d<T>>();
			setOutput(output);
		}

		op.run();
		op.getOutput().countData(getInput());

		final OpRef<HistogramCreate> ref = new OpRef<HistogramCreate>(
				HistogramCreate.class, numBins);

		output.clear();
		output.put(ref, op.getOutput());
	}

	@Override
	public List<Pair<String, LongType>> getFeatureList(Iterable<T> input) {
		compute(input);

		final List<Pair<String, LongType>> res = new ArrayList<Pair<String, LongType>>();

		int n = 0;
		for (LongType type : op.getOutput()) {
			res.add(new ValuePair<String, LongType>("Histogram [" + ++n + "]",
					type));
		}

		return res;
	}

	@Override
	public Map<OpRef<? extends Op>, Histogram1d<T>> getFeaturesByRef(
			Iterable<T> input) {
		return compute(input);
	}
}