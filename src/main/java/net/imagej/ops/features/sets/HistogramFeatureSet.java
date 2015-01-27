package net.imagej.ops.features.sets;

import java.util.HashMap;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.AbstractFeatureSet;
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
@Plugin(type = Op.class, label = "Histogram Features")
public class HistogramFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<Iterable<T>, LongType[]> implements
		FeatureSet<Iterable<T>, LongType[]> {

	@Parameter
	private OpService ops;

	@Parameter
	private int numBins = 256;

	private HistogramCreate<T> op;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		if (op == null) {
			op = ops.op(HistogramCreate.class, getInput(), numBins);
		}

		op.run();
		op.getHistogram().countData(getInput());

		final Map<OpRef, LongType[]> output = new HashMap<OpRef, LongType[]>();
		final LongType[] res = new LongType[256];
		int i = 0;
		for (LongType type : op.getHistogram()) {
			res[i++] = type;
		}

		output.put(new OpRef(HistogramCreate.class, 256), res);
	}
}