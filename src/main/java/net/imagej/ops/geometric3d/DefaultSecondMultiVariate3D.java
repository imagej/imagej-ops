/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Descriptor3D;
import net.imagej.ops.Ops.Descriptor3D.SecondMultiVariate3D;
import net.imglib2.Cursor;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

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
public class DefaultSecondMultiVariate3D<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, CovarianceOf2ndMultiVariate3D>
		implements
			SecondMultiVariate3D,
			Contingent {

	private FunctionOp<IterableRegion<B>, DoubleType> volume;

	private FunctionOp<IterableRegion<B>, double[]> centroid;

	@Override
	public void initialize() {
		volume = ops().function(DefaultVolumeFeature.class, DoubleType.class, in());
		centroid = ops().function(DefaultCentroid.class, double[].class, in());
	}
	
	@Override
	public CovarianceOf2ndMultiVariate3D compute(final IterableRegion<B> input) {
		CovarianceOf2ndMultiVariate3D output = new CovarianceOf2ndMultiVariate3D();
		Cursor<Void> c = input.localizingCursor();
		int[] pos = new int[3];
		double[] computedCentroid = centroid.compute(input);
		double mX = computedCentroid[0];
		double mY = computedCentroid[1];
		double mZ = computedCentroid[2];
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
		return in().numDimensions() == 3;
	}

}
