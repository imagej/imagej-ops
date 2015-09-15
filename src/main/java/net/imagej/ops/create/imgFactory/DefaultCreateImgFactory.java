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

package net.imagej.ops.create.imgFactory;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imagej.ops.Output;
import net.imglib2.Dimensions;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.util.Intervals;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the "create.imgFactory" op.
 *
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.ImgFactory.class, name = Ops.Create.ImgFactory.NAME)
public class DefaultCreateImgFactory<T extends NativeType<T>> extends
	AbstractOp implements Ops.Create.ImgFactory, Output<ImgFactory<T>>
{

	@Parameter(type = ItemIO.OUTPUT)
	private ImgFactory<T> output;

	@Parameter(required = false)
	private Dimensions dims;

	@Override
	public void run() {
		output =
			(dims == null || Intervals.numElements(dims) <= Integer.MAX_VALUE)
				? new ArrayImgFactory<T>() : new CellImgFactory<T>();
	}

	@Override
	public ImgFactory<T> out() {
		return output;
	}

}
