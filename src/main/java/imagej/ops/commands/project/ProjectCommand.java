/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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

package imagej.ops.commands.project;

import imagej.command.Command;
import imagej.ops.AbstractFunction;
import imagej.ops.OpService;
import imagej.ops.descriptors.statistics.Mean;
import imagej.ops.project.Project;
import net.imglib2.img.Img;
import net.imglib2.meta.ImgPlus;
import net.imglib2.meta.TypedAxis;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Image > Threshold > Project")
public class ProjectCommand<T extends RealType<T>> implements Command {

	@Parameter(type = ItemIO.BOTH)
	private ImgPlus<T> out;

	@Parameter
	private ImgPlus<T> in;

	// TODO: same problem as in the threshold: parameter aggregation ...
	@Parameter
	private ProjectMethod<T> method;

	// the dimension that will be aggregated
	@Parameter
	private TypedAxis axis;

	@Parameter
	private OpService ops;

	@Override
	public void run() {
		if (out == null) {
			final Img<T> img = in.factory().create(in, in.firstElement().createVariable());
			out = new ImgPlus<T>(img, in);
		}
		final int axisIndex = in.dimensionIndex(axis.type());
		ops.run(Project.class, out, in, method, axisIndex);
	}

	/* -- Wrapper classes to mark certain operations as projection methods --*/

	private class ProjectMean extends AbstractFunction<Iterable<T>, T> implements
		ProjectMethod<T>
	{

		private Mean<Iterable<T>, T> mean;

		@Override
		public T compute(final Iterable<T> input, final T output) {
			if (mean == null) {
				mean = (Mean<Iterable<T>, T>) ops.op(Mean.class, output, input);
			}
			return mean.compute(input, output);
		}

	}

}
