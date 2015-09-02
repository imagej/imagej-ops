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
import net.imagej.ops.Ops;
import net.imglib2.img.Img;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.IntegerType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

/**
 * Copying {@link ImgLabeling} into another {@link ImgLabeling}
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 */
@Plugin(type = Ops.Copy.ImgLabeling.class, name = Ops.Copy.ImgLabeling.NAME)
public class CopyImgLabeling<T extends IntegerType<T> & NativeType<T>, L>
	extends AbstractHybridOp<ImgLabeling<L, T>, ImgLabeling<L, T>>implements
	Ops.Copy.ImgLabeling, Contingent
{

	@Parameter
	private OpService ops;

	@Override
	public ImgLabeling<L, T> createOutput(final ImgLabeling<L, T> input) {
		return ops.create().imgLabeling(input);
	}

	@Override
	public void compute(final ImgLabeling<L, T> input,
		final ImgLabeling<L, T> output)
	{
		ops.copy().img((Img<T>) output.getIndexImg(), (Img<T>) input.getIndexImg());
		ops.copy().labelingMapping(output.getMapping(), input.getMapping());

	}

	@Override
	public boolean conforms() {
		if (getOutput() != null) {
			return Intervals.equalDimensions(getInput(), getOutput()) && Util
				.getTypeFromInterval(getInput().getIndexImg()).getClass() == Util
					.getTypeFromInterval(getOutput().getIndexImg()).getClass();
		}

		return true;
	}

}
