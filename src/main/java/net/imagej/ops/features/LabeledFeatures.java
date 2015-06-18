package net.imagej.ops.features;

import java.util.List;

import net.imglib2.util.Pair;

public interface LabeledFeatures<I, O> {

	List<Pair<String, O>> getFeatureList(I input);

}
