package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric3D.Compactness;
import net.imagej.ops.Ops.Geometric3D.ConvexHullSurfaceArea;
import net.imagej.ops.Ops.Geometric3D.ConvexHullSurfacePixel;
import net.imagej.ops.Ops.Geometric3D.ConvexHullVolume;
import net.imagej.ops.Ops.Geometric3D.Convexity;
import net.imagej.ops.Ops.Geometric3D.MainElongation;
import net.imagej.ops.Ops.Geometric3D.MedianElongation;
import net.imagej.ops.Ops.Geometric3D.Rugosity;
import net.imagej.ops.Ops.Geometric3D.Solidity;
import net.imagej.ops.Ops.Geometric3D.Spareness;
import net.imagej.ops.Ops.Geometric3D.Sphericity;
import net.imagej.ops.Ops.Geometric3D.SurfaceArea;
import net.imagej.ops.Ops.Geometric3D.SurfacePixel;
import net.imagej.ops.Ops.Geometric3D.Volume;
import net.imglib2.roi.labeling.LabelRegion;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<I, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "3D Geometric Features", description = "Calculates the 3D Geometric Features")
public class Geometric3DFeatureSet<L, O> extends AbstractOpRefFeatureSet<LabelRegion<L>, O>
		implements DimensionBoundFeatureSet<LabelRegion<L>, O> {

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Compactness.class));
		refs.add(ref(ConvexHullSurfaceArea.class));
		refs.add(ref(ConvexHullSurfacePixel.class));
		refs.add(ref(ConvexHullVolume.class));
		refs.add(ref(Convexity.class));
		refs.add(ref(MainElongation.class));
		refs.add(ref(MedianElongation.class));
		refs.add(ref(Rugosity.class));
		refs.add(ref(Solidity.class));
		refs.add(ref(Spareness.class));
		refs.add(ref(Sphericity.class));
		refs.add(ref(SurfaceArea.class));
		refs.add(ref(SurfacePixel.class));
		refs.add(ref(Volume.class));

		return refs;
	}

	@Override
	public int getMinDimensions() {
		return 3;
	}

	@Override
	public int getMaxDimensions() {
		return 3;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 3;
	}

}
