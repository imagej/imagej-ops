package net.imagej.ops.featuresets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.scijava.plugin.Plugin;

import net.imglib2.RealLocalizable;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<I, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@SuppressWarnings("rawtypes")
@Plugin(type = FeatureSet.class, label = "Centroid", description = "Calculates the Centroid")
public class CentroidFeatureSet extends AbstractCachedFeatureSet<LabelRegion, DoubleType> {

	@Override
	public Collection<NamedFeature> getFeatures() {
		List<NamedFeature> fs = new ArrayList<NamedFeature>();
		
		for (int i = 0; i < in().numDimensions(); i++) {
			fs.add(new NamedFeature("Centroid of dimension#" + i));
		}
		return fs;
	}

	@Override
	public Map<NamedFeature, DoubleType> compute(LabelRegion input) {
		Map<NamedFeature, DoubleType> res = new LinkedHashMap<NamedFeature, DoubleType>();
		RealLocalizable centroid = ops().geom().centroid(input);
		
		for (int i = 0; i < getFeatures().size(); i++) {
			res.put(new NamedFeature("Centroid of dimension#" + i), new DoubleType(centroid.getDoublePosition(i)));
		}
		
		return res;
	}


}
