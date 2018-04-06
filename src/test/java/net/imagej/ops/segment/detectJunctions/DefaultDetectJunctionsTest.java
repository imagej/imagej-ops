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

package net.imagej.ops.segment.detectJunctions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.DefaultWritablePolyline;

import org.junit.Test;

/**
 * Tests {@link DefaultDetectJunctions}
 * 
 * @author Gabe Selzer
 */
public class DefaultDetectJunctionsTest extends AbstractOpTest {

	@Test
	public void TestRegression() {
		List<DefaultWritablePolyline> lines = new ArrayList<>();

		double threshold = Math.sqrt(2);

		// create first polyline (horizontal)
		List<RealPoint> list1 = new ArrayList<>();
		RealPoint p = new RealPoint(-5, 0);
		for (int i = 0; i < 10; i++) {
			p.move(1, 0);
			list1.add(new RealPoint(p));
		}
		lines.add(new DefaultWritablePolyline(list1));

		// create second polyline (vertical)
		List<RealPoint> list2 = new ArrayList<>();
		p.setPosition(0, 0);
		p.setPosition(-5, 1);
		for (int i = 0; i < 10; i++) {
			p.move(1, 1);
			list2.add(new RealPoint(p));
		}
		lines.add(new DefaultWritablePolyline(list2));

		// create third polyline (diagonal)
		List<RealPoint> list3 = new ArrayList<>();
		p.setPosition(-15, 0);
		p.setPosition(-15, 1);
		for (int i = 0; i < 20; i++) {
			p.move(1, 0);
			p.move(1, 1);
			list3.add(new RealPoint(p));
		}
		lines.add(new DefaultWritablePolyline(list3));

		// create fourth polyline (vertical, different intersection point)
		List<RealPoint> list4 = new ArrayList<>();
		p.setPosition(-11, 0);
		p.setPosition(-18, 1);
		for (int i = 0; i < 7; i++) {
			p.move(1, 1);
			list4.add(new RealPoint(p));
		}
		lines.add(new DefaultWritablePolyline(list4));

		List<RealPoint> results;
		results = (List<RealPoint>) ops.run(
			net.imagej.ops.segment.detectJunctions.DefaultDetectJunctions.class, lines,
			threshold);

		List<RealPoint> expected = new ArrayList<>();
		expected.add(new RealPoint(0, 0));
		expected.add(new RealPoint(-11, -11));

		for (int i = 0; i < results.size(); i++) {
			assertEquals(results.get(i).getDoublePosition(0), expected.get(i)
				.getDoublePosition(0), 0);
			assertEquals(results.get(i).getDoublePosition(1), expected.get(i)
				.getDoublePosition(1), 0);
		}

	}

}
