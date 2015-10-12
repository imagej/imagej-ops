package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.Boxivity;
import net.imagej.ops.Ops.Geometric.Circularity;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.Eccentricity;
import net.imagej.ops.Ops.Geometric.FeretsAngle;
import net.imagej.ops.Ops.Geometric.FeretsDiameter;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MajorAxis;
import net.imagej.ops.Ops.Geometric.MinorAxis;
import net.imagej.ops.Ops.Geometric.Roundness;
import net.imagej.ops.Ops.Geometric.Rugosity;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imglib2.roi.geometric.Polygon;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<I, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "2D Geometric Features", description = "Calculates the 2D Geometric Features")
public class Geometric2DFeatureSet<L, O> extends AbstractOpRefFeatureSet<Polygon, O>
		implements GeometricFeatureSet<L, O> {

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Size.class));
		refs.add(ref(Circularity.class));
		refs.add(ref(Convexity.class));
		refs.add(ref(Eccentricity.class));
		refs.add(ref(MainElongation.class));
		refs.add(ref(FeretsAngle.class));
		refs.add(ref(FeretsDiameter.class));
		refs.add(ref(MajorAxis.class));
		refs.add(ref(MinorAxis.class));
		refs.add(ref(BoundarySize.class));
		refs.add(ref(Boxivity.class));
		refs.add(ref(Roundness.class));
		refs.add(ref(Rugosity.class));
		refs.add(ref(Solidity.class));

		return refs;
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

}
