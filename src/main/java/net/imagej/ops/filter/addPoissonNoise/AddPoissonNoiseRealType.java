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

package net.imagej.ops.filter.addPoissonNoise;

import java.util.Random;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to a real number sampled from
 * a Poisson distribution with lambda of the input real number.
 * <p>
 * Implementation according to:
 * <p>D. E. Knuth.<br/>
 * Art of Computer Programming, Volume 2: Seminumerical Algorithms (3rd Edition).<br/>
 * Addison-Wesley Professional, November 1997
 *  
 * @author Jan Eglinger
 */
@Plugin(type = Ops.Filter.AddPoissonNoise.class, name = Ops.Filter.AddPoissonNoise.NAME)
public class AddPoissonNoiseRealType<I extends RealType<I>, O extends RealType<O>>
	extends AbstractComputerOp<I, O> implements Ops.Filter.AddPoissonNoise
{
	@Parameter(required = false)
	private long seed = 0xabcdef1234567890L;
	
	private Random rng;

	public long getSeed() {
		return seed;
	}
	
	public void setSeed(final long seed) {
		this.seed = seed;
	}
	
	@Override
	public void compute(final I input, final O output) {
		if (rng == null) rng = new Random(seed);
		double l = Math.exp(-(input.getRealDouble()));
		int k = 0;
		double p = 1;
		do {
			k++;
			p *= rng.nextDouble();
		} while (p >= l);
		output.setReal(k-1);
		return;
	}
}
