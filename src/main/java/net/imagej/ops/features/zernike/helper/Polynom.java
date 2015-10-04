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
package net.imagej.ops.features.zernike.helper;

/**
 * class used to represent a zernike moment polynomial.
 * 
 * @author Andreas Graumann, University of Konstanz
 */
public class Polynom {
    /** the array of polynom coefficients. */
    private final int[] m_coefficients;

    /** the degree of the polynom. */
    private final int m_degree;

    /**
     * default constructor.
     * 
     * @param degree the degree of the polynom
     */
    public Polynom(final int degree) {
        m_degree = degree;
        m_coefficients = new int[m_degree + 1];
        for (int i = 0; i <= m_degree; ++i) {
            setCoefficient(i, 0);
        }
    }

    /**
     * set the coefficient at a position.
     * 
     * @param pos the position (the power of the monom)
     * @param coef the coefficient
     */
    public void setCoefficient(final int pos, final int coef) {
        m_coefficients[pos] = coef;
    }

    /**
     * return the coefficient at a given position.
     * 
     * @param pos the position
     * @return the coefficient
     */
    public int getCoefficient(final int pos) {
        return m_coefficients[pos];
    }

    /**
     * return the value of the polynom in a given point.
     * 
     * @param x the point
     * @return the value of the polynom
     */
    public double evaluate(final double x) {
        double power = 1.0;
        double result = 0.0;
        for (int i = 0; i <= m_degree; ++i) {
            result += m_coefficients[i] * power;
            power *= x;
        }
        return result;
    }

    /**
     * provide a String representation of this polynom. mostly for debugging purposes and for the JUnit test case
     * 
     * @return the String representation
     */
    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        for (int i = m_degree; i >= 0; i--) {
            if (m_coefficients[i] != 0) {
                result.append(m_coefficients[i] + "X^" + i + " ");
            }
        }
        return result.toString();
    }
}
