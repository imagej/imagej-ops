/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.image.scale;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.cell.CellImg;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * @author Martin Horn (University of Konstanz)
 */
public class ScaleImgTest extends AbstractOpTest {

	@Test
	public void test() {
		Img<ByteType> in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		double[] scaleFactors = new double[] { 2, 2 };
		@SuppressWarnings("unchecked")
		Img<ByteType> out = (Img<ByteType>) ops.run(ScaleImg.class, in,
			scaleFactors, new NLinearInterpolatorFactory<ByteType>());

		assertEquals(out.dimension(0), 20);
		assertEquals(out.dimension(1), 20);

		RandomAccess<ByteType> inRA = in.randomAccess();
		RandomAccess<ByteType> outRA = out.randomAccess();
		inRA.setPosition(new long[] { 5, 5 });
		outRA.setPosition(new long[] { 10, 10 });
		assertEquals(inRA.get().get(), outRA.get().get());

	}

	/**
	 * A small {@link CellImg} is given, and a different type of image is expected
	 * as output.
	 */
	@Test
	public void testDiffImgTypes() {
		Img<ByteType> in = new CellImgFactory<ByteType>(5).create(
			new FinalDimensions(10, 10), new ByteType());
		RandomAccess<ByteType> inRA = in.randomAccess();
		inRA.setPosition(new long[] { 5, 5 });
		inRA.get().set((byte) 10);
		
		double[] scaleFactors = new double[] { 2, 2 };
		Img<ByteType> out = ops.image().scale(in, scaleFactors,
			new NLinearInterpolatorFactory<ByteType>());

		assertEquals(out.dimension(0), 20);
		assertEquals(out.dimension(1), 20);
		
		assertEquals(out instanceof CellImg, false);
		
		RandomAccess<ByteType> outRA = out.randomAccess();
		outRA.setPosition(new long[] { 10, 10 });
		assertEquals(outRA.get().get(), 10);
	}

}
