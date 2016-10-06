package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.roi.geometric.PointCollection;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.FittingEllipse.class)
public class DefaultFittingEllipsePointCollection extends AbstractDefaultFittingEllipse<PointCollection> {
	
	@Override
	public EllipseRegionOfInterest compute1(PointCollection input) {
		return getEllipse(input.getVertices());
	}

	@Override
	public boolean conforms() {
		return in() != null && in().numDimensions() == 2;
	}
}
