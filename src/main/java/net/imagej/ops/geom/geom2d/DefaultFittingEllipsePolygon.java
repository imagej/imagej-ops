package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.roi.geometric.Polygon;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.FittingEllipse.class)
public class DefaultFittingEllipsePolygon extends AbstractDefaultFittingEllipse<Polygon> {

	@Override
	public EllipseRegionOfInterest compute1(Polygon input) {
		return getEllipse(input.getVertices());
	}

	@Override
	public boolean conforms() {
		return in() != null && in().numDimensions() == 2;
	}
}
