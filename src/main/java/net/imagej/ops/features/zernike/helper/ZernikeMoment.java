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

import net.imagej.types.BigComplex;

/**
 * Class to hold a zernike moment, including its polynom, order, repition and
 * complex representation
 * 
 * @author Andreas Graumann, University of Konstanz
 *
 */
public class ZernikeMoment {
	private Polynom p;
	private int n;
	private int m;
	private BigComplex zm;
	public double real = 0;
	public double imag = 0;

	/**
	 * 
	 * @return Phase of moment * 180/PI
	 */
	public double getPhase() {
		return zm.getPhaseDouble() * 180 / Math.PI;
	}

	/**
	 * 
	 * @return Magnitude of moment
	 */
	public double getMagnitude() {
		return zm.getPowerDouble();
	}

	/**
	 * 
	 * @return Zernime moment in complex representation
	 */
	public BigComplex getZm() {
		return zm;
	}

	/**
	 * 
	 * Set zernike moment in complex representation
	 * 
	 * @param _zm
	 */
	public void setZm(BigComplex _zm) {
		this.zm = _zm;
	}

	/**
	 * 
	 * @return Get radial polynom p
	 */
	public Polynom getP() {
		return p;
	}

	/**
	 * 
	 * Set radial polynom p
	 * 
	 * @param _p
	 */
	public void setP(Polynom _p) {
		this.p = _p;
	}

	/**
	 * 
	 * @return Order n
	 */
	public int getN() {
		return n;
	}

	/**
	 * 
	 * Set order n
	 * 
	 * @param _n
	 */
	public void setN(int _n) {
		this.n = _n;
	}

	/**
	 * 
	 * @return Repitition m
	 */
	public int getM() {
		return m;
	}

	/**
	 * 
	 * Set repitition m
	 * 
	 * @param _m
	 */
	public void setM(int _m) {
		this.m = _m;
	}

	/**
	 * Print zernike moment in complex representation
	 */
	public void printComplex() {
		String sign = "+";
		if (zm.getImaginaryDouble() < 0)
			sign = "-";
		System.out.println("Complex Representation: " + zm.getRealDouble() + " " + sign + " "
				+ Math.abs(zm.getImaginaryDouble()) + "i");
	}

	/**
	 * Print Zernike moment containing order, representation, polyonom, complex,
	 * phase and magnitude
	 */
	public void printMoment() {
		System.out.println("N: " + getN() + " M: " + getM());
		System.out.println("Polynom: " + getP().toString());
		printComplex();
		System.out.println("Phase: " + (double) Math.round(getPhase() * 10000) / 10000);
		System.out.println("Magnitude: " + (double) Math.round(getMagnitude() * 10000) / 10000);

		System.out.println("");
	}
}
