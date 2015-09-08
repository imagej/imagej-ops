
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

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Output;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.Type;

/**
 * Creates an {@link Img} of a type from {@link Dimensions}. If no
 * {@link ImgFactory} is provided a new one is created.
 *
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.Img.class, name = Ops.Create.Img.NAME)
public class DefaultCreateImg<T extends Type<T>>
		implements
			Ops.Create.Img,
			Output<Img<T>> {

	@Parameter(type = ItemIO.OUTPUT)
	private Img<T> output;

	@Parameter(type = ItemIO.INPUT)
	private Dimensions dims;

	@Parameter(type = ItemIO.INPUT, required = false)
	private ImgFactory<T> factory;

	@Parameter(type = ItemIO.INPUT)
	private T type;

	@Parameter
	private OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		if (factory == null) {
			factory = (ImgFactory<T>) ops.create().imgFactory(dims, type);
		}
		output = factory.create(dims, type);
	}

	@Override
	public Img<T> getOutput() {
		return output;
	}

}
