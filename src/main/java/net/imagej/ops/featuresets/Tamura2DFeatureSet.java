package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Tamura.Coarseness;
import net.imagej.ops.Ops.Tamura.Contrast;
import net.imagej.ops.Ops.Tamura.Directionality;
import net.imagej.ops.features.haralick.HaralickFeature;
import net.imglib2.IterableInterval;

/**
 * {@link FeatureSet} for {@link HaralickFeature}s
 * 
 * @author Christian Dietz, University of Konstanz
 *
 * @param <T>
 * @param <O>w
 */
@Plugin(type = FeatureSet.class, label = "Tamura Features", description = "Calculates the Tamura Features")
public class Tamura2DFeatureSet<T, O> extends AbstractOpRefFeatureSet<IterableInterval<T>, O>
		implements Contingent, IntensityFeatureSet<IterableInterval<T>, O> {

	@Parameter(type = ItemIO.INPUT, label = "Histogram Size", description = "The size of the histogram used by the directionality feature.", min = "1", max = "2147483647", stepSize = "1")
	private int histogramSize = 16;

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final HashSet<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Coarseness.class));
		refs.add(ref(Contrast.class));
		refs.add(ref(Directionality.class, histogramSize));

		return refs;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}
}