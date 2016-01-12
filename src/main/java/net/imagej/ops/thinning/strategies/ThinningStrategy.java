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

import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

/**
 * An interface for a simple thinning strategy employed by the thinningOp.
 * @author Andreas Burger, University of Konstanz
 */
public interface ThinningStrategy {

    /**
     * This method should determine whether to keep a foreground pixel or not.
     *
     * @param position Long Array containing the current position in the image.
     * @param access The image to thin.
     * @return True if pixel can be switched to background, false otherwise.
     */
    public boolean removePixel(final long[] position, final RandomAccessible<BitType> accessible);

    /**
     * Returns the minimum number of iterations necessary for the algorithm to run. This delays termination of
     * the thinning algorithm until the end of the current cycle. If, for example, no changes occur during the second
     * iteration of a 4-iteration-cycle, iterations 3 and 4 still take place.
     *
     * @return The number of iterations per cycle.
     */
    public int getIterationsPerCycle();

    /**
     * Called by the ThinningOp after each cycle, and thus exactly getIterationsPerCycle()-times per iteration. Used for
     * performing different calculations in each step of the cycle.
     */
    public void afterCycle();

    /**
     * Returns a seperate copy of this strategy.
     * @return A new instance of this strategy with the same values.
     */
    public ThinningStrategy copy();

}
