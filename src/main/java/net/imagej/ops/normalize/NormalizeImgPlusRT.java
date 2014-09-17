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

package net.imagej.ops.normalize;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.convert.ConvertNormalizeScale;
import net.imagej.ops.slicer.Slicewise;
import net.imglib2.img.Img;
import net.imglib2.meta.Axes;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link Normalize} an {@link Img} of type {@link RealType} such that minimum /
 * maximum values are equal to the minimum / maximum of the {@link RealType}
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 */
@Plugin(type = Op.class, name = Normalize.NAME, attrs = { @Attr(name = "aliases", value = Normalize.ALIASES) }, priority = Priority.HIGH_PRIORITY)
public class NormalizeImgPlusRT<T extends RealType<T>> extends
		AbstractOutputFunction<ImgPlus<T>, ImgPlus<T>> implements Normalize {

	@Parameter
	private OpService ops;

	@Parameter(required = false)
	private Axes[] axes;

	@Override
	public ImgPlus<T> createOutput(ImgPlus<T> input) {
		// TODO create an op which is smart enough (including copying of
		// metadata etc)
		return new ImgPlus<T>(input.factory().create(input,
				input.firstElement().createVariable()), input);
	}

	@Override
	protected ImgPlus<T> safeCompute(ImgPlus<T> input, ImgPlus<T> output) {

		ops.run(Slicewise.class,
				output,
				input,
				ops.op(ConvertNormalizeScale.class, output.getClass(),
						input.getClass()), getAxesIndices(input, axes));

		return output;
	}

	// TODO where to put this friend

	public synchronized static int[] getAxesIndices(ImgPlus<?> input,
			Axes[] axes) {
		if (axes == null)
			return null;

		int[] indices = new int[axes.length];

		outer: for (int i = 0; i < indices.length; i++) {
			for (int a = 0; a < input.numDimensions(); a++) {
				if (input.axis(a).equals(axes[i])) {
					indices[i] = a;
					continue outer;
				}
			}

			return null;
		}

		return indices;
	}
}
