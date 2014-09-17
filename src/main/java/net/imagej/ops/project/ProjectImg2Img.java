/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.project;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Function;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implementation of a {@link Project} for {@link Img} on {@link Img}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 * @param <V>
 */
@Plugin(type = Op.class, name = Project.NAME, priority = Priority.LOW_PRIORITY)
public class ProjectImg2Img<T, V extends NativeType<V>> extends
		AbstractOutputFunction<Img<T>, Img<V>> implements
		Project<Img<T>, Img<V>> {

	@Parameter
	private Function<Iterable<T>, V> method;

	@Parameter
	private int dim;

	@Parameter(required = false)
	private NativeType<V> outType;

	@Parameter
	private OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public Img<V> createOutput(final Img<T> input) {
		long[] dims = new long[input.numDimensions() - 1];

		int k = 0;
		for (int d = 0; d < dims.length; d++) {
			if (d != dim) {
				dims[k++] = input.dimension(d);
			}
		}

		if (outType != null)
			return (Img<V>) ops.createImg(input.factory(), outType, dims);
		else
			return (Img<V>) ops.createImg(input.factory(),
					input.firstElement(), dims);
	}

	@Override
	protected Img<V> safeCompute(final Img<T> input, final Img<V> output) {

		ops.run(ProjectRAI2II.class, output, input, method, dim);

		return output;
	}
}
