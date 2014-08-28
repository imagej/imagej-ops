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
package net.imagej.ops.descriptors.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment02Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment03Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment11Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment12Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment20Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment21Generic;
import net.imagej.ops.descriptors.moments.helper.generic.NormalizedCentralMoment30Generic;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * @author Andreas Graumann (University of Konstanz)
 */
@Plugin(type = Op.class, label = "Hu Moments")
public class HuMoments implements OutputOp<double[]> {

	@Parameter
	private NormalizedCentralMoment20Generic n20;

	@Parameter
	private NormalizedCentralMoment02Generic n02;

	@Parameter
	private NormalizedCentralMoment30Generic n30;

	@Parameter
	private NormalizedCentralMoment12Generic n12;

	@Parameter
	private NormalizedCentralMoment21Generic n21;

	@Parameter
	private NormalizedCentralMoment03Generic n03;

	@Parameter
	private NormalizedCentralMoment11Generic n11;

	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	@Override
	public double[] getOutput() {
		return output;
	}

	@Override
	public void run() {

		double n11 = this.n11.getOutput();
		double n02 = this.n02.getOutput();
		double n20 = this.n20.getOutput();
		double n12 = this.n12.getOutput();
		double n21 = this.n21.getOutput();
		double n03 = this.n03.getOutput();
		double n30 = this.n30.getOutput();

		double[] result = new double[7];

		result[0] = n20 + n02;
		result[1] = Math.pow(n20 - n02, 2) + 4 * Math.pow(n11, 2);
		result[2] = Math.pow(n30 - 3 * n12, 2) + Math.pow(3 * n21 - n03, 2);
		result[3] = Math.pow(n30 + n12, 2) + Math.pow(n21 + n03, 2);
		result[4] = (n30 - 3 * n12) * (n30 + n12)
				* (Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2))
				+ (3 * n21 - n03) * (n21 + n03)
				* (3 * Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2));
		result[5] = (n20 - n02)
				* (Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2)) + 4 * n11
				* (n30 + n12) * (n21 + n03);
		result[6] = (3 * n21 - n03) * (n30 + n12)
				* (Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2))
				- (n30 - 3 * n12) * (n21 + n03)
				* (3 * Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2));

		output = result;
	}
}
