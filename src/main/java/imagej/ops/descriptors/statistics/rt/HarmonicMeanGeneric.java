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


package imagej.ops.descriptors.statistics.rt;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.misc.Area;
import imagej.ops.descriptors.statistics.HarmonicMean;
import imagej.ops.descriptors.statistics.SumOfInverses;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HarmonicMean}. Use {@link DescriptorService}
 * to compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, label = HarmonicMean.LABEL, name = HarmonicMean.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class HarmonicMeanGeneric extends AbstractFunction<Object, RealType<?>>
	implements HarmonicMean<Object, RealType<?>>
{

	@Parameter
	private SumOfInverses<Object, DoubleType> inverseSum;

	@Parameter
	private Area<Object, DoubleType> area;

	@Override
	public RealType<?> compute(final Object input, RealType<?> output) {
		if (output == null) {
			output = new DoubleType();
			setOutput(output);
		}

		output.setReal(area.getOutput().get() / inverseSum.getOutput().get());
		return output;
	}
}
