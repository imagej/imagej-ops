/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.create;

import net.imagej.ops.AbstractOp;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract class for creating an image from a factory and a type. Derived
 * classes will contain logic to determine size and values of the image.
 *
 * @author Brian Northan
 * @param <V>
 */
public abstract class AbstractCreateKernelImg<V extends NativeType<V>> extends
	AbstractOp
{

	@Parameter(type = ItemIO.OUTPUT)
	private Img<V> output;

	@Parameter(required = false)
	private V outType;

	@Parameter(required = false)
	private ImgFactory<V> fac;

	@SuppressWarnings("unchecked")
	protected void createOutputImg(final long[] dims) {
		final Interval interval = new FinalInterval(dims);

		// no factory and no type
		if (fac == null && outType == null) {
			final Img<DoubleType> img = ops().create().img(interval);
			// FIXME: This cast is wrong.
			output = (Img<V>) img;
		}
		// type but no factory
		else if (fac == null && outType != null) {
			output = ops().create().img(interval, outType);
		}
		// factory but no type
		else if (fac != null && outType == null) {
			final DoubleType dType = new DoubleType();
			try {
				final ImgFactory<DoubleType> dFactory = fac.imgFactory(dType);
				final Img<DoubleType> img = ops().create().img(interval, dType,
					dFactory);
				// FIXME: This cast is wrong.
				output = (Img<V>) img;
			}
			catch (final IncompatibleTypeException exc) {
				throw new IllegalArgumentException("Factory " + fac.getClass()
					.getName() + " does not support " + dType.getClass().getName(), exc);
			}
		}
		else {
			output = ops().create().img(interval, outType, fac);
		}

	}

	protected Img<V> getOutput() {
		return output;
	}

	protected V getOutType() {
		return outType;
	}

}
