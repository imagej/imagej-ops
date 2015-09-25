package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric2D.Area;
import net.imagej.ops.Ops.Geometric2D.BoundingBox;
import net.imagej.ops.Ops.Geometric2D.Centroid;
import net.imagej.ops.Ops.Geometric2D.Circularity;
import net.imagej.ops.Ops.Geometric2D.Contour;
import net.imagej.ops.Ops.Geometric2D.ConvexHull;
import net.imagej.ops.Ops.Geometric2D.Convexity;
import net.imagej.ops.Ops.Geometric2D.Eccentricity;
import net.imagej.ops.Ops.Geometric2D.Elongation;
import net.imagej.ops.Ops.Geometric2D.Feret;
import net.imagej.ops.Ops.Geometric2D.FeretsAngle;
import net.imagej.ops.Ops.Geometric2D.FeretsDiameter;
import net.imagej.ops.Ops.Geometric2D.MajorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorMajorAxis;
import net.imagej.ops.Ops.Geometric2D.Perimeter;
import net.imagej.ops.Ops.Geometric2D.Rectangularity;
import net.imagej.ops.Ops.Geometric2D.Roundness;
import net.imagej.ops.Ops.Geometric2D.Rugosity;
import net.imagej.ops.Ops.Geometric2D.SmallestEnclosingRectangle;
import net.imagej.ops.Ops.Geometric2D.Solidity;
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

		refs.add(ref(Area.class));
		refs.add(ref(BoundingBox.class));
		refs.add(ref(Centroid.class));
		refs.add(ref(Circularity.class));
		refs.add(ref(Contour.class, true, false));
		refs.add(ref(ConvexHull.class));
		refs.add(ref(Convexity.class));
		refs.add(ref(Eccentricity.class));
		refs.add(ref(Elongation.class));
		refs.add(ref(Feret.class));
		refs.add(ref(FeretsAngle.class));
		refs.add(ref(FeretsDiameter.class));
		refs.add(ref(MajorAxis.class));
		refs.add(ref(MinorAxis.class));
		refs.add(ref(MinorMajorAxis.class));
		refs.add(ref(Perimeter.class));
		refs.add(ref(Rectangularity.class));
		refs.add(ref(Roundness.class));
		refs.add(ref(Rugosity.class));
		refs.add(ref(SmallestEnclosingRectangle.class));
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
