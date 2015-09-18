package net.imagej.ops.geometric3d;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.DefaultFacets;
import net.imagej.ops.descriptor3d.MarchingCubes;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link SurfaceAreaFeature}. Use {@link FeatureSet}
 * to compile this {@link Op}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.SurfaceArea.NAME, label = "Geometric3D: SurfaceArea", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultSurfaceAreaFeature<I extends BooleanType<I>>
		extends
			AbstractFunctionOp<IterableRegion<I>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<I>, DoubleType>,
			Geometric3D.SurfaceArea {

	private FunctionOp<IterableRegion, DefaultFacets> marchingCube;
	
	@Override
	public void initialize() {
		marchingCube = ops().function(MarchingCubes.class, DefaultFacets.class, IterableRegion.class);
	}
	
	@Override
	public DoubleType compute(IterableRegion<I> input) {
		return new DoubleType(marchingCube.compute(input).getArea());
	}

}
