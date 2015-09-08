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
 * Implementation of the thinning algorithm proposed by T. Y. Zhang and C. Y. Suen.
 * @author Andreas Burger, University of Konstanz
 */
public class ZhangSuenAlgorithm extends Abstract3x3NeighbourhoodThinning {

    private int iteration = 0;

    /**
     * Create a new Zhang-Suen thinning strategy. The passed boolean will represent the foreground-value of the image.
     *
     * @param foreground Value determining the boolean value of foreground pixels.
     */
    public ZhangSuenAlgorithm(final boolean foreground)
    {
        super(foreground);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removePixel(final long[] position, final RandomAccessible<BitType> accessible) {

        // Setup
        RandomAccess<BitType> access = accessible.randomAccess();
        access.setPosition(position);

        boolean[] vals = getNeighbourhood(access);

        // First two conditions are similar to Hilditch-Thinning.
        int numForeground = 0;

        for (int i = 1; i < vals.length; ++i) {
            if (vals[i] == m_foreground) {
                ++numForeground;
            }
        }

        if (!(2 <= numForeground && numForeground <= 6)) {
            return false;
        }
        int numPatternSwitches = findPatternSwitches(vals);
        if (!(numPatternSwitches == 1)) {
            return false;
        }

        // Currently, this thinning algorithm runs as 1-iteration-per-cycle, since the order of operations is not important.
        if ((iteration % 2) != 1) {
            return evenIteration(vals);
        } else {
            return oddIteration(vals);
        }

    }

    // Check for background pixels in the vicinity.
    private boolean evenIteration(final boolean[] vals) {
        if (!(vals[1] == m_background || vals[3] == m_background || vals[5] == m_background)) {
            return false;
        }

        if (!(vals[3] == m_background || vals[5] == m_background || vals[7] == m_background)) {
            return false;
        }

        return true;
    }

    // Variation of the checks in an even iteration.
    private boolean oddIteration(final boolean[] vals) {
        if (!(vals[1] == m_background || vals[3] == m_background || vals[7] == m_background)) {
            return false;
        }

        if (!(vals[1] == m_background || vals[5] == m_background || vals[7] == m_background)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCycle() {
        ++iteration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThinningStrategy copy() {
        return new ZhangSuenAlgorithm(m_foreground);
    }


}
