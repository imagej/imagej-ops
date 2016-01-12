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

package net.imagej.ops.create.imgLabeling;

import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imglib2.Interval;
import net.imglib2.img.ImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.numeric.IntegerType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates an {@link ImgLabeling} from an {@link Interval}. Minimum and maximum
 * of resulting {@link ImgLabeling} are the same as the minimum and maximum of
 * the incoming {@link Interval}.
 *
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Create.ImgLabeling.class, priority = Priority.HIGH_PRIORITY)
public class CreateImgLabelingFromInterval<L, T extends IntegerType<T>> extends
	AbstractUnaryFunctionOp<Interval, ImgLabeling<L, T>> implements
	Ops.Create.ImgLabeling
{

	@Parameter(required = false)
	private T outType;

	@Parameter(required = false)
	private ImgFactory<T> fac;

	@Parameter(required = false)
	private int maxNumLabelSets;

	@SuppressWarnings("unchecked")
	@Override
	public ImgLabeling<L, T> compute1(final Interval input) {

		if (outType == null) {
			outType = (T) ops().create().integerType(maxNumLabelSets);
		}

		return new ImgLabeling<>(ops().create().img(input, outType, fac));
	}

}
