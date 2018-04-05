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

package net.imagej.ops.create.img;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.UFViaUFSameIO;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;
import net.imglib2.util.Util;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Create an {@link Img} from a {@link RandomAccessibleInterval} using its type
 * {@code T}.
 *
 * @author Curtis Rueden
 * @param <T>
 */
@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH)
public class CreateImgFromRAI<T extends NativeType<T>> extends
	UFViaUFSameIO<RandomAccessibleInterval<T>, Img<T>> implements
	Ops.Create.Img
{

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UnaryFunctionOp<RandomAccessibleInterval<T>, Img<T>> createWorker(
		final RandomAccessibleInterval<T> input)
	{
		// NB: Intended to match CreateImgFromDimsAndType.
		return (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, input, Util.getTypeFromInterval(input));
	}

}
