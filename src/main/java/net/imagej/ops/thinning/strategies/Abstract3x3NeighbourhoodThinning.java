/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
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
 * Created on 01.12.2013 by Andreas
 */
package net.imagej.ops.thinning.strategies;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

/**
 *  This abstract class represents an rough framework for a Thinning algorithm operating on a 3x3 neighbourhood by providing
 *  some often needed functions.
 *
 * @author Andreas Burger, University of Konstanz
 */
public abstract class Abstract3x3NeighbourhoodThinning implements ThinningStrategy {

    /**
     * Boolean value of the foreground.
     */
    protected boolean m_foreground;

    /**
     * Boolean value of the background.
     */
    protected boolean m_background;

    /**
     * Create a new abstract thinning strategy. The passed boolean will represent the foreground-value of the image.
     *
     * @param foreground Value determining the boolean value of foreground pixels.
     */
    protected Abstract3x3NeighbourhoodThinning(final boolean foreground)
    {
        m_foreground = foreground;
        m_background = !foreground;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removePixel(final long[] position, final RandomAccessible<BitType> access) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Returns all booleans in a 3x3 neighbourhood of the pixel the RandomAccess points to.
     * These booleans are stored in an Array in the following order: <br>
     *
     *  8   1   2 <br>
     *  7   0   3 <br>
     *  6   5   4 <br>
     *
     * @param access A RandomAccess pointing to a pixel of the image
     * @return A boolean Array holding the values of the neighbourhood in clockwise order.
     */
    protected boolean[] getNeighbourhood(final RandomAccess<BitType> access) {
        boolean[] vals = new boolean[9];

        vals[0] = access.get().get();

        access.move(-1, 1);
        vals[1] = access.get().get();

        access.move(1, 0);
        vals[2] = access.get().get();

        access.move(1, 1);
        vals[3] = access.get().get();

        access.move(1, 1);
        vals[4] = access.get().get();

        access.move(-1, 0);
        vals[5] = access.get().get();

        access.move(-1, 0);
        vals[6] = access.get().get();

        access.move(-1, 1);
        vals[7] = access.get().get();

        access.move(-1, 1);
        vals[8] = access.get().get();

        return vals;

    }

    /**
     * Returns the amount of switches from foreground to background occurring in the circle around vals[1]
     * @param vals Boolean Array holding the neighbourhood.
     * @return Amount of true-false switches in the neighbourhood.
     */
    protected int findPatternSwitches(final boolean[] vals) {
        int res = 0;
        for (int i = 1; i < vals.length - 1; ++i) {
            if (vals[i] == m_foreground && vals[i + 1] == m_background) {
                ++res;
            }
            if (vals[vals.length - 1] == m_foreground && vals[1] == m_background) {
                ++res;
            }
        }
        return res;
    }

    @Override
    public void afterCycle() {
        // Intentionally left blank.
    }


    @Override
    public int getIterationsPerCycle() {
        return 1;
    }

}
