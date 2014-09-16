/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.scale.pixel;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.map.Map;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link GenericScale} on {@link Iterable}s of {@link RealType}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 * @param <V>
 */
@Plugin(type = Op.class, name = GenericScale.NAME)
public class GenericScaleIterableRT<T extends RealType<T>, V extends RealType<V>>
		extends AbstractFunction<Iterable<T>, Iterable<V>> implements
		GenericScale<Iterable<T>, Iterable<V>> {

	@Parameter
	private OpService ops;

	@Parameter
	private double oldMin;

	@Parameter
	private double oldMax;

	@Parameter
	private double newMin;

	@Parameter
	private double newMax;

	@Override
	public Iterable<V> compute(final Iterable<T> input, final Iterable<V> output) {

		ops.run(Map.class, output, input, new ScalePixelRT(oldMin, oldMax,
				newMin, newMax));

		return output;
	}

	// Internal helper function
	private class ScalePixelRT extends AbstractFunction<T, V> {

		private double factor;
		private double newMin;
		private double oldMin;

		public ScalePixelRT(final double oldMin, final double oldMax,
				final double newMin, final double newMax) {
			this.oldMin = oldMin;
			this.newMin = newMin;
			this.factor = calculateFactor(oldMin, oldMax, newMin, newMax);
		}

		@Override
		public V compute(final T input, final V output) {
			output.setReal(((input.getRealDouble() - oldMin) * factor) + newMin);
			return output;
		}

		private double calculateFactor(double oldMin, double oldMax,
				double newMin, double newMax) {
			return 1.0d / (oldMax - oldMin) * ((newMax - newMin));
		}

	}
}
