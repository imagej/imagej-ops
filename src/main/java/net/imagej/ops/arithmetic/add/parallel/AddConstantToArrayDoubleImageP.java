/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.arithmetic.add.parallel;

import net.imagej.ops.MathOps;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.chunker.Chunk;
import net.imagej.ops.chunker.Chunker;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Multi-threaded version of optimized add constant for {@link ArrayImg}s of type
 * {@link DoubleType}.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Op.class, name = MathOps.Add.NAME, priority = Priority.HIGH_PRIORITY + 11)
public class AddConstantToArrayDoubleImageP implements MathOps.Add {

	@Parameter
	private OpService opService;

	@Parameter(type = ItemIO.BOTH)
	private ArrayImg<DoubleType, DoubleArray> image;

	@Parameter
	private double value;

	@Override
	public void run() {
		final double[] data = image.update(null).getCurrentStorageArray();
		opService.run(Chunker.class, new Chunk() {

			@Override
			public void execute(final int startIndex, final int stepSize, final int numSteps)
			{
				if (stepSize != 1) {
					for (int i = startIndex, j = 0; j < numSteps; i = i + stepSize, j++) {
						data[i] += value;
					}
				}
				else {
					for (int i = startIndex; i < startIndex + numSteps; i++) {
						data[i] += value;
					}
				}
			}
		}, data.length);
	}
}
