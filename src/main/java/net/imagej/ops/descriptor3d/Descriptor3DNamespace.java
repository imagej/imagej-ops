package net.imagej.ops.descriptor3d;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.plugin.Plugin;

@Plugin(type = Namespace.class)
public class Descriptor3DNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "descriptor3d";
	}

	@OpMethod(op = net.imagej.ops.descriptor3d.DefaultBitTypeVertexInterpolator.class)
	public double[] BitTypeVertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.descriptor3d.DefaultBitTypeVertexInterpolator.class, p1, p2, p1Value, p2Value);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.descriptor3d.DefaultCentroid3D.class)
	public <B extends BooleanType<B>> Vector3D Centroid3D(final IterableRegion<B> in) {
		final Vector3D result =
			(Vector3D) ops().run(net.imagej.ops.descriptor3d.DefaultCentroid3D.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.descriptor3d.DefaultVertexInterpolator.class)
	public double[] VertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value, final double isolevel) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.descriptor3d.DefaultVertexInterpolator.class, p1, p2, p1Value, p2Value, isolevel);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.descriptor3d.MarchingCubes.class)
	public <B extends BooleanType<B>> DefaultFacets Polygonize(final RandomAccessibleInterval<B> in) {
		final DefaultFacets result =
			(DefaultFacets) ops().run(net.imagej.ops.descriptor3d.MarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.descriptor3d.MarchingCubes.class)
	public <B extends BooleanType<B>> DefaultFacets Polygonize(final RandomAccessibleInterval<B> in, final double isolevel) {
		final DefaultFacets result =
			(DefaultFacets) ops().run(net.imagej.ops.descriptor3d.MarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.descriptor3d.MarchingCubes.class)
	public <B extends BooleanType<B>> DefaultFacets Polygonize(final RandomAccessibleInterval<B> in, final double isolevel, final VertexInterpolator interpolatorClass) {
		final DefaultFacets result =
			(DefaultFacets) ops().run(net.imagej.ops.descriptor3d.MarchingCubes.class, in, isolevel, interpolatorClass);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.descriptor3d.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D SecondMultiVariate3D(final IterableRegion<B> in) {
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(net.imagej.ops.descriptor3d.DefaultSecondMultiVariate3D.class, in);
		return result;
	}
}
