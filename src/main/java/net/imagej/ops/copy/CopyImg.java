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

package net.imagej.ops.copy;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Copy;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.util.Intervals;

/**
 * Copying {@link Img} into another {@link Img}
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 */
@Plugin(type = Copy.Img.class, name = Copy.Img.NAME)
public class CopyImg<T extends NativeType<T>> extends
	AbstractHybridOp<Img<T>, Img<T>>implements Copy.Img, Contingent
{

	@Parameter
	private OpService ops;

	@Override
	public Img<T> createOutput(final Img<T> input) {
		return ops.create().img(input, input.firstElement().createVariable(), input
			.factory());
	}

	@Override
	public void compute(final Img<T> input, final Img<T> output) {
		ops.copy().iterableInterval(output, input);
	}

	@Override
	public boolean conforms() {
		if (getOutput() != null) {
			return Intervals.equalDimensions(getInput(), getOutput());
		}
		return true;
	}
}
