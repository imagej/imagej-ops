package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric.BoundaryPixelCount;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull;
import net.imagej.ops.Ops.Geometric.Compactness;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.Rugosity;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.Ops.Geometric.SizeConvexHull;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imagej.ops.Ops.Geometric.Sphericity;
import net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh;
import net.imagej.ops.geom.geom3d.mesh.DefaultVolume;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imglib2.roi.labeling.LabelRegion;

/**
 * {@link FeatureSet} to calculate {@link AbstractOpRefFeatureSet<Mesh, O>}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "3D Geometric Features", description = "Calculates the 3D Geometric Features")
public class Geometric3DFeatureSet<O> extends AbstractOpRefFeatureSet<Mesh, O>
		implements DimensionBoundFeatureSet<Mesh, O> {

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(Compactness.class));
		refs.add(ref(BoundarySizeConvexHull.class));
		refs.add(ref(BoundaryPixelCountConvexHullMesh.class));
		refs.add(ref(SizeConvexHull.class));
		refs.add(ref(Convexity.class));
		refs.add(ref(Rugosity.class));
		refs.add(ref(Solidity.class));
		refs.add(ref(Sphericity.class));
		refs.add(ref(BoundarySize.class));
		refs.add(ref(BoundaryPixelCount.class));
		refs.add(ref(DefaultVolume.class));

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
		return in().triangularFacets();
	}

}
