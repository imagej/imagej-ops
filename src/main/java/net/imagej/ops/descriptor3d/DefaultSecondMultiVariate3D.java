package net.imagej.ops.descriptor3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.SecondMultiVariate3D;
import net.imagej.ops.Ops.Geometric3D.Volume;
import net.imagej.ops.geometric3d.DefaultVolumeFeature;
import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This {@link Op} computes the 2nd multi variate of a 
 * {@link IterableRegion} (Label).
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <B> BooleanType
 */
@Plugin(type = Op.class, name = Descriptor3D.SecondMultiVariate3D.NAME)
public class DefaultSecondMultiVariate3D<B extends BooleanType<B>> extends
		AbstractFunctionOp<IterableRegion<B>, CovarianceOf2ndMultiVariate3D> implements Contingent, SecondMultiVariate3D {

	private FunctionOp<IterableRegion, DoubleType> volume;
	
	private FunctionOp<IterableRegion, Vector3D> centroid;

	@Override
	public void initialize() {
		volume = ops().function(DefaultVolumeFeature.class, DoubleType.class, IterableRegion.class);
		centroid = ops().function(DefaultCentroid3D.class, Vector3D.class, IterableRegion.class);
	}
	
	@Override
	public CovarianceOf2ndMultiVariate3D compute(IterableRegion<B> input) {
		CovarianceOf2ndMultiVariate3D output = new CovarianceOf2ndMultiVariate3D();
		Cursor<B> c = input.localizingCursor();
		int[] pos = new int[3];
		Vector3D computedCentroid = centroid.compute(input);
		double mX = computedCentroid.getX();
		double mY = computedCentroid.getY();
		double mZ = computedCentroid.getZ();
		while (c.hasNext()) {
			c.next();
			c.localize(pos);
			output.setS200(output.getS200() + (pos[0] - mX) * (pos[0] - mX));
			output.setS020(output.getS020() + (pos[1] - mX) * (pos[1] - mY));
			output.setS002(output.getS002() + (pos[2] - mX) * (pos[2] - mZ));
			output.setS110(output.getS110() + (pos[0] - mY) * (pos[1] - mY));
			output.setS101(output.getS101() + (pos[0] - mY) * (pos[2] - mZ));
			output.setS011(output.getS011() + (pos[1] - mZ) * (pos[2] - mZ));
		}
		
		DoubleType computedVolume = volume.compute(input);
		output.setS200(output.getS200() / computedVolume.get());
		output.setS020(output.getS020() / computedVolume.get());
		output.setS002(output.getS002() / computedVolume.get());
		output.setS110(output.getS110() / computedVolume.get());
		output.setS101(output.getS101() / computedVolume.get());
		output.setS011(output.getS011() / computedVolume.get());
		
		return output;
	}

	@Override
	public boolean conforms() {
		return true;
	}

}
