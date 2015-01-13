/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.features.zernike;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.features.FeatureResult;
import net.imagej.ops.features.sets.ZernikeFeatureSet;

import org.junit.Test;

/**
 * Testing implementations of {@link ZernikeFeatures}
 * 
 * @author Andreas Graumann (University of Konstanz)
 */
public class ZernikeFeatureTest extends AbstractFeatureTest {

	/**
	 * Testing
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testZernikeMoments() {
		
		// test on ellipse
		List<FeatureResult> res = (List<FeatureResult>) ops.op(
				ZernikeFeatureSet.class, ellipse, true, true, 1, 3).compute(
				ellipse);

		double[] matlabResultsEllipse = { 0.009, 45.0, 0.5737, 180.0, 0.1155,
				179.905, 0.0158, 200.5937, 0.0065, 224.9683 };

		for(int i = 0; i < res.size(); i++) {
			assertEquals(res.get(i).getName(), matlabResultsEllipse[i], res.get(i).getValue(), BIG_DELTA);
		}
		res.clear();
		
		// test on rotated ellipse
		res = (List<FeatureResult>) ops.op(
				ZernikeFeatureSet.class, rotatedEllipse, true, true, 1, 3).compute(
				rotatedEllipse);

		double[] matlabResultsRotated = { 0.009, 43.9237, 0.5735, 180.0, 0.1155,
				0.0985, 0.016, 247.7494, 0.0065, 45.267 };

		for(int i = 0; i < res.size(); i++) {
			assertEquals(res.get(i).getName(), matlabResultsRotated[i], res.get(i).getValue(), BIG_DELTA);
		}
		
		res.clear();
		
		// test on image with constant filling
		res = (List<FeatureResult>) ops.op(
				ZernikeFeatureSet.class, constant, true, true, 1, 3).compute(
						constant);

		double[] matlabResultsConstant = { 0.0, 180.0, 0.002, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0 };

		for(int i = 0; i < res.size(); i++) {
			assertEquals(res.get(i).getName(), matlabResultsConstant[i], res.get(i).getValue(), BIG_DELTA);
		}
	}

}
