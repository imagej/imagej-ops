/*
 * #%L
 * A framework for reusable algorithms.
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

package imagej.ops.normalize;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Normalize.NAME, attrs = { @Attr(
	name = "aliases", value = Normalize.ALIASES) })
public class NormalizeRealType<T extends RealType<T>> extends
	AbstractFunction<T, T> implements Normalize
{

	@Parameter
	private double oldMin;

	@Parameter
	private double oldMax;

	@Parameter
	private double newMin;

	@Parameter
	private double newMax;

	@Parameter
	private double factor;

	@Override
	public T compute(T input, T output) {
		output.setReal(Math.min(newMax, Math.max(newMin,
			(input.getRealDouble() - oldMin) * factor + newMin)));
		return output;
	}

	/**
	 * Determines the factor to map the interval [oldMin, oldMax] to
	 * [newMin,newMax].
	 * 
	 * @param oldMin
	 * @param oldMax
	 * @param newMin
	 * @param newMax
	 * @return the normalization factor
	 */
	public static double normalizationFactor(double oldMin, double oldMax,
		double newMin, double newMax)
	{
		return 1.0d / (oldMax - oldMin) * ((newMax - newMin));
	}

}
