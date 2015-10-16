package net.imagej.ops.features.sets;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.RealType;

/**
 * {@link FeatureSet} to calculate Geometric2DFeatureSet
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Geometric Features 2D", description = "Calculates Geometric Features on 2D LabelRegions")
public class Geometric2DFeatureSet<L, O extends RealType<O>> extends AbstractOpRefFeatureSet<LabelRegion<L>, O>
		implements DimensionBoundFeatureSet<LabelRegion<L>, O> {

	@Parameter(required = false, label = "Size")
	private boolean isSizeActive = true;

	@Parameter(required = false, label = "Circularity")
	private boolean isCircularityActive = true;

	@Parameter(required = false, label = "Convexity")
	private boolean isConvexityActive = true;

	@Parameter(required = false, label = "Eccentricity")
	private boolean isEccentricityActive = true;

	@Parameter(required = false, label = "MainElongation")
	private boolean isMainElongationActive = true;

	@Parameter(required = false, label = "FeretsAngle")
	private boolean isFeretsAngleActive = true;

	@Parameter(required = false, label = "FeretsDiameter")
	private boolean isFeretsDiameterActive = true;

	@Parameter(required = false, label = "MajorAxis")
	private boolean isMajorAxisActive = true;

	@Parameter(required = false, label = "MinorAxis")
	private boolean isMinorAxisActive = true;

	@Parameter(required = false, label = "BoundarySize")
	private boolean isBoundarySizeActive = true;

	@Parameter(required = false, label = "Boxivity")
	private boolean isBoxivityActive = true;

	@Parameter(required = false, label = "Roundness")
	private boolean isRoundnessActive = true;

	@Parameter(required = false, label = "Rugosity")
	private boolean isRugosityActive = true;

	@Parameter(required = false, label = "Solidity")
	private boolean isSolidityActive = true;

	public Geometric2DFeatureSet() {
		// NB: Empty cofstruction
	}

	@Override
	protected void initOpRefs() {
		setSizeActive(isSizeActive);
		setCircularityActive(isCircularityActive);
		setConvexityActive(isConvexityActive);
		setEccentricityActive(isEccentricityActive);
		setMainElongationActive(isMainElongationActive);
		setFeretsAngleActive(isFeretsAngleActive);
		setFeretsDiameterActive(isFeretsDiameterActive);
		setMajorAxisActive(isMajorAxisActive);
		setMinorAxisActive(isMinorAxisActive);
		setBoundarySizeActive(isBoundarySizeActive);
		setBoxivityActive(isBoxivityActive);
		setRoundnessActive(isRoundnessActive);
		setRugosityActive(isRugosityActive);
		setSolidityActive(isSolidityActive);
	}

	public boolean isSizeActive() {
		return isSizeActive;
	}

	public void setSizeActive(boolean isActive) {
		if (isSizeActive && !this.isSizeActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Size.class);
		}

		this.isSizeActive = isActive;
	}

	public boolean isCircularityActive() {
		return isCircularityActive;
	}

	public void setCircularityActive(boolean isActive) {
		if (isCircularityActive && !this.isCircularityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Circularity.class);
		}

		this.isCircularityActive = isActive;
	}

	public boolean isConvexityActive() {
		return isConvexityActive;
	}

	public void setConvexityActive(boolean isActive) {
		if (isConvexityActive && !this.isConvexityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Convexity.class);
		}
		this.isConvexityActive = isActive;
	}

	public boolean isEccentricityActive() {
		return isEccentricityActive;
	}

	public void setEccentricityActive(boolean isActive) {
		if (isEccentricityActive && !this.isEccentricityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Eccentricity.class);
		}

		this.isEccentricityActive = isActive;
	}

	public boolean isMainElongationActive() {
		return isMainElongationActive;
	}

	public void setMainElongationActive(boolean isActive) {
		if (isMainElongationActive && !this.isMainElongationActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.MainElongation.class);
		}

		this.isMainElongationActive = isActive;
	}

	public boolean isFeretsAngleActive() {
		return isFeretsAngleActive;
	}

	public void setFeretsAngleActive(boolean isActive) {
		if (isFeretsAngleActive && !this.isFeretsAngleActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.FeretsAngle.class);
		}

		this.isFeretsAngleActive = isActive;
	}

	public boolean isFeretsDiameterActive() {
		return isFeretsDiameterActive;
	}

	public void setFeretsDiameterActive(boolean isActive) {
		if (isFeretsDiameterActive && !this.isFeretsDiameterActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.FeretsDiameter.class);
		}

		this.isFeretsDiameterActive = isActive;
	}

	public boolean isMajorAxisActive() {
		return isMajorAxisActive;
	}

	public void setMajorAxisActive(boolean isActive) {
		if (isMajorAxisActive && !this.isMajorAxisActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.MajorAxis.class);
		}

		this.isMajorAxisActive = isActive;
	}

	public boolean isMinorAxisActive() {
		return isMinorAxisActive;
	}

	public void setMinorAxisActive(boolean isActive) {
		if (isMinorAxisActive && !this.isMinorAxisActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.MinorAxis.class);
		}

		this.isMinorAxisActive = isActive;
	}

	public boolean isBoundarySizeActive() {
		return isBoundarySizeActive;
	}

	public void setBoundarySizeActive(boolean isActive) {
		if (isBoundarySizeActive && !this.isBoundarySizeActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.BoundarySize.class);
		}

		this.isBoundarySizeActive = isActive;
	}

	public boolean isBoxivityActive() {
		return isBoxivityActive;
	}

	public void setBoxivityActive(boolean isActive) {
		if (isBoxivityActive && !this.isBoxivityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Boxivity.class);
		}

		this.isBoxivityActive = isActive;
	}

	public boolean isRoundnessActive() {
		return isRoundnessActive;
	}

	public void setRoundnessActive(boolean isActive) {
		if (isRoundnessActive && !this.isRoundnessActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Roundness.class);
		}

		this.isRoundnessActive = isActive;
	}

	public boolean isRugosityActive() {
		return isRugosityActive;
	}

	public void setRugosityActive(boolean isActive) {
		if (isRugosityActive && !this.isRugosityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Rugosity.class);
		}

		this.isRugosityActive = isActive;
	}

	public boolean isSolidityActive() {
		return isSolidityActive;
	}

	public void setSolidityActive(boolean isActive) {
		if (isSolidityActive && !this.isSolidityActive) {
			activateFeature(net.imagej.ops.Ops.Geometric.Solidity.class);
		}

		this.isSolidityActive = isActive;
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