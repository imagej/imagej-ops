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

package net.imagej.ops.convolve.kernel.create;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

/**
 * Abstract class for creating an image from a factory and a type. Derived
 * classes will contain logic to determine size and values of the image.
 * 
 * @author bnorthan
 * @param <V>
 */
abstract public class AbstractCreateKernelImg<V extends Type<V>, W extends Type<W>, FAC extends ImgFactory<W>>
{

	@Parameter(type = ItemIO.OUTPUT)
	protected Img<V> output;

	@Parameter(required = false)
	protected Type<V> outType;

	@Parameter(required = false)
	protected ImgFactory<V> fac;

	protected void createOutputImg(long[] dims, ImgFactory<V> fac,
		Type<V> outType, FAC defaultFactory, W defaultType)
	{

		// no factory and no type
		if ((fac == null) && (outType == null)) {
			@SuppressWarnings("unchecked")
			Img<V> temp = (Img<V>) defaultFactory.create(dims, defaultType);

			output = temp;
		}
		// type but no factory
		else if ((fac == null) && (outType != null)) {

			try {
				Img<V> temp =
					defaultFactory.imgFactory(outType.createVariable()).create(dims,
						outType.createVariable());

				output = temp;
			}
			catch (IncompatibleTypeException ex) {

			}
		}
		// factory but no type
		else if ((fac != null) && (outType == null)) {
			try {
				@SuppressWarnings("unchecked")
				Img<V> temp =
					(Img<V>) fac.imgFactory(defaultType.createVariable()).create(dims,
						defaultType.createVariable());

				output = temp;
			}
			catch (IncompatibleTypeException ex) {

			}

		}
		// type and a factory passed in
		else {
			output = fac.create(dims, outType.createVariable());
		}

	}

	protected void createOutputImg(long[] dims, Type<V> outType,
		FAC defaultFactory, W defaultType)
	{
		createOutputImg(dims, null, outType, defaultFactory, defaultType);
	}

	protected void createOutputImg(long[] dims, ImgFactory<V> fac,
		FAC defaultFactory, W defaultType)
	{
		createOutputImg(dims, fac, null, defaultFactory, defaultType);
	}

}