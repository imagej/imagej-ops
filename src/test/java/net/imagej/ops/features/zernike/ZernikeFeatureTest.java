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
package net.imagej.ops.features.zernike;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.imagej.ops.Ops.Zernike.Magnitude;
import net.imagej.ops.Ops.Zernike.Phase;
import net.imagej.ops.features.AbstractFeatureTest;

/**
 * 
 * Test {@Link ZernikeFeature}
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class ZernikeFeatureTest extends AbstractFeatureTest {
	
	@Test
	public void testPhaseFeature() {
		
		assertEquals(Phase.NAME, 180.0, ops.zernike().phase(ellipse, 4, 2).getRealDouble(), 1e-3);
		assertEquals(Phase.NAME, 360.0, ops.zernike().phase(rotatedEllipse, 4, 2).getRealDouble(), 1e-3);
	}
	
	@Test 
	public void testMagnitudeFeature() {
			
		double v1 = ops.zernike().magnitude(ellipse, 4, 2).getRealDouble();
		double v2 = ops.zernike().magnitude(rotatedEllipse, 4, 2).getRealDouble();
	
		assertEquals(Magnitude.NAME, 0.16684211008, ops.zernike().magnitude(ellipse, 4, 2).getRealDouble(), 1e-3);
		
		// magnitude should be the same after rotating the image
		assertEquals(Magnitude.NAME, v1, v2, 1e-3);
	}

}
