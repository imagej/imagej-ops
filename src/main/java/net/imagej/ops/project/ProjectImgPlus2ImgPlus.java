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
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.meta.AxisType;
import net.imglib2.meta.CalibratedAxis;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implementation of a {@link Project} for {@link ImgPlus} on {@link ImgPlus}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 * @param <V>
 */
@Plugin(type = Op.class, name = Project.NAME, priority = Priority.LOW_PRIORITY + 1, menuPath = "Image -> Project")
public class ProjectImgPlus2ImgPlus<T, V extends NativeType<V>> extends
		AbstractOutputFunction<ImgPlus<T>, ImgPlus<V>> implements
		Project<ImgPlus<T>, ImgPlus<V>> {

	@Parameter
	private OpService ops;

	@Parameter
	private ProjectMethod<T, V> method;

	@Parameter
	private AxisType axisType;

	@Parameter(required = false)
	private NativeType<V> outType;

	@SuppressWarnings("unchecked")
	@Override
	public ImgPlus<V> createOutput(final ImgPlus<T> input) {
		final long[] dims = new long[input.numDimensions() - 1];
		final CalibratedAxis[] axes = new CalibratedAxis[dims.length];

		int dim = input.dimensionIndex(axisType);

		int k = 0;
		for (int d = 0; d < dims.length; d++) {
			if (d != dim) {
				axes[k] = input.axis(d);
				dims[k++] = input.dimension(d);
			}
		}

		if (outType != null)
			return (ImgPlus<V>) new ImgPlus<V>((Img<V>) ops.createImg(
					input.factory(), outType, dims), input.getName(), axes);
		else
			return (ImgPlus<V>) new ImgPlus<V>((Img<V>) ops.createImg(
					input.factory(), input.firstElement(), dims),
					input.getName(), axes);
	}

	@Override
	protected ImgPlus<V> safeCompute(final ImgPlus<T> input,
			final ImgPlus<V> output) {

		ops.run(Project.class, output, input, method, input
				.dimensionIndex(axisType), output.firstElement()
				.createVariable());

		return output;
	}
}
