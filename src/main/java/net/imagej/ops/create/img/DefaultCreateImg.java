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

package net.imagej.ops.create.img;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imagej.ops.special.Output;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.util.Util;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the "create.img" op.
 *
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.Img.class)
public class DefaultCreateImg<T> extends AbstractOp implements Ops.Create.Img,
	Output<Img<T>>
{

	@Parameter(type = ItemIO.OUTPUT)
	private Img<T> output;

	@Parameter
	private Dimensions dims;

	@Parameter(required = false)
	private T outType;

	@Parameter(required = false)
	private ImgFactory<T> fac;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		// FIXME: not guaranteed to be a T unless a Class<T> is given.
		if (outType == null) {
			if (dims instanceof IterableInterval) {
				outType = (T) ((IterableInterval<?>) dims).firstElement();
			}
			else if (dims instanceof RandomAccessibleInterval) {
				outType = (T) Util.getTypeFromInterval(
					(RandomAccessibleInterval<?>) dims);
			}
			else {
				// HACK: For Java 6 compiler.
				@SuppressWarnings("rawtypes")
				final NativeType o = ops().create().nativeType();
				final T result = (T) o;
				outType = result;
			}
		}

		if (fac == null) {
			if (dims instanceof Img) {
				final Img<?> inImg = ((Img<?>) dims);
				if (inImg.firstElement().getClass().isInstance(outType)) {
					fac = (ImgFactory<T>) inImg.factory();
				}
				else {
					try {
						// HACK: For Java 6 compiler.
						final Object o = inImg.factory().imgFactory(outType);
						final ImgFactory<T> result = (ImgFactory<T>) o;
						fac = result;
					}
					catch (final IncompatibleTypeException e) {
						// FIXME: outType may not be a NativeType, but imgFactory needs one.
						fac = (ImgFactory<T>) ops().create().imgFactory(dims, outType);
					}
				}
			}
			else {
				// FIXME: outType may not be a NativeType, but imgFactory needs one.
				fac = (ImgFactory<T>) ops().create().imgFactory(dims, outType);
			}
		}

		output = fac.create(dims, outType);
	}

	@Override
	public Img<T> out() {
		return output;
	}

}
