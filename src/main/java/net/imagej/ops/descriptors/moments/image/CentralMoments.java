/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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
package net.imagej.ops.descriptors.moments.image;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment02II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment03II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment11II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment12II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment20II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment21II;
import net.imagej.ops.descriptors.moments.helper.ii.CentralMoment30II;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Central Moments")
public class CentralMoments implements OutputOp<double[]> {

	@Parameter
	private CentralMoment02II m02;

	@Parameter
	private CentralMoment03II m03;

	@Parameter
	private CentralMoment11II m11;

	@Parameter
	private CentralMoment12II m12;

	@Parameter
	private CentralMoment20II m20;

	@Parameter
	private CentralMoment21II m21;

	@Parameter
	private CentralMoment30II m30;

	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void run() {
		final double[] result = new double[7];

		result[0] = m11.getOutput();
		result[1] = m02.getOutput();
		result[2] = m20.getOutput();
		result[3] = m12.getOutput();
		result[4] = m21.getOutput();
		result[5] = m30.getOutput();
		result[6] = m03.getOutput();

		output = result;
	}

}
