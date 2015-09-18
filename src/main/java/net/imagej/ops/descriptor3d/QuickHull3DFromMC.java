package net.imagej.ops.descriptor3d;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D.ConvexHull3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;

@Plugin(type = Op.class, name = ConvexHull3D.NAME)
public class QuickHull3DFromMC<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DefaultFacets> {

	private FunctionOp<IterableRegion, DefaultFacets> mc;
	
	@Override
	public void initialize() {
		mc = ops().function(MarchingCubes.class, DefaultFacets.class, IterableRegion.class);
	}

	@Override
	public DefaultFacets compute(IterableRegion<B> input) {
		return (DefaultFacets) ops().run(QuickHull3D.class, mc.compute(input).getPoints());
	}

}