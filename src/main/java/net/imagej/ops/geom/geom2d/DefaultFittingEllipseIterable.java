package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imagej.ops.geom.IIVoidToIRealLocalizableWrapper;
import net.imglib2.IterableInterval;
import net.imglib2.roi.EllipseRegionOfInterest;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.FittingEllipse.class)
public class DefaultFittingEllipseIterable extends AbstractDefaultFittingEllipse<IterableInterval<Void>> {

	@Override
	public EllipseRegionOfInterest compute1(final IterableInterval<Void> input) {
		return getEllipse(new IIVoidToIRealLocalizableWrapper(input));
	}
	
	@Override
	public boolean conforms() {
		return in() != null && in().numDimensions() == 2;
	}
}
