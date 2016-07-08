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

package net.imagej.ops.morphology;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Before;
import org.junit.Test;

import ij.io.Opener;

public class MorphologyOpsTest extends AbstractOpTest {

	private Img<BitType> imgWithoutHoles;
	private Img<BitType> imgWithHoles;

	private boolean initialized = false;

	@Before
	public void loadImages() {
		if (initialized) {
			return;
		}

		// create two bittypes images
		Img<FloatType> inputWithoutHoles = ImageJFunctions.convertFloat(new Opener()
			.openImage(MorphologyOpsTest.class.getResource("img_without_holes.png")
				.getPath()));
		Img<FloatType> inputWithHoles = ImageJFunctions.convertFloat(new Opener()
			.openImage(MorphologyOpsTest.class.getResource("img_with_holes.png")
				.getPath()));

		Cursor<FloatType> inputWithoutHolesCursor = inputWithoutHoles.cursor();
		Cursor<FloatType> inputWithHolesCursor = inputWithHoles.cursor();

		imgWithoutHoles = ops.create().img(inputWithoutHoles, new BitType());
		imgWithHoles = ops.create().img(inputWithHoles, new BitType());

		Cursor<BitType> imgWithoutHolesCursor = imgWithoutHoles.cursor();
		Cursor<BitType> imgWithHolesCursor = imgWithHoles.cursor();

		while (inputWithoutHolesCursor.hasNext()) {
			imgWithoutHolesCursor.next().set((inputWithoutHolesCursor.next()
				.get() > 0) ? false : true);
		}

		while (inputWithHolesCursor.hasNext()) {
			imgWithHolesCursor.next().set((inputWithHolesCursor.next().get() > 0)
				? false : true);
		}

		initialized = true;
	}

	@Test
	public void testExtractHoles() throws IOException {
		assertNotNull("Img Without Holes", ops.morphology().extractHoles(
			imgWithoutHoles, StructuringElement.FOUR_CONNECTED, true));
		assertNotNull("Img With Holes", ops.morphology().extractHoles(imgWithHoles,
			StructuringElement.FOUR_CONNECTED, false));
	}
}
