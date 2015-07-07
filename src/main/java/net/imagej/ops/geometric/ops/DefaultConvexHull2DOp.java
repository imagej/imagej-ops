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
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
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

package net.imagej.ops.geometric.ops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.geometric.GeometricOps.ConvexHullOp;
import net.imagej.ops.geometric.twod.ConvexPolygon;
import net.imagej.ops.geometric.twod.Polygon;
import net.imglib2.RealLocalizable;

/**
 * Fast Convex Hull implementationn. See
 * https://code.google.com/p/convex-hull/source/browse/Convex+Hull/src/
 * algorithms/FastConvexHull.java?r=4 for reference.ta
 *
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = ConvexHullOp.NAME)
public class DefaultConvexHull2DOp implements ConvexHullOp<ConvexPolygon> {

	@Parameter(type = ItemIO.OUTPUT)
	private ConvexPolygon output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	@Parameter(type = ItemIO.INPUT)
	private OpService ops;

	@Override
	public void run() {

		final List<RealLocalizable> xSorted = new ArrayList<RealLocalizable>(
			this.input.vertices());
		Collections.sort(xSorted, new XCompare());

		final int n = xSorted.size();

		final RealLocalizable[] lUpper = new RealLocalizable[n];

		lUpper[0] = xSorted.get(0);
		lUpper[1] = xSorted.get(1);

		int lUpperSize = 2;

		for (int i = 2; i < n; i++) {
			lUpper[lUpperSize] = xSorted.get(i);
			lUpperSize++;

			while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3],
				lUpper[lUpperSize - 2], lUpper[lUpperSize - 1]))
			{
				// Remove the middle point of the three last
				lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
				lUpperSize--;
			}
		}

		final RealLocalizable[] lLower = new RealLocalizable[n];

		lLower[0] = xSorted.get(n - 1);
		lLower[1] = xSorted.get(n - 2);

		int lLowerSize = 2;

		for (int i = n - 3; i >= 0; i--) {
			lLower[lLowerSize] = xSorted.get(i);
			lLowerSize++;

			while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3],
				lLower[lLowerSize - 2], lLower[lLowerSize - 1]))
			{
				// Remove the middle point of the three last
				lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
				lLowerSize--;
			}
		}

		final List<RealLocalizable> result = new ArrayList<RealLocalizable>();

		for (int i = 0; i < lUpperSize; i++) {
			result.add(lUpper[i]);
		}

		for (int i = 1; i < lLowerSize - 1; i++) {
			result.add(lLower[i]);
		}

		this.output = new ConvexPolygon(result);
	}

	private boolean rightTurn(final RealLocalizable a, final RealLocalizable b,
		final RealLocalizable c)
	{
		return (b.getDoublePosition(0) - a.getDoublePosition(0)) * (c
			.getDoublePosition(1) - a.getDoublePosition(1)) - (b.getDoublePosition(
				1) - a.getDoublePosition(1)) * (c.getDoublePosition(0) - a
					.getDoublePosition(0)) > 0;
	}

	private static class XCompare implements Comparator<RealLocalizable> {

		@Override
		public int compare(final RealLocalizable o1, final RealLocalizable o2) {
			return Double.compare(o1.getDoublePosition(0), o2.getDoublePosition(0));
		}
	}

	@Override
	public ConvexPolygon getOutput() {
		return this.output;
	}

	@Override
	public void setOutput(final ConvexPolygon output) {
		this.output = output;
	}

}
