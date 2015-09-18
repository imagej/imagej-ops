package net.imagej.ops.descriptor3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D.Volume;
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
@Plugin(type = Op.class, name = "multivariate3d")
public class SecondMultiVariate3D<B extends BooleanType<B>> extends
		AbstractFunctionOp<IterableRegion<B>, CovarianceOf2ndMultiVariate3D> implements Contingent {

	@Parameter(type = ItemIO.INPUT)
	private FunctionOp<IterableRegion, DoubleType> volume;
	
	@Parameter(type = ItemIO.INPUT)
	private FunctionOp<IterableRegion, Vector3D> centroid;

	@Override
	public void initialize() {
		volume = ops().function(Volume.class, DoubleType.class, IterableRegion.class);
		centroid = ops().function(Centroid.class, Vector3D.class, IterableRegion.class);
	}
	
	@Override
	public CovarianceOf2ndMultiVariate3D compute(IterableRegion<B> input) {
		CovarianceOf2ndMultiVariate3D output = new CovarianceOf2ndMultiVariate3D();
		Cursor<Void> c = input.localizingCursor();
		int[] pos = new int[3];
		double mX = centroid.out().getX();
		double mY = centroid.out().getY();
		double mZ = centroid.out().getZ();
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
		
		output.setS200(output.getS200() / volume.out().get());
		output.setS020(output.getS020() / volume.out().get());
		output.setS002(output.getS002() / volume.out().get());
		output.setS110(output.getS110() / volume.out().get());
		output.setS101(output.getS101() / volume.out().get());
		output.setS011(output.getS011() / volume.out().get());
		
		return output;
	}

	@Override
	public boolean conforms() {
		return true;
	}

}
