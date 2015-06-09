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

package net.imagej.ops.create;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputOp;
import net.imagej.ops.create.CreateOps.CreateImg;
import net.imagej.ops.create.CreateOps.CreateImgFactory;
import net.imagej.ops.create.CreateOps.CreateNativeImg;
import net.imagej.ops.create.CreateOps.CreateNativeType;
import net.imglib2.Dimensions;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the {@link CreateImg} interface.
 *
 * @author Daniel Seebacher, University of Konstanz.
 * @author Tim-Oliver Buchholz, University of Konstanz.
 * @param <T>
 */
@Plugin(type = Op.class)
public class DefaultCreateImg<T extends NativeType<T>> implements
	CreateNativeImg, OutputOp<Img<T>>
{

	@Parameter
	private OpService ops;

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

		// FIXME this is not guaranteed to be a T unless Class<T> is passed in here..
		if (outType == null) {
			outType = (T) ops.run(CreateNativeType.class);
		}

		if (fac == null) {
			if ((dims instanceof Img)) {

				final Img<?> inImg = ((Img<?>) dims);
				if (inImg.firstElement().getClass()
					.isAssignableFrom(outType.getClass()))
				{
					fac = (ImgFactory<T>) inImg.factory();
				}
				else {
					try {
						fac = inImg.factory().imgFactory(outType);
					}
					catch (final IncompatibleTypeException e) {
						fac =
							(ImgFactory<T>) ops.run(CreateImgFactory.class, dims, outType);
					}
				}
			}
			else {
				fac = (ImgFactory<T>) ops.run(CreateImgFactory.class, dims, outType);
			}
		}

		output = fac.create(dims, outType);
	}

	@Override
	public Img<T> getOutput() {
		return output;
	}

	@Override
	public void setOutput(final Img<T> output) {
		this.output = output;
	}

}
