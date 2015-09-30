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
package net.imagej.ops.geom;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.Contour;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BoolType;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Contour}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Contour", name = Geometric.Contour.NAME)
public class DefaultContour
		extends
			AbstractFunctionOp<RandomAccessibleInterval<BoolType>, Polygon>
		implements
			GeometricOp<RandomAccessibleInterval<BoolType>, Polygon>,
			Contingent,
			Geometric.Contour {

	@Parameter(type = ItemIO.INPUT, description = "Set this flag to use the refined Jacobs stopping criteria")
	private boolean useJacobs = true;

	@Parameter(type = ItemIO.INPUT, description = "Set this flag to invert between foreground/background.")
	private boolean isInverted = false;

	/**
	 * ClockwiseMooreNeighborhoodIterator Iterates clockwise through a 2D Moore
	 * Neighborhood (8 connected Neighborhood). This iterator encourages reuse!
	 * Reset the iterator and move the underlying random accessible, do not
	 * create new ones. That is more resource efficient and faster.
	 *
	 * @author Jonathan Hale (University of Konstanz)
	 */
	final class ClockwiseMooreNeighborhoodIterator<T extends Type<T>>
			implements java.util.Iterator<T> {

		final private RandomAccess<T> m_ra;

		final private int[][] CLOCKWISE_OFFSETS = { { 0, -1 }, { 1, 0 },
				{ 1, 0 }, { 0, 1 }, { 0, 1 }, { -1, 0 }, { -1, 0 }, { 0, -1 } };

		final private int[][] CCLOCKWISE_OFFSETS = { { 0, 1 }, { 0, 1 },
				{ -1, 0 }, { -1, 0 }, { 0, -1 }, { 0, -1 }, { 1, 0 }, { 1, 0 } };

		// index of offset to be executed at next next() call.
		private int m_curOffset = 0;

		// startIndex basically tells the Cursor when it performed
		// every relative movement in CLOCKWISE_OFFSETS once. After
		// backtrack, this is reset to go through all 8 offsets again.
		private int m_startIndex = 7;

		public ClockwiseMooreNeighborhoodIterator(final RandomAccess<T> ra) {
			m_ra = ra;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final boolean hasNext() {
			return (m_curOffset != m_startIndex);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final T next() {
			m_ra.move(CLOCKWISE_OFFSETS[m_curOffset]);
			m_curOffset = (m_curOffset + 1) & 7; // <=> (m_curOffset+1) % 8
			return m_ra.get();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		public final void backtrack() {
			final int[] back = CCLOCKWISE_OFFSETS[m_curOffset];
			m_ra.move(back); // undo last move

			// find out, where to continue:
			if (back[0] == 0) {
				if (back[1] == 1) {
					m_curOffset = 6;
				} else {
					m_curOffset = 2;
				}
			} else {
				if (back[0] == 1) {
					m_curOffset = 4;
				} else {
					m_curOffset = 0;
				}
			}

			m_startIndex = (m_curOffset + 7) & 7; // set the Pixel to stop at
		}

		public final int getIndex() {
			return m_curOffset;
		}

		/**
		 * Reset the current offset index. This does not influence the
		 * RandomAccess.
		 */
		public final void reset() {
			m_curOffset = 0;
			m_startIndex = 7;
		}

		/**
		 * backtrack and set only part of the neighborhood to be iterated.
		 */
		public void backtrackSpecial() {
			final int[] back = CCLOCKWISE_OFFSETS[m_curOffset];
			m_ra.move(back); // undo last move

			// find out, where to continue:
			if (back[0] == 0) {
				if (back[1] == 1) {
					m_curOffset = 6;
				} else {
					m_curOffset = 2;
				}
			} else {
				if (back[0] == 1) {
					m_curOffset = 4;
				} else {
					m_curOffset = 0;
				}
			}

			m_startIndex = (m_curOffset + 5) & 7; // set the Pixel to stop at
		}
	}

	@Override
	public Polygon compute(final RandomAccessibleInterval<BoolType> input) {
		List<RealPoint> p = new ArrayList<RealPoint>();

		final RandomAccess<BoolType> raInput = Views.extendValue(input,
				new BoolType(!isInverted)).randomAccess();
		final Cursor<BoolType> cInput = Views.flatIterable(input).cursor();
		final ClockwiseMooreNeighborhoodIterator<BoolType> cNeigh = new ClockwiseMooreNeighborhoodIterator<BoolType>(
				raInput);

		double[] position = new double[2];
		double[] startPos = new double[2];

		// find first black pixel
		while (cInput.hasNext()) {
			// we are looking for a black pixel
			if (cInput.next().get() == isInverted) {
				raInput.setPosition(cInput);
				raInput.localize(startPos);

				// add to polygon
				p.add(new RealPoint(startPos[0], startPos[1]));

				// backtrack:
				raInput.move(-1, 0);

				cNeigh.reset();

				while (cNeigh.hasNext()) {
					if (cNeigh.next().get() == isInverted) {

						boolean specialBacktrack = false;

						raInput.localize(position);
						if (startPos[0] == position[0]
								&& startPos[1] == position[1]) {
							// startPoint was found.
							if (useJacobs) {
								// Jacobs stopping criteria
								final int index = cNeigh.getIndex();
								if (index == 1 || index == 0) {
									// Jonathans refinement to the
									// non-terminating jacobs criteria
									specialBacktrack = true;
								} else if (index == 2 || index == 3) {
									// if index is 2 or 3, we entered the pixel
									// by moving {1, 0}, therefore in the same
									// way.
									break;
								} // else criteria not fulfilled, continue.
							} else {
								break;
							}
						}
						// add found point to polygon
						p.add(new RealPoint(position[0], position[1]));

						if (specialBacktrack) {
							cNeigh.backtrackSpecial();
						} else {
							cNeigh.backtrack();
						}
					}
				}

				break; // we only need to extract one contour.
			}
		}

		return new Polygon(p);
	}
	
	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}
}
