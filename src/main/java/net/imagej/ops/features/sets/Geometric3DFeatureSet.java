package net.imagej.ops.features.sets;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

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
import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} to calculate 3D Geometric Features
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Geometric Features 3D", description = "Calculates Geometric Features on 3D LabelRegions")
public class Geometric3DFeatureSet<L, O extends RealType<O>> extends AbstractOpRefFeatureSet<LabelRegion<L>, O>
		implements DimensionBoundFeatureSet<LabelRegion<L>, O> {

	@Parameter(required = false, label = "Compactness")
	private boolean isCompactnessActive = true;

	@Parameter(required = false, label = "BoundarySizeConvexHull")
	private boolean isBoundarySizeConvexHullActive = true;

	@Parameter(required = false, label = "BoundaryPixelCountConvexHullMesh")
	private boolean isBoundaryPixelCountConvexHullMeshActive = true;

	@Parameter(required = false, label = "SizeConvexHull")
	private boolean isSizeConvexHullActive = true;

	@Parameter(required = false, label = "Convexity")
	private boolean isConvexityActive = true;

	@Parameter(required = false, label = "Rugosity")
	private boolean isRugosityActive = true;

	@Parameter(required = false, label = "Solidity")
	private boolean isSolidityActive = true;

	@Parameter(required = false, label = "Sphericity")
	private boolean isSphericityActive = true;

	@Parameter(required = false, label = "BoundarySize")
	private boolean isBoundarySizeActive = true;

	@Parameter(required = false, label = "BoundaryPixelCount")
	private boolean isBoundaryPixelCountActive = true;

	@Parameter(required = false, label = "Size")
	private boolean isSizeActive = true;

	public Geometric3DFeatureSet() {
		// NB: Empty cofstruction
	}

	@Override
	protected void initOpRefs() {
		setCompactnessActive(isCompactnessActive);
		setBoundarySizeConvexHullActive(isBoundarySizeConvexHullActive);
		setBoundaryPixelCountConvexHullMeshActive(isBoundaryPixelCountConvexHullMeshActive);
		setSizeConvexHullActive(isSizeConvexHullActive);
		setConvexityActive(isConvexityActive);
		setRugosityActive(isRugosityActive);
		setSolidityActive(isSolidityActive);
		setSphericityActive(isSphericityActive);
		setBoundarySizeActive(isBoundarySizeActive);
		setBoundaryPixelCountActive(isBoundaryPixelCountActive);
		setSizeActive(isSizeActive);
	}

	public boolean isCompactnessActive() {
		return isCompactnessActive;
	}

	public void setCompactnessActive(boolean isActive) {
		if (isCompactnessActive && !this.isCompactnessActive) {
			activateFeature(Compactness.class);
		}

		this.isCompactnessActive = isActive;
	}

	public boolean isBoundarySizeConvexHullActive() {
		return isBoundarySizeConvexHullActive;
	}

	public void setBoundarySizeConvexHullActive(boolean isActive) {
		if (isBoundarySizeConvexHullActive && !this.isBoundarySizeConvexHullActive) {
			activateFeature(BoundarySizeConvexHull.class);
		}

		this.isBoundarySizeConvexHullActive = isActive;
	}

	public boolean isBoundaryPixelCountConvexHullMeshActive() {
		return isBoundaryPixelCountConvexHullMeshActive;
	}

	public void setBoundaryPixelCountConvexHullMeshActive(boolean isActive) {
		if (isBoundaryPixelCountConvexHullMeshActive && !this.isBoundaryPixelCountConvexHullMeshActive) {
			activateFeature(BoundaryPixelCountConvexHullMesh.class);
		}

		this.isBoundaryPixelCountConvexHullMeshActive = isActive;
	}

	public boolean isSizeConvexHullActive() {
		return isSizeConvexHullActive;
	}

	public void setSizeConvexHullActive(boolean isActive) {
		if (isSizeConvexHullActive && !this.isSizeConvexHullActive) {
			activateFeature(SizeConvexHull.class);
		}

		this.isSizeConvexHullActive = isActive;
	}

	public boolean isConvexityActive() {
		return isConvexityActive;
	}

	public void setConvexityActive(boolean isActive) {
		if (isConvexityActive && !this.isConvexityActive) {
			activateFeature(Convexity.class);
		}

		this.isConvexityActive = isActive;
	}

	public boolean isRugosityActive() {
		return isRugosityActive;
	}

	public void setRugosityActive(boolean isActive) {
		if (isRugosityActive && !this.isRugosityActive) {
			activateFeature(Rugosity.class);
		}

		this.isRugosityActive = isActive;
	}

	public boolean isSolidityActive() {
		return isSolidityActive;
	}

	public void setSolidityActive(boolean isActive) {
		if (isSolidityActive && !this.isSolidityActive) {
			activateFeature(Solidity.class);
		}

		this.isSolidityActive = isActive;
	}

	public boolean isSphericityActive() {
		return isSphericityActive;
	}

	public void setSphericityActive(boolean isActive) {
		if (isSphericityActive && !this.isSphericityActive) {
			activateFeature(Sphericity.class);
		}

		this.isSphericityActive = isActive;
	}

	public boolean isBoundarySizeActive() {
		return isBoundarySizeActive;
	}

	public void setBoundarySizeActive(boolean isActive) {
		if (isBoundarySizeActive && !this.isBoundarySizeActive) {
			activateFeature(BoundarySize.class);
		}

		this.isBoundarySizeActive = isActive;
	}

	public boolean isBoundaryPixelCountActive() {
		return isBoundaryPixelCountActive;
	}

	public void setBoundaryPixelCountActive(boolean isActive) {
		if (isBoundaryPixelCountActive && !this.isBoundaryPixelCountActive) {
			activateFeature(BoundaryPixelCount.class);
		}

		this.isBoundaryPixelCountActive = isActive;
	}

	public boolean isSizeActive() {
		return isSizeActive;
	}

	public void setSizeActive(boolean isActive) {
		if (isSizeActive && !this.isSizeActive) {
			activateFeature(Size.class);
		}

		this.isSizeActive = isActive;
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