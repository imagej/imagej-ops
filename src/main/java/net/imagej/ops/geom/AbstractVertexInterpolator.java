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
package net.imagej.ops.geom;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Contingent;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * This is the {@link AbstractVertexInterpolator}. A vertex interpolator
 * computes the real coordinates based on the pixel intensities.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public abstract class AbstractVertexInterpolator extends AbstractOp
		implements
			VertexInterpolator,
			Contingent {

	@Parameter(type = ItemIO.INPUT)
	int[] p1;

	@Parameter(type = ItemIO.INPUT)
	int[] p2;

	@Parameter(type = ItemIO.INPUT)
	double p1Value;

	@Parameter(type = ItemIO.INPUT)
	double p2Value;

	@Parameter(type = ItemIO.OUTPUT)
	double[] output;
	
	@Override
	public void setPoint1(int[] p) {
		p1 = p;
	}
	
	@Override
	public void setPoint2(int[] p) {
		p2 = p;
	}
	
	@Override
	public void setValue1(double d) {
		p1Value = d;
	}
	
	@Override
	public void setValue2(double d) {
		p2Value = d;
	}
	
	@Override
	public double[] getOutput() {
		return output;
	}
	
	@Override
	public boolean conforms() {
		return p1.length == 3 && p2.length == 3;
	}
}
