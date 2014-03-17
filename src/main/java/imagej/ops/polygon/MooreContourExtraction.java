/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2014
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * Created on Jan 31, 2014 by Jonathan Hale
 */

package imagej.ops.polygon;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;

import java.awt.Polygon;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * MooreContourExtractionOp Implementation of the Moore contour extraction
 * algorithm. Input: BitType RandomAccessible Output: A list of Polygons
 * representing the found contours. Warning! There will be alot of duplicates.
 * This operation only extracts one contour and then terminates. If you have
 * multiple contours you want to extract, you may want to perform a CCA on them
 * and then use BitMaskProvider to extract contours from the images of the
 * labels. Also, using the the Jacobs Stopping Criterion is recommended, but
 * turned on by default anyway. It leads to better results and doesn't
 * significantly slow down the extraction.
 * 
 * @author Jonathan Hale (University of Konstanz)
 */

@Plugin(type = Op.class, name = ContourExtraction.NAME,
	label = ContourExtraction.LABEL)
public class MooreContourExtraction extends
	AbstractFunction<RandomAccessibleInterval<BitType>, Polygon> implements
	ContourExtraction<RandomAccessibleInterval<BitType>, Polygon>
{

	/**
	 * ClockwiseMooreNeighborhoodIterator Iterates clockwise through a 2D Moore
	 * Neighborhood (8 connected Neighborhood). This iterator encourages reuse!
	 * Reset the iterator and move the underlying random accessible, do not create
	 * new ones. That is more resource efficient and faster.
	 * 
	 * @author Jonathan Hale (University of Konstanz)
	 */
	final class ClockwiseMooreNeighborhoodIterator<T extends Type<T>> implements
		java.util.Iterator<T>
	{

		final private RandomAccess<T> ra;

		final private int[][] CLOCKWISE_OFFSETS = { { 0, -1 }, { 1, 0 }, { 1, 0 },
			{ 0, 1 }, { 0, 1 }, { -1, 0 }, { -1, 0 }, { 0, -1 } };

		final private int[][] CCLOCKWISE_OFFSETS = { { 0, 1 }, { 0, 1 }, { -1, 0 },
			{ -1, 0 }, { 0, -1 }, { 0, -1 }, { 1, 0 }, { 1, 0 } };

		// index of offset to be executed at next next() call.
		private int curOffset = 0;

		// startIndex basically tells the Cursor when it performed
		// every relative movement in CLOCKWISE_OFFSETS once. After
		// backtrack, this is reset to go through all 8 offsets again.
		private int startIndex = 7;

		public ClockwiseMooreNeighborhoodIterator(final RandomAccess<T> ra) {
			this.ra = ra;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final boolean hasNext() {
			return (curOffset != startIndex);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final T next() {
			ra.move(CLOCKWISE_OFFSETS[curOffset]);
			curOffset = (curOffset + 1) & 7; // <=> (m_curOffset+1) % 8
			return ra.get();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		public final void backtrack() {
			final int[] back = CCLOCKWISE_OFFSETS[curOffset];
			ra.move(back); // undo last move

			// find out, where to continue:
			if (back[0] == 0) {
				if (back[1] == 1) {
					curOffset = 6;
				}
				else {
					curOffset = 2;
				}
			}
			else {
				if (back[0] == 1) {
					curOffset = 4;
				}
				else {
					curOffset = 0;
				}
			}

			startIndex = (curOffset + 7) & 7; // set the Pixel to stop at
		}

		public final int getIndex() {
			return curOffset;
		}

		/**
		 * Reset the current offset index. This does not influence the RandomAccess.
		 */
		public final void reset() {
			curOffset = 0;
			startIndex = 7;
		}
	}

	final private boolean jacobs;
	final private boolean inverted;

	/**
	 * MooreContourExtractionOp Default Constructor 'useJacobsCriteria' is turned
	 * on by default.
	 */
	public MooreContourExtraction() {
		this(true);
	}

	/**
	 * MooreContourExtractionOp
	 * 
	 * @param useJacobsCriteria - Set this flag to use "Jacobs stopping criteria"
	 */
	public MooreContourExtraction(final boolean useJacobsCriteria) {
		this(useJacobsCriteria, false);
	}

	/**
	 * MooreContourExtractionOp
	 * 
	 * @param useJacobsCriteria - Set this flag to use "Jacobs stopping criteria"
	 * @param inverted - Set this to your foreground value (default false)
	 */
	public MooreContourExtraction(final boolean useJacobsCriteria,
		final boolean inverted)
	{
		this.jacobs = useJacobsCriteria;
		this.inverted = inverted;
	}

	/**
	 * {@inheritDoc} Note that the output Polygon is cleared before the contour
	 * extraction.
	 */
	@Override
	public Polygon compute(final RandomAccessibleInterval<BitType> input,
		Polygon output)
	{

		if (output == null) {
			output = new Polygon();
			setOutput(output);
		}

		final RandomAccess<BitType> raInput =
			Views.extendValue(input, new BitType(!inverted)).randomAccess();
		final Cursor<BitType> cInput = Views.flatIterable(input).cursor();
		final ClockwiseMooreNeighborhoodIterator<BitType> cNeigh =
			new ClockwiseMooreNeighborhoodIterator<BitType>(raInput);

		final int[] position = new int[2];
		final int[] startPos = new int[2];

		final int[] FIRST_BACKTRACK = new int[] { -1, 0 };

		// clear out all the points
		output.reset();

		cInput.fwd();

		// find first black pixel
		while (cInput.hasNext()) {
			// we are looking for a black pixel
			if (cInput.next().get() == inverted) {
				raInput.setPosition(cInput);
				raInput.localize(startPos);

				// add to polygon
				output.addPoint(startPos[0], startPos[1]);

				// backtrack:
				raInput.move(FIRST_BACKTRACK); // manual moving back one on x-axis

				cNeigh.reset();

				while (cNeigh.hasNext()) {
					if (cNeigh.next().get() == inverted) {
						raInput.localize(position);
						if (startPos[0] == position[0] && startPos[1] == position[1]) {
							// startPoint was found.
							if (jacobs) {
								// jacobs stopping criteria
								if ((cNeigh.getIndex() - 2) < 2) {
									// if index is 2 or 3, we entered the pixel
									// by moving {1, 0}, therefore in the same way.
									break;
								} // else criteria not fulfilled, continue.
							}
							else {
								break;
							}
						}
						// add found point to polygon
						output.addPoint(position[0], position[1]);
						cNeigh.backtrack();
					}
				}

				break; // we only need to extract one contour.
			}
		}

		return output;
	}

}
