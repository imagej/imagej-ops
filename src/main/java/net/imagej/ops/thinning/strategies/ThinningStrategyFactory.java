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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class ThinningStrategyFactory {

    private boolean m_foreground;

    public ThinningStrategyFactory(final boolean foreground)
    {
        m_foreground = foreground;
    }

    public static enum Strategy {

        MORPHOLOGICAL, HILDITCH, ZHANGSUEN, GUOHALL;

        public static List<String> getNames() {
            Strategy[] algorithms = values();
            List<String> names = new LinkedList<String>();

            for (int i = 0; i < algorithms.length; ++i) {
                names.add(algorithms[i].toString());
            }
            return names;

        }

        @Override
        public String toString() {
            switch (this) {
                case MORPHOLOGICAL:
                    return "Morphological Thinning";
                case HILDITCH:
                    return "Hilditch Algorithm";
                case ZHANGSUEN:
                    return "Zhang-Suen Algorithm";
                case GUOHALL:
                    return "Guo-Hall Algorithm";
                default:
                    throw new IllegalArgumentException();
            }
        }
    };

    public ThinningStrategy getStrategy(final Strategy strategy) {
        switch (strategy) {
            case MORPHOLOGICAL:
                return new MorphologicalThinning(m_foreground);
            case HILDITCH:
                return new HilditchAlgorithm(m_foreground);
            case ZHANGSUEN:
                return new ZhangSuenAlgorithm(m_foreground);
            case GUOHALL:
                return new GuoHallAlgorithm(m_foreground);
            default:
                return new MorphologicalThinning(m_foreground);
        }

    }

    public ThinningStrategy getStrategy(final String strategy) {
        for(Strategy s: Strategy.values())
        {
            if (s.toString().equals(strategy)) {
                return getStrategy(s);
            }
        }
        return new MorphologicalThinning(m_foreground);
    }
}
