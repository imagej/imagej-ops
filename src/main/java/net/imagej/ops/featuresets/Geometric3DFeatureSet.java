package net.imagej.ops.featuresets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpRef;
import net.imagej.ops.Ops.Geometric.BoundaryPixelCount;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull;
import net.imagej.ops.Ops.Geometric.Boxivity;
import net.imagej.ops.Ops.Geometric.Compactness;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MarchingCubes;
import net.imagej.ops.Ops.Geometric.MedianElongation;
import net.imagej.ops.Ops.Geometric.Rugosity;
import net.imagej.ops.Ops.Geometric.SizeConvexHull;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imagej.ops.Ops.Geometric.Spareness;
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
@SuppressWarnings("rawtypes")
@Plugin(type = FeatureSet.class, label = "3D Geometric Features", description = "Calculates the 3D Geometric Features")
public class Geometric3DFeatureSet<O> extends AbstractOpRefFeatureSet<LabelRegion, O>
		implements GeometricFeatureSet<O> {

	private FunctionOp<LabelRegion, Mesh> contourFunc;

	@Override
	protected Collection<? extends OpRef<?>> initOpRefs() {
		final Set<OpRef<?>> refs = new HashSet<OpRef<?>>();

		refs.add(ref(MainElongation.class));
		refs.add(ref(MedianElongation.class));
		refs.add(ref(Spareness.class));

		refs.add(ref(Boxivity.class));
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

		contourFunc = ops().function(MarchingCubes.class, Mesh.class, in());

		return refs;
	}

	@Override
	protected O evalFunction(final FunctionOp<Object, ? extends O> func, final LabelRegion input) {

		// FIXME: this hack is not required any more as soon as issue
		// https://github.com/imagej/imagej-ops/issues/231 is resolved.
		if (func instanceof MainElongation || func instanceof MedianElongation || func instanceof Spareness) {
			return super.evalFunction(func, input);
		} else {
			return func.compute(contourFunc.compute(input));
		}
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
