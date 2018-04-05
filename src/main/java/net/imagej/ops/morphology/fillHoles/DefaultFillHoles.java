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

package net.imagej.ops.morphology.fillHoles;

import net.imagej.ops.Ops;
import net.imagej.ops.create.img.CreateImgFromDimsAndType;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Fills the holes of a BooleanType image.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Morphology.FillHoles.class)
public class DefaultFillHoles<T extends BooleanType<T>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Morphology.FillHoles
{

	@Parameter(required = false)
	private Shape structElement = new RectangleShape(1, false);

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createFunc;
	private BinaryComputerOp<RandomAccessibleInterval<T>, Localizable, RandomAccessibleInterval<T>> floodFillComp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		createFunc = RAIs.function(ops(), CreateImgFromDimsAndType.class, in(), new BitType());
		floodFillComp = (BinaryComputerOp) Computers.binary(ops(),
			Ops.Morphology.FloodFill.class, RandomAccessibleInterval.class, in(),
			Localizable.class, structElement);
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> op,
		final RandomAccessibleInterval<T> r)
	{
		final IterableInterval<T> iterOp = Views.flatIterable(op);
		final IterableInterval<T> iterR = Views.flatIterable(r);

		long[] dim = new long[r.numDimensions()];
		r.dimensions(dim);
		Cursor<T> rc = iterR.cursor();
		Cursor<T> opc = iterOp.localizingCursor();
		// Fill with non background marker
		while (rc.hasNext()) {
			rc.next().setOne();
		}

		rc.reset();
		boolean border;
		// Flood fill from every background border voxel
		while (rc.hasNext()) {
			rc.next();
			opc.next();
			if (rc.get().get() && !opc.get().get()) {
				border = false;
				for (int i = 0; i < r.numDimensions(); i++) {
					if (rc.getLongPosition(i) == 0 || rc.getLongPosition(i) == dim[i] -
						1)
					{
						border = true;
						break;
					}
				}
				if (border) {
					floodFillComp.compute(op, rc, r);
				}
			}
		}
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return createFunc.calculate(input);
	}

}
