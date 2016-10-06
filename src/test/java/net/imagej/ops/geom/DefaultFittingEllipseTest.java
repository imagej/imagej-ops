package net.imagej.ops.geom;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.EllipseRegionOfInterest;
import net.imglib2.roi.geometric.Polygon;

import org.junit.Before;
import org.junit.Test;

public class DefaultFittingEllipseTest extends AbstractFeatureTest {

	private Polygon p;
	
	@Before
	public void loadPolygon() {
		p = getPolygon();
	}
	
	@Test
	public void defaultFittingEllipse() {
		EllipseRegionOfInterest result = ops.geom().fittingEllipse(p);
		assertEquals("Center X", 39.105022524406240, result.getOrigin(0), 10e-14);
		assertEquals("Center Y", 59.222976128044480, result.getOrigin(1), 10e-14);
		assertEquals("Major Axis", 45.634747808580330, result.getRadius(0), 10e-14);
		assertEquals("Minor Axis", 25.454190959974017, result.getRadius(1), 10e-14);
	}
}
