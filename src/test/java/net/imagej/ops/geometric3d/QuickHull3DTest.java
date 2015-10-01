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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

/**
 * This class tests the {@link DefaultConvexHull3D} implementation. 
 * 
 * The number of facets is verified with qhull.org and
 * {@link QuickHull3DTest#isConvex(List, double)} checks for 
 * each centroid if it is behind all other facets. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class QuickHull3DTest extends AbstractOpTest {

	@Test
	public void quickhull_100_000_Test() {
		DefaultMesh df = new DefaultMesh();
		df.setPoints(randomPointSet(100000, 20150818));

		DefaultMesh convexHull = (DefaultMesh) ops.run(DefaultConvexHull3D.class,
				df.getPoints());
		assertTrue(isConvex(convexHull.getFacets(), convexHull.getEpsilon()));
		assertEquals(175, convexHull.getPoints().size());
	}

	@Test
	public void quickhull_4_Test() {
		LinkedHashSet<Vertex> points = new LinkedHashSet<Vertex>();
		points.add(new Vertex(0, 0, 0));
		points.add(new Vertex(1, 0, 0));
		points.add(new Vertex(0, 0, 1));
		points.add(new Vertex(0, 1, 0));
		
		DefaultMesh df = new DefaultMesh();
		df.setPoints(points);

		DefaultMesh convexHull = (DefaultMesh) ops.run(DefaultConvexHull3D.class,
				df.getPoints());
		assertTrue(isConvex(convexHull.getFacets(), convexHull.getEpsilon()));
		assertEquals(4, convexHull.getPoints().size());
	}

	@Test
	public void quickhull_6_Test() {
		LinkedHashSet<Vertex> points = new LinkedHashSet<Vertex>();
		points.add(new Vertex(3.2, 4.8, 4.4));
		points.add(new Vertex(0, -4.9, 1.1));
		points.add(new Vertex(-2.4, 4.9, -3.1));
		points.add(new Vertex(4.5, -0.9, -2.5));
		points.add(new Vertex(-4.7, 0.4, -4.2));
		points.add(new Vertex(-1.9, 2.2, -3.3));

		DefaultMesh df = new DefaultMesh();
		df.setPoints(points);

		DefaultMesh convexHull = (DefaultMesh) ops.run(DefaultConvexHull3D.class,
				df.getPoints());
		assertTrue(isConvex(convexHull.getFacets(), convexHull.getEpsilon()));
		assertEquals(5, convexHull.getPoints().size());
	}

	@Test
	public void quickhull_12_Test() {
		LinkedHashSet<Vertex> points = new LinkedHashSet<Vertex>();
		points.add(new Vertex(-0.03621271768232132, 0.3728502838619522,	0.4947140370446388));
		points.add(new Vertex(0.3210853052521919, 0.4807189479290684, 0.4433501688235907));
		points.add(new Vertex(0.07214279572678994, -0.4960366976410492, 0.1112227161519441));
		points.add(new Vertex(0.2229772524190855, -0.4213242506806965, -0.1966818060695024));
		points.add(new Vertex(-0.3411871756810576, -0.3328629143842151, -0.4270033635450559));
		points.add(new Vertex(-0.245701439441835, 0.495905311308713, -0.3194406286994373));

		points.add(new Vertex(0.458374538420117, -0.09914027349943322, -0.2505798421339875));
		points.add(new Vertex(-0.4954086979808367, -0.3339869997780649, -0.3195065691317492));
		points.add(new Vertex(-0.3392973838740004, 0.4288679723896719, -0.01599531622230571));
		points.add(new Vertex(0.2724846394476338, -0.3506708492996831, 0.2750346518820475));
		points.add(new Vertex(0.3544683273457627, -0.450828987127942, -0.0827870439577727));
		points.add(new Vertex(0.1667164640191164, 0.003605551555385444, -0.4014989499947977));

		DefaultMesh df = new DefaultMesh();
		df.setPoints(points);

		DefaultMesh convexHull = (DefaultMesh) ops.run(DefaultConvexHull3D.class,
				df.getPoints());
		assertTrue(isConvex(convexHull.getFacets(), convexHull.getEpsilon()));
		assertEquals(12, convexHull.getPoints().size());
	}

	@Test
	public void quickhull_40_Test() {

		// 20 result points
		LinkedHashSet<Vertex> points = new LinkedHashSet<Vertex>();
		points.add(new Vertex(0.3215426810286406, 0.1678336189760208, -0.2203710966001927));
		points.add(new Vertex(0.2229772524190855, -0.4213242506806965, -0.1966818060695024));
		points.add(new Vertex(0.3688830163971363, -0.1831502133823468, -0.2056387967482571));
		points.add(new Vertex(-0.1712592515826777, -0.3542439228428937, 0.2223876390814666));
		points.add(new Vertex(-0.3309556113844324, -0.370961861099081, 0.2439994981922204));
		points.add(new Vertex(-0.1004397059794885, -0.09014152417903909, -0.008600084584765189));
		points.add(new Vertex(0.458374538420117, -0.09914027349943322, -0.2505798421339875));
		points.add(new Vertex(-0.4954086979808367, -0.3339869997780649, -0.3195065691317492));
		points.add(new Vertex(0.053091190339151, 0.3036317017894533, 0.1380056861210668));
		points.add(new Vertex(0.4615616439483703, 0.4665423151725366, 0.1766835406205464));
		points.add(new Vertex(-0.4797380864431505, 0.0419809916447671, -0.4254776681079321));
		points.add(new Vertex(-0.003168473023146823, -0.2525299883005488, -0.27151530400991));
		points.add(new Vertex(-0.3577162826971303, -0.1375644040643837, -0.04494194644032229));
		points.add(new Vertex(-0.3392973838740004, 0.4288679723896719, -0.01599531622230571));
		points.add(new Vertex(0.1667164640191164, 0.003605551555385444, -0.4014989499947977));
		points.add(new Vertex(0.00714666676441833, 0.1140243407469469, 0.407090128778564));
		points.add(new Vertex(-0.03621271768232132, 0.3728502838619522, 0.4947140370446388));
		points.add(new Vertex(-0.3411871756810576, -0.3328629143842151, -0.4270033635450559));
		points.add(new Vertex(0.3544683273457627, -0.450828987127942, -0.0827870439577727));
		points.add(new Vertex(-0.4018510635028137, 0.08917494033386464, -0.2367824197158054));
		points.add(new Vertex(0.3978697768392692, -0.002667689232777493, 0.1641431727112673));
		points.add(new Vertex(-0.245701439441835, 0.495905311308713, -0.3194406286994373));
		points.add(new Vertex(0.161352035739787, -0.1563404972258401, 0.3852604361113724));
		points.add(new Vertex(0.07214279572678994, -0.4960366976410492, 0.1112227161519441));
		points.add(new Vertex(0.3201855824516951, 0.359077846965825, 0.02136723140381946));
		points.add(new Vertex(0.1190541238701475, -0.05734495917087884, 0.2032677509852384));
		points.add(new Vertex(0.3210853052521919, 0.4807189479290684, 0.4433501688235907));
		points.add(new Vertex(0.3862800354941562, 0.2085496142586224, 0.09336129957191763));
		points.add(new Vertex(0.1233572616459404, 0.265491605052251, 0.117400122450106));
		points.add(new Vertex(0.1438531872293476, -0.2594872752758556, -0.2026374435076839));
		points.add(new Vertex(0.2724846394476338, -0.3506708492996831, 0.2750346518820475));
		points.add(new Vertex(-0.4926118841325975, -0.3279366743079728, 0.3683135596740186));
		points.add(new Vertex(0.2459906458351674, 0.3647787136629026, -0.1641662355178652));
		points.add(new Vertex(-0.141922976953837, -0.2994764654892278, -0.3009570467294725));
		points.add(new Vertex(-0.1850859398814719, 0.2606059478228967, 0.004159106876849283));
		points.add(new Vertex(-0.09789466634196664, -0.3156603563722785, -0.303610991503681));
		points.add(new Vertex(0.2100642609503719, -0.4499717643018549, 0.3245569875692548));
		points.add(new Vertex(-0.1707163766685095, -0.2301452446078371, -0.05112823569320907));
		points.add(new Vertex(-0.312260808713977, -0.1674135249735914, 0.2808831662692904));
		points.add(new Vertex(-0.1966306233747216, 0.2291105671125563, -0.3387042454804333));

		DefaultMesh df = new DefaultMesh();
		df.setPoints(points);

		DefaultMesh convexHull = (DefaultMesh) ops.run(DefaultConvexHull3D.class,
				df.getPoints());
		assertTrue(isConvex(convexHull.getFacets(), convexHull.getEpsilon()));
		assertEquals(20, convexHull.getPoints().size());
	}

	/**
	 * Checks for each centroid of each facet if the centroid is behind all other facets.
	 * @param facets of the convex hull
	 * @param tolerance of the convex hull computation
	 * @return is convex
	 */
	private boolean isConvex(List<TriangularFacet> facets, double tolerance) {
		Vector3D[] centroids = new Vector3D[facets.size()];
		for (int i = 0; i < facets.size(); i++) {
			centroids[i] = facets.get(i).getCentroid();
		}

		boolean isConvex = true;
		for (int i = 0; i < facets.size(); i++) {
			for (int j = 0; j < centroids.length; j++) {
				if (j != i) {
					if (facets.get(i)
							.distanceToPlane(centroids[j]) >= tolerance) {
						isConvex = false;
						break;
					}
				}
			}
		}
		return isConvex;
	}

	/**
	 * Creates a random point cloud.
	 * @param n number of points
	 * @param seed the seed
	 * @return random point cloud
	 */
	private LinkedHashSet<Vertex> randomPointSet(int n, long seed) {
		LinkedHashSet<Vertex> points = new LinkedHashSet<Vertex>();
		Random r = new Random(seed);

		for (int i = 0; i < n; i++) {
			points.add(
					new Vertex(r.nextDouble(), r.nextDouble(), r.nextDouble()));
		}

		return points;
	}
}
