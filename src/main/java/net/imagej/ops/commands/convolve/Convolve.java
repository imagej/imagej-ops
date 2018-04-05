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

package net.imagej.ops.commands.convolve;

import net.imagej.ImgPlus;
import net.imagej.axis.Axis;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * @author Martin Horn (University of Konstanz)
 */
@Plugin(type = Command.class, menuPath = "Image > Convolve")
public class Convolve<I extends RealType<I>, K extends RealType<K>, O extends RealType<O>>
		implements Command {

	@Parameter
	private ImgPlus<I> in;

	// TODO: kernel creator command needed (to be able to create some
	// parametrized
	// pre-defined kernel, such as gauss, gabor (curved, circular), sobel,
	// prewitt, derivative of gaussian, laplacian of gaussian, roberts, ...)
	// NOTE: a list of kernels would also be conceivable
	@Parameter
	private ImgPlus<K> kernel;

	// TODO: maybe not necessary here and the kernels dimension labels determine
	// in what dimensions the convolution takes place
	@Parameter
	private Axis[] axes;

	// TODO: needs to be selected by the user, but is not a plugin -> probably
	// needs to be wrapped?
	@Parameter
	private OutOfBoundsFactory<I, ImgPlus<I>> outOfBounds = new OutOfBoundsMirrorFactory<>(
			Boundary.SINGLE);

	@Parameter
	private boolean asFloat;

	@Parameter(type = ItemIO.OUTPUT)
	private ImgPlus<O> out;

	@Parameter
	private OpService ops;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		// TODO: get the dimension indices from the image dependent on the
		// selected
		// axes -> OR: just map the kernel dimension labels to the image
		// dimension
		// labels
		final int[] axisIndices = new int[] { 0, 1 };

		// number of indicies must be conform with the dimensionality of axes
		if (axes.length != axisIndices.length) {
			throw new IllegalArgumentException(
					"The number of selected dimension doesn't conforms with the kernel size.");
		}

		if (asFloat) {
			try {
				out = (ImgPlus) in.factory().imgFactory(new FloatType())
						.create(in, new FloatType());
			} catch (final IncompatibleTypeException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			out = (ImgPlus<O>) in.factory().create(in,
					in.firstElement().createVariable());
		}

		final Op op = ops.op(Ops.Filter.Convolve.class, out, in, kernel);
		if (in.numDimensions() > kernel.numDimensions()) {
			if (op instanceof UnaryComputerOp) {
				// if the selected convolve op is a function and the kernel dimensions
				// doesn't match the input image dimensions, than we can still convolve
				// each slice individually
				ops.run(Ops.Slice.class, out, in, op, axisIndices);
			} else {
				throw new IllegalArgumentException(
						"The input image has more dimensions than the kernel!");
			}
		} else if (in.numDimensions() == kernel.numDimensions()) {
			// no 'slicing' necessary
			ops.run(op, out, in, kernel);
		}

	}
}
