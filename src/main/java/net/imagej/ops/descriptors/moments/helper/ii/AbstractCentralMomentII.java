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
package net.imagej.ops.descriptors.moments.helper.ii;

import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.geometric.CenterOfGravity;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * 
 * @author Christian Dietz (University of Konstanz)
 * @author Andreas Graumann (University of Konstanz)
 * 
 */
public abstract class AbstractCentralMomentII implements OutputOp<Double> {

	@Parameter
	private IterableInterval<? extends RealType<?>> ii;

	@Parameter(type = ItemIO.INPUT)
	private Moment00II m00;

	@Parameter(type = ItemIO.INPUT)
	private Moment01II m01;

	@Parameter(type = ItemIO.INPUT)
	private Moment10II m10;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;

	@Override
	public Double getOutput() {
		return output;
	}

	@Override
	public void run() {

		final double centerX = m10.getOutput() / m00.getOutput();
		final double centerY = m01.getOutput() / m00.getOutput();

		final int p = getP();
		final int q = getQ();

		double result = 0.0;

		final Cursor<? extends RealType<?>> it = ii.localizingCursor();
		while (it.hasNext()) {
			it.fwd();
			final double x = it.getIntPosition(0) + 1 - centerX;
			final double y = it.getIntPosition(1) + 1 - centerY;

			result += it.get().getRealDouble() * Math.pow(x, p)
					* Math.pow(y, q);
		}

		output = result;
	}

	protected abstract int getQ();

	protected abstract int getP();
}
