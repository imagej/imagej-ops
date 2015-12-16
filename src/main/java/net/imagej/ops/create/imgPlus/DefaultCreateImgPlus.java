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

package net.imagej.ops.create.imgPlus;

import net.imagej.ImgPlus;
import net.imagej.ImgPlusMetadata;
import net.imagej.ops.AbstractOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Output;
import net.imglib2.img.Img;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the "create.imgPlus" op.
 *
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.ImgPlus.class)
public class DefaultCreateImgPlus<T> extends AbstractOp implements
	Ops.Create.ImgPlus, Output<ImgPlus<T>>, Contingent
{

	@Parameter(type = ItemIO.OUTPUT)
	private ImgPlus<T> output;

	@Parameter
	private Img<T> img;

	@Parameter(required = false)
	private ImgPlusMetadata metadata;

	@Override
	public void run() {

		if (metadata != null) {
			output = new ImgPlus<T>(img, metadata);
		}
		else {
			output = new ImgPlus<T>(img);
		}

	}

	@Override
	public boolean conforms() {
		return metadata == null || metadata.numDimensions() == img.numDimensions();
	}

	@Override
	public ImgPlus<T> out() {
		return output;
	}

}
