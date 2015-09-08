/*
* ------------------------------------------------------------------------
*
* Copyright (C) 2003 - 2013
* University of Konstanz, Germany and
* KNIME GmbH, Konstanz, Germany
* Website: http://www.knime.org; Email: contact@knime.org
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License, Version 3, as
* published by the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful, but
* WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see <http://www.gnu.org/licenses>.
*
* Additional permission under GNU GPL version 3 section 7:
*
* KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
* Hence, KNIME and ECLIPSE are both independent programs and are not
* derived from each other. Should, however, the interpretation of the
* GNU GPL Version 3 ("License") under any applicable laws result in
* KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
* you the additional permission to use and propagate KNIME together with
* ECLIPSE with only the license terms in place for ECLIPSE applying to
* ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
* license terms of ECLIPSE themselves allow for the respective use and
* propagation of ECLIPSE together with KNIME.
*
* Additional permission relating to nodes for KNIME that extend the Node
* Extension (and in particular that are based on subclasses of NodeModel,
* NodeDialog, and NodeView) and that only interoperate with KNIME through
* standard APIs ("Nodes"):
* Nodes are deemed to be separate and independent programs and to not be
* covered works. Notwithstanding anything to the contrary in the
* License, the License does not apply to Nodes, you are not required to
* license Nodes under the License, and you are granted a license to
* prepare and propagate Nodes, in each case even if such Nodes are
* propagated with or for interoperation with KNIME. The owner of a Node
* may freely choose the license terms applicable to such Node, including
* when such Node is propagated with or for interoperation with KNIME.
* ---------------------------------------------------------------------
*
* Created on 07.11.2013 by Daniel
*/
package net.imagej.ops.thinning;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.ops.operation.UnaryOperation;
import net.imglib2.type.logic.BitType;
import net.imglib2.view.Views;

import net.imagej.ops.thinning.strategies.ThinningStrategy;

/**
 * Thinning Operation
 *
 * @author Andreas Burger, University of Konstanz
 */
public class ThinningOp implements UnaryOperation<RandomAccessibleInterval<BitType>, RandomAccessibleInterval<BitType>> {

    private boolean m_foreground = true;

    private boolean m_background = false;

    private ThinningStrategy m_strategy;

    private ImgFactory<BitType> m_factory;

    /**
     * Instantiate a new ThinningOp using the given strategy and considering the given boolean value as foreground.
     *
     * @param strategy thinning strategy to use
     * @param foreground Boolean value of foreground pixels.
     * @param factory for temporary result image
     */
    public ThinningOp(final ThinningStrategy strategy, final boolean foreground, final ImgFactory<BitType> factory) {
        m_strategy = strategy;
        m_foreground = foreground;
        m_background = !foreground;
        m_factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomAccessibleInterval<BitType> compute(final RandomAccessibleInterval<BitType> input,
                                                     final RandomAccessibleInterval<BitType> output) {

        // Create a new image as a buffer to store the thinning image in each iteration.
        // This image and output are swapped each iteration since we need to work on the image
        // without changing it.
        final Img<BitType> buffer = m_factory.create(input, new BitType());

        final IterableInterval<BitType> it1 = Views.iterable(buffer);
        final IterableInterval<BitType> it2 = Views.iterable(output);

        // Extend the buffer in order to be able to iterate care-free later.
        final RandomAccessible<BitType> ra1 = Views.extendBorder(buffer);
        final RandomAccessible<BitType> ra2 = Views.extendBorder(output);
         RandomAccessible<BitType> currRa = Views.extendBorder(input); // Used only in first iteration.

        // Create cursors.
        final Cursor<BitType> firstCursor =  it1.localizingCursor();
        Cursor<BitType> currentCursor = Views.iterable(input).localizingCursor();
        final Cursor<BitType> secondCursor = it2.localizingCursor();

        // Create pointers to the current and next cursor and set them to Buffer and output respectively.
        Cursor<BitType>  nextCursor;
        nextCursor = secondCursor;

        // The main loop.
        boolean changes = true;
        int i = 0;
        // Until no more changes, do:
        while (changes) {
            changes = false;
            // This For-Loop makes sure, that iterations only end on full cycles (as defined by the strategies).
            for (int j = 0; j < m_strategy.getIterationsPerCycle(); ++j) {
                // For each pixel in the image.
                while (currentCursor.hasNext()) {
                    // Move both cursors
                    currentCursor.fwd();
                    nextCursor.fwd();
                    // Get the position of the current cursor.
                    long[] coordinates = new long[currentCursor.numDimensions()];
                    currentCursor.localize(coordinates);

                    // Copy the value of the image currently operated upon.
                    boolean curr = currentCursor.get().get();
                    nextCursor.get().set(curr);

                    // Only foreground pixels may be thinned
                    if (curr == m_foreground) {

                        // Ask the strategy whether to flip the foreground pixel or not.
                        boolean flip = m_strategy.removePixel(coordinates, currRa);

                        // If yes - change and keep track of the change.
                        if (flip) {
                            nextCursor.get().set(m_background);
                            changes = true;
                        }
                    }
                }
                // One step of the cycle is finished, notify the strategy.
                m_strategy.afterCycle();

                // Reset the cursors to the beginning and assign pointers for the next iteration.
                currentCursor.reset();
                nextCursor.reset();

                // Keep track of the most recent image. Needed for output.
                if (currRa == ra2) {
                    currRa = ra1;
                    currentCursor = firstCursor;
                    nextCursor = secondCursor;
                } else {
                    currRa = ra2;
                    currentCursor = secondCursor;
                    nextCursor = firstCursor;
                }

                // Keep track of iterations.
                ++i;
            }
        }

        // Depending on the iteration count, the final image is either in ra1 or ra2. Copy it to output.
        if (i % 2 == 0) {
            //Ra1 points to img1, ra2 points to output.
            copy(buffer, output);

        }

        return output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnaryOperation<RandomAccessibleInterval<BitType>, RandomAccessibleInterval<BitType>> copy() {
        return new ThinningOp(m_strategy.copy(), m_foreground, m_factory);
    }

    private void copy(final RandomAccessibleInterval<BitType> source, final RandomAccessibleInterval<BitType> target) {
        IterableInterval<BitType> targetIt = Views.iterable(target);
        IterableInterval<BitType> sourceIt = Views.iterable(source);

        if (sourceIt.iterationOrder().equals(targetIt.iterationOrder())) {
            Cursor<BitType> targetCursor = targetIt.cursor();
            Cursor<BitType> sourceCursor = sourceIt.cursor();
            while (sourceCursor.hasNext()) {
                targetCursor.fwd();
                sourceCursor.fwd();
                targetCursor.get().set(sourceCursor.get().get());
            }
        } else { // Fallback to random access
            RandomAccess<BitType> targetRA = target.randomAccess();
            Cursor<BitType> sourceCursor = sourceIt.localizingCursor();
            while (sourceCursor.hasNext()) {
                sourceCursor.fwd();
                targetRA.setPosition(sourceCursor);
                targetRA.get().set(sourceCursor.get().get());
            }
        }
    }

}