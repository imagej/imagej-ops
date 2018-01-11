/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

package net.imagej.ops.segment.ridgeDetection;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.DefaultPolyline;

import org.junit.Test;

/**
 * Tests the Junction Detection Algorithm
 * 
 * @author Gabe Selzer
 */
public class JunctionDetectionTest extends AbstractOpTest {
	
	@Test
	public void TestRegression() {
		List<DefaultPolyline> lines = new ArrayList<>();
		
		double threshold = 2;

		List<RealPoint> list1 = new ArrayList<>();
		RealPoint p11 = new RealPoint(1, 1);
		list1.add(p11);
		RealPoint p12 = new RealPoint(2, 2);
		list1.add(p12);
		RealPoint p13 = new RealPoint(3, 3);
		list1.add(p13);
		RealPoint p14 = new RealPoint(4, 4);
		list1.add(p14);
		RealPoint p15 = new RealPoint(5, 5);
		list1.add(p15);
		RealPoint p16 = new RealPoint(6, 6);
		list1.add(p16);
		RealPoint p17 = new RealPoint(7, 7);
		list1.add(p17);
		RealPoint p18 = new RealPoint(8, 8);
		list1.add(p18);
		RealPoint p19 = new RealPoint(9, 9);
		list1.add(p19);

		DefaultPolyline line1 = new DefaultPolyline(list1);
		lines.add(line1);

		List<RealPoint> list2 = new ArrayList<>();
		RealPoint p21 = new RealPoint(6, 1);
		list2.add(p21);
		RealPoint p22 = new RealPoint(5, 2);
		list2.add(p22);
		RealPoint p23 = new RealPoint(4, 3);
		list2.add(p23);
		RealPoint p24 = new RealPoint(3, 4);
		list2.add(p24);
		RealPoint p25 = new RealPoint(2, 5);
		list2.add(p25);

		DefaultPolyline line2 = new DefaultPolyline(list2);
		lines.add(line2);
		
		List<RealPoint> list3 = new ArrayList<>();
		RealPoint p31 = new RealPoint(9, 4);
		list3.add(p31);
		RealPoint p32 = new RealPoint(8, 5);
		list3.add(p32);
		RealPoint p33 = new RealPoint(7, 6);
		list3.add(p33);
		RealPoint p34 = new RealPoint(6, 7);
		list3.add(p34);
		RealPoint p35 = new RealPoint(5, 8);
		list3.add(p35);

		DefaultPolyline line3= new DefaultPolyline(list3);
		lines.add(line3);
		
		List<RealPoint> list4 = new ArrayList<>();
		RealPoint p41 = new RealPoint(20, 4);
		list4.add(p41);
		RealPoint p42 = new RealPoint(20, 5);
		list4.add(p42);
		RealPoint p43 = new RealPoint(20, 6);
		list4.add(p43);
		RealPoint p44 = new RealPoint(20, 7);
		list4.add(p44);
		RealPoint p45 = new RealPoint(20, 8);
		list4.add(p45);

		DefaultPolyline line4= new DefaultPolyline(list4);
		lines.add(line4);
		
		List<RealPoint> list5 = new ArrayList<>();
		RealPoint p51 = new RealPoint(23, 4);
		list5.add(p51);
		RealPoint p52 = new RealPoint(22, 5);
		list5.add(p52);
		RealPoint p53 = new RealPoint(21, 6);
		list5.add(p53);
		RealPoint p54 = new RealPoint(22, 7);
		list5.add(p54);
		RealPoint p55 = new RealPoint(23, 8);
		list5.add(p55);

		DefaultPolyline line5= new DefaultPolyline(list5);
		lines.add(line5);
		
		List<RealPoint> list6 = new ArrayList<>();
		RealPoint p61 = new RealPoint(17, 4);
		list6.add(p61);
		RealPoint p62 = new RealPoint(18, 5);
		list6.add(p62);
		RealPoint p63 = new RealPoint(19, 6);
		list6.add(p63);
		RealPoint p64 = new RealPoint(18, 7);
		list6.add(p64);
		RealPoint p65 = new RealPoint(17, 8);
		list6.add(p65);

		DefaultPolyline line6= new DefaultPolyline(list6);
		lines.add(line6);
		
		List<RealPoint> results;
		results = (List<RealPoint>) ops.run(
			net.imagej.ops.segment.ridgeDetection.JunctionDetection.class, lines, threshold);
		
//		System.out.println(results.size());

		for (int i = 0; i < results.size(); i++) {
//			RealPoint p = results.get(i);
//			System.out.println("[" + p.getDoublePosition(0) + "," + p.getDoublePosition(1) + "]");
		}

	}

}
