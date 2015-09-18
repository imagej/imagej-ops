package net.imagej.ops.geometric3d;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.DefaultFacets;
import net.imagej.ops.descriptor3d.MarchingCubes;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link Geometric3D.SurfacePixel}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.SurfacePixel.NAME, label = "Geometric3D: SurfacePixel", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultSurfacePixelFeature <T extends BooleanType<T>>
		extends
			AbstractFunctionOp<IterableRegion<T>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<T>, DoubleType>,
			Geometric3D.SurfacePixel {

	private FunctionOp<IterableRegion, DefaultFacets> marchingCube;
	
	@Override
	public void initialize() {
		marchingCube = ops().function(MarchingCubes.class, DefaultFacets.class, IterableRegion.class);
	}
	
	@Override
	public DoubleType compute(IterableRegion<T> input) {
		return new DoubleType(marchingCube.compute(input).getPoints().size());
	}

}