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
 *  An implementation of the Algorithm proposed by C. J. Hilditch.
 *
 * @author Andreas Burger, University of Konstanz
 */
public class HilditchAlgorithm extends Abstract3x3NeighbourhoodThinning {

    /**
     * Create a new hilditch strategy. The passed boolean will represent the foreground-value of the image.
     *
     * @param foreground Value determining the boolean value of foreground pixels.
     */
    public HilditchAlgorithm(final boolean foreground)
    {
        super(foreground);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removePixel(final long[] position, final RandomAccessible<BitType> accessible) {
        RandomAccess<BitType> access = accessible.randomAccess();
        access.setPosition(position);

        boolean[] vals = getNeighbourhood(access);

        // First condition is to ensure there are at least 2 and at most 6 neighbouring foreground pixels.
        int numForeground = 0;
        for (int i = 1; i < vals.length; ++i) {
            if (vals[i] == m_foreground) {
                ++numForeground;
            }
        }

        if (!(2 <= numForeground && numForeground <= 6)) {
            return false;
        }

        // Second condition checks for transitions between foreground and background. Exactly 1 such transition
        // is required.
        int numPatterns = findPatternSwitches(vals);
        if (!(numPatterns == 1)) {
            return false;
        }

        // The third and fourth conditions require neighbourhoods of adjacent pixels.

        // Access has to be reset to current image-position before moving it, since
        // the getNeighbourhood() method moves it to the top-left of the initial pixel.
        access.setPosition(position);
        access.move(-1, 1);
        int p2Patterns = findPatternSwitches((getNeighbourhood(access)));
        if (!( (vals[1] == m_background || vals[3] == m_background || vals[7] == m_background) || p2Patterns != 1)) {
            return false;
        }

        access.setPosition(position);
        access.move(1, 0);
        int p4Patterns = findPatternSwitches((getNeighbourhood(access)));

        if (!((vals[1] == m_background || vals[3] == m_background || vals[5] == m_background) || p4Patterns != 1)) {
            return false;
        }

        // If all conditions are met, we can safely remove the pixel.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThinningStrategy copy() {
        return new HilditchAlgorithm(m_foreground);
    }


}
