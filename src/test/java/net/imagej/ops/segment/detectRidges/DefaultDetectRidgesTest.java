/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops.segment.detectRidges;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.roi.geom.real.DefaultWritablePolyline;
import net.imglib2.roi.util.RealLocalizableRealPositionable;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.app.StatusService;
import org.scijava.cache.CacheService;
import org.scijava.ui.UIService;

/**
 * Tests for {@link DefaultDetectRidges}
 * 
 * @author Gabe Selzer
 *
 */
public class DefaultDetectRidgesTest extends AbstractOpTest {

	@Override
	protected Context createContext() {
		return new Context(OpService.class, OpMatchingService.class,
			CacheService.class, StatusService.class, UIService.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooFewDimensions() {
		Img<FloatType> input = generateFloatArrayTestImg(false, 30);
		
		// run the image through ridge detection
		int width = 1, ridgeLengthMin = 4;
		double lowerThreshold = 2, higherThreshold = 4;

		List<DefaultWritablePolyline> polylines = (List<DefaultWritablePolyline>) ops.run(
			Ops.Segment.DetectRidges.class, input, width, lowerThreshold,
			higherThreshold, ridgeLengthMin);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooManyDimensions() {
		Img<FloatType> input = generateFloatArrayTestImg(false, 30, 30, 30, 30);
		
		// run the image through ridge detection
		int width = 1, ridgeLengthMin = 4;
		double lowerThreshold = 2, higherThreshold = 4;

		List<DefaultWritablePolyline> polylines = (List<DefaultWritablePolyline>) ops.run(
			Ops.Segment.DetectRidges.class, input, width, lowerThreshold,
			higherThreshold, ridgeLengthMin);
	}

	@Test
	public void RegressionTest() {
		Img<FloatType> input = generateFloatArrayTestImg(false, 30, 30);
		RandomAccess<FloatType> linePainter = input.randomAccess();

		// paint lines on the input image
		// vertical line, then horizontal line
		for (int i = 0; i < 2; i++) {
			linePainter.setPosition(15, i);
			for (int j = 2; j < 12; j++) {
				linePainter.setPosition(j, 1 - i);
				linePainter.get().set(256);
			}
		}
		// diagonal line
		for (int j = 0; j < 10; j++) {
			linePainter.setPosition(j, 0);
			linePainter.setPosition(j, 1);
			linePainter.get().set(256);
		}
		// circle
		int radius = 4, h = 22, k = 22;
		for (double a = 0; a < 2 * Math.PI; a += (Math.PI / (4 * radius))) {
			linePainter.setPosition(h + (int) Math.round(radius * Math.cos(a)), 0);
			linePainter.setPosition(k + (int) Math.round(radius * Math.sin(a)), 1);
			linePainter.get().set(256);
		}

		// run the image through ridge detection
		int width = 1, ridgeLengthMin = 4;
		double lowerThreshold = 2, higherThreshold = 4;

		List<DefaultWritablePolyline> polylines = (List<DefaultWritablePolyline>) ops.run(
			Ops.Segment.DetectRidges.class, input, width, lowerThreshold,
			higherThreshold, ridgeLengthMin);

		int vertexCount = 0;
		for (DefaultWritablePolyline pline : polylines) {
			for (int i = 0; i < pline.numVertices(); i++) {
				RealLocalizableRealPositionable p = pline.vertex(i);
				assertEquals(p.getDoublePosition(0), plineVertices[vertexCount++],
					1e-5);
				assertEquals(p.getDoublePosition(1), plineVertices[vertexCount++],
					1e-5);
			}
		}

	}

	double[] plineVertices = { 15.0, 12.0, 15.0, 11.0, 15.0, 10.0, 15.0, 9.0,
		15.0, 8.0, 15.0, 7.0, 15.0, 6.0, 15.0, 5.0, 15.0, 4.0, 15.0, 3.0, 15.0, 2.0,
		15.0, 1.0, 1.0, 15.0, 2.0, 15.0, 3.0, 15.0, 4.0, 15.0, 5.0, 15.0, 6.0, 15.0,
		7.0, 15.0, 8.0, 15.0, 9.0, 15.0, 10.0, 15.0, 11.0, 15.0, 12.0, 15.0,
		22.99751187109411, 18.020211123079733, 23.91666828802532,
		18.083331199962224, 24.916668629361407, 19.083331370638593,
		25.916668800037776, 20.08333171197468, 25.979788876920267,
		21.00248812890589, 25.999999914664198, 22.0, 25.979788876920267,
		22.99751187109411, 25.916668800037776, 23.91666828802532,
		24.916668629361407, 24.916668629361407, 23.91666828802532,
		25.916668800037776, 22.99751187109411, 25.979788876920267, 22.0,
		25.999999914664198, 21.00248812890589, 25.979788876920267,
		20.08333171197468, 25.916668800037776, 19.083331370638593,
		24.916668629361407, 18.083331199962224, 23.91666828802532,
		18.020211123079733, 22.99751187109411, 18.000000085335802, 22.0,
		18.020211123079733, 21.00248812890589, 18.083331199962224,
		20.08333171197468, 19.083331370638593, 19.083331370638593,
		20.08333171197468, 18.083331199962224, 21.00248812890589,
		18.020211123079733, 22.0, 18.000000085335802, 9.999999999999869,
		9.000000085339167, 9.0, 9.0, 8.0, 8.0, 7.0, 7.0, 6.0, 6.0, 5.0, 5.0, 4.0,
		4.0, 3.0, 3.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.8333387946876465 };

}
