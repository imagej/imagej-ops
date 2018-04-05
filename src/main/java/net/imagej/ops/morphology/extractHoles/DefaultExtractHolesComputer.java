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

package net.imagej.ops.morphology.extractHoles;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.BooleanType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Extracts the holes from a binary image.
 *
 * @author Christian Dietz (University of Konstanz)
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Morphology.ExtractHoles.class)
public class DefaultExtractHolesComputer<T extends BooleanType<T>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Morphology.ExtractHoles
{

	@Parameter(required=false)
	private Shape structElement = new RectangleShape(1, false);

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> xorMapOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> fillHolesComp;

	@Override
	public void initialize() {
		fillHolesComp = RAIs.computer(ops(), Ops.Morphology.FillHoles.class, in(), structElement);
		xorMapOp = RAIs.computer(ops(), Ops.Map.class, in(),
			new AbstractUnaryComputerOp<T, T>()
		{

				@Override
				public void compute(T input, T output) {
					output.set((input.get()) ^ (output
						.get()));
				}
			});

	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		fillHolesComp.compute(input, output);
		xorMapOp.compute(input, output);
	}

}
