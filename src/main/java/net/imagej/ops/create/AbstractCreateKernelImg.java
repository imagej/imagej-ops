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
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract class for creating an image from a factory and a type. Derived
 * classes will contain logic to determine size and values of the image.
 *
 * @author Brian Northan
 * @param <V>
 */
public abstract class AbstractCreateKernelImg<V extends Type<V>, W extends Type<W>, FAC extends ImgFactory<W>>
	extends AbstractOp
{

	@Parameter(type = ItemIO.OUTPUT)
	private Img<V> output;

	@Parameter(required = false)
	private Type<V> outType;

	@Parameter(required = false)
	private ImgFactory<V> fac;

	@SuppressWarnings("unchecked")
	protected void createOutputImg(final long[] dims, final ImgFactory<V> fac,
		final Type<V> outType, final FAC defaultFactory, final W defaultType)
	{

		// no factory and no type
		if ((fac == null) && (outType == null)) {
			output = (Img<V>) ops().create().img(dims, defaultType, defaultFactory);
		}
		// type but no factory
		else if ((fac == null) && (outType != null)) {
			output = (Img<V>) ops().create().img(dims, outType, defaultFactory);
		}
		// factory but no type
		else if ((fac != null) && (outType == null)) {
			output = (Img<V>) ops().create().img(dims, defaultType, fac);
		}
		else {
			output = (Img<V>) ops().create().img(dims, outType, fac);
		}

	}

	protected void createOutputImg(final long[] dims, final Type<V> outType,
		final FAC defaultFactory, final W defaultType)
	{
		createOutputImg(dims, null, outType, defaultFactory, defaultType);
	}

	protected void createOutputImg(final long[] dims, final ImgFactory<V> fac,
		final FAC defaultFactory, final W defaultType)
	{
		createOutputImg(dims, fac, null, defaultFactory, defaultType);
	}

	protected Img<V> getOutput() {
		return output;
	}

	protected Type<V> getOutType() {
		return outType;
	}

	protected ImgFactory<V> getFac() {
		return fac;
	}

}
