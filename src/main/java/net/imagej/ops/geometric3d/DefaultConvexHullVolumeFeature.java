package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.DefaultFacets;
import net.imagej.ops.descriptor3d.QuickHull3DFromMC;
import net.imagej.ops.descriptor3d.TriangularFacet;
import net.imagej.ops.descriptor3d.Vertex;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Geometric3D.ConvexHullVolume}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.ConvexHullVolume.NAME, label = "Geometric3D: ConvexHullVolume", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultConvexHullVolumeFeature<B extends BooleanType<B>> extends
		AbstractFunctionOp<IterableRegion<B>, DoubleType> implements
		Geometric3DOp<IterableRegion<B>, DoubleType>, Geometric3D.Volume {

	private FunctionOp<IterableRegion, DefaultFacets> convexHull;

	@Override
	public void initialize() {
		convexHull = ops().function(QuickHull3DFromMC.class,
				DefaultFacets.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		DefaultFacets compute = convexHull.compute(input);
		Vertex centroid = compute.getCentroid();
		double volume = 0;
		for (TriangularFacet f : compute.getFacets()) {
			volume += 1 / 3d * f.getArea()
					* Math.abs(f.distanceToPlane(centroid));
		}
		return new DoubleType(volume);
	}

}
