/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Interval;
import net.imglib2.img.Img;
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
@Plugin(type = Ops.Create.ImgLabeling.class, priority = Priority.HIGH)
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

	private UnaryFunctionOp<Interval, Img<T>> imgCreator;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		if (outType == null) {
			outType = (T) ops().create().integerType(maxNumLabelSets);
		}
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), outType, fac);
	}

	@Override
	public ImgLabeling<L, T> calculate(final Interval input) {
		return new ImgLabeling<>(imgCreator.calculate(input));
	}

}
