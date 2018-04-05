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

package net.imagej.ops.morphology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Test;

public class MorphologyOpsTest extends AbstractOpTest {

	private Img<BitType> imgWithoutHoles;
	private Img<BitType> imgWithHoles;
	private Img<BitType> invertedImgWithFilledHoles;

	private boolean initialized = false;

	@Before
	public void loadImages() {
		if (initialized) {
			return;
		}

		// create two bittypes images
		Img<FloatType> inputWithoutHoles = openFloatImg("img_without_holes.png");
		Img<FloatType> inputWithHoles = openFloatImg("img_with_holes.png");
		Img<FloatType> invertedInputWithFilledHoles = openFloatImg("inverted_img_with_filled_holes.png");

		Cursor<FloatType> inputWithoutHolesCursor = inputWithoutHoles.cursor();
		Cursor<FloatType> inputWithHolesCursor = inputWithHoles.cursor();
		Cursor<FloatType> invertedInputWithFilledHolesCursor = invertedInputWithFilledHoles.cursor();

		imgWithoutHoles = ops.create().img(inputWithoutHoles, new BitType());
		imgWithHoles = ops.create().img(inputWithHoles, new BitType());
		invertedImgWithFilledHoles = ops.create().img(invertedInputWithFilledHoles, new BitType());

		Cursor<BitType> imgWithoutHolesCursor = imgWithoutHoles.cursor();
		Cursor<BitType> imgWithHolesCursor = imgWithHoles.cursor();
		Cursor<BitType> invertedImgWithFilledHolesCursor = invertedImgWithFilledHoles.cursor();

		while (inputWithoutHolesCursor.hasNext()) {
			imgWithoutHolesCursor.next().set((inputWithoutHolesCursor.next().get() > 0) ? true : false);
		}

		while (inputWithHolesCursor.hasNext()) {
			imgWithHolesCursor.next().set((inputWithHolesCursor.next().get() > 0) ? true : false);
		}

		while (invertedInputWithFilledHolesCursor.hasNext()) {
			invertedImgWithFilledHolesCursor.next()
					.set((invertedInputWithFilledHolesCursor.next().get() > 0) ? true : false);
		}

		initialized = true;
	}

	@Test
	public void testExtractHoles() {
		assertNotNull("Img Without Holes", ops.morphology().extractHoles(imgWithoutHoles, new DiamondShape(1)));
		assertNotNull("Img With Holes", ops.morphology().extractHoles(imgWithHoles, new DiamondShape(1)));
	}

	@Test
	public void testFillHoles() {
		Img<BitType> result = ops.create().img(imgWithHoles);
		ops.morphology().fillHoles(result, imgWithHoles, new DiamondShape(1));

		Cursor<BitType> resultC = result.cursor();
		final BitType one = new BitType(true);
		while (resultC.hasNext()) {
			assertEquals(one, resultC.next());
		}
	}

	@Test
	public void testFillHoles1() {
		Img<BitType> result = ops.create().img(invertedImgWithFilledHoles);
		Img<BitType> inverted = ops.create().img(invertedImgWithFilledHoles);
		ops.image().invert(inverted, imgWithHoles);
		ops.morphology().fillHoles(result, inverted, new DiamondShape(1));

		Cursor<BitType> resultC = result.localizingCursor();
		RandomAccess<BitType> groundTruthRA = invertedImgWithFilledHoles.randomAccess();

		while (resultC.hasNext()) {
			boolean r = resultC.next().get();
			groundTruthRA.setPosition(resultC);
			assertEquals(groundTruthRA.get().get(), r);
		}
	}

	@Test
	public void testFillHoles2() {
		RandomAccessibleInterval<BitType> result = ops.morphology().fillHoles(imgWithoutHoles);
		Cursor<BitType> groundTruthC = imgWithoutHoles.localizingCursor();
		RandomAccess<BitType> resultRA = result.randomAccess();

		while (groundTruthC.hasNext()) {
			boolean r = groundTruthC.next().get();
			resultRA.setPosition(groundTruthC);
			assertEquals(r, resultRA.get().get());
		}
	}
}
