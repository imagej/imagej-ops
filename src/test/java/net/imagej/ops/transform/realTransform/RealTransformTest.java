/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
   this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.transform.realTransform;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

import org.junit.Test;

public class RealTransformTest extends AbstractOpTest {

	@Test
	public void regressionTest() throws Exception {

		final Img<UnsignedByteType> image = openUnsignedByteType(getClass(),
			"lowresbridge.tif");
		final Img<UnsignedByteType> expectedOutput = openUnsignedByteType(
			getClass(), "rotatedscaledcenter.tif");

		final AffineTransform2D transform = new AffineTransform2D();

		transform.translate(-image.dimension(0) / 2, -image.dimension(0) / 2);
		transform.rotate(1);
		transform.scale(0.5);
		transform.translate(image.dimension(0) / 2, image.dimension(0) / 2);

		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<UnsignedByteType> actualOutput =
			(RandomAccessibleInterval<UnsignedByteType>) ops.run(
				net.imagej.ops.transform.realTransform.DefaultTransformView.class,
				image, transform);

		// compare the output image data to that stored in the file.
		final Cursor<UnsignedByteType> cursor = Views.iterable(actualOutput)
			.localizingCursor();
		final RandomAccess<UnsignedByteType> actualRA = actualOutput.randomAccess();
		final RandomAccess<UnsignedByteType> expectedRA = expectedOutput
			.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			actualRA.setPosition(cursor);
			expectedRA.setPosition(cursor);
			assertEquals(expectedRA.get().get(), actualRA.get().get(), 0);
		}

	}

}
