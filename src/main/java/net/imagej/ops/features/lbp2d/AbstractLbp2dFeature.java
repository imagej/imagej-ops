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
package net.imagej.ops.features.lbp2d;

import java.util.ArrayList;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.Contingent;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;

/**
 * 
 * Abstract class for 2d local binary pattern feature
 * 
 * @author Andreas Graumann
 *
 * @param <I>
 * @param <O>
 */
public abstract class AbstractLbp2dFeature<I extends RealType<I>>
		extends AbstractHybridOp<RandomAccessibleInterval<I>, ArrayList<LongType>>implements Lbp2dFeature<I>, Contingent {

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}
	
	/**
	 * 
	 * @author Andreas Graumann, University of Konstanz
	 * @author Jonathan Hale, University of Konstanz
	 *
	 * @param <T>
	 */
	final class ClockwiseDistanceNeighborhoodIterator<T extends Type<T>> implements java.util.Iterator<T> {
        final private RandomAccess<T> m_ra;
        
        final private int m_distance;

        final private int[][] CLOCKWISE_OFFSETS = {{0, -1}, {1, 0}, {1, 0}, {0, 1}, {0, 1}, {-1, 0}, {-1, 0}, {0, -1}};


        //index of offset to be executed at next next() call.
        private int m_curOffset = 0;


        private int m_startIndex = 8;

        public ClockwiseDistanceNeighborhoodIterator(final RandomAccess<T> ra, final int distance) {
            m_ra = ra;
            m_distance = distance;
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
        	m_ra.move(CLOCKWISE_OFFSETS[m_curOffset][0]*m_distance,0);
        	m_ra.move(CLOCKWISE_OFFSETS[m_curOffset][1]*m_distance,1);
           
            m_curOffset++;// = (m_curOffset + 1) & 7; //<=> (m_curOffset+1) % 8
            
            return m_ra.get();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final void remove() {
            throw new UnsupportedOperationException();
        }

        public final int getIndex() {
            return m_curOffset;
        }

        /**
         * Reset the current offset index. This does not influence the RandomAccess.
         */
        public final void reset() {
            m_curOffset = 0;
            m_startIndex = 8;
        }

    }

}
