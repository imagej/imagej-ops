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
package net.imagej.ops.create;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Intervals;

import org.junit.Test;

/**
 * Tests several ways to create an image
 *
 * @author Daniel Seebacher, University of Konstanz.
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */

public class CreateLabelingTest<T extends NativeType<T>> extends AbstractOpTest {

	private static final int TEST_SIZE = 100;

	@Test
	public void testImageDimensions() {

		Random randomGenerator = new Random();

		for (int i = 0; i < TEST_SIZE; i++) {

			// between 2 and 5 dimensions
			long[] dim = new long[randomGenerator.nextInt(4) + 2];

			// between 2 and 10 pixels per dimensions
			for (int j = 0; j < dim.length; j++) {
				dim[j] = randomGenerator.nextInt(9) + 2;
			}

			// create imglabeling
			@SuppressWarnings("unchecked")
			ImgLabeling<String, ?> img = (ImgLabeling<String, ?>) ops
					.createlabeling(dim);

			assertArrayEquals("Labeling Dimensions:", dim,
					Intervals.dimensionsAsLongArray(img));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testImageFactory() {

		long[] dim = new long[] { 10, 10, 10 };

		assertEquals("Labeling Factory: ", ArrayImgFactory.class,
				((Img<?>) ((ImgLabeling<String, ?>) ops
						.createlabeling(dim, null,
								new ArrayImgFactory<IntType>())).getIndexImg())
						.factory().getClass());

		assertEquals("Labeling Factory: ", CellImgFactory.class,
				((Img<?>) ((ImgLabeling<String, ?>) ops.createlabeling(dim,
						null, new CellImgFactory<IntType>())).getIndexImg())
						.factory().getClass());

	}

	@Test
	public void testImageType() {

		assertEquals("Labeling Type", String.class, createLabelingWithType("1")
				.firstElement().toArray()[0].getClass());

		assertEquals("Labeling Type", Integer.class, createLabelingWithType(1)
				.firstElement().toArray()[0].getClass());

		assertEquals("Labeling Type", Double.class, createLabelingWithType(1d)
				.firstElement().toArray()[0].getClass());

		assertEquals("Labeling Type", Float.class, createLabelingWithType(1f)
				.firstElement().toArray()[0].getClass());
	}

	@SuppressWarnings("unchecked")
	private <I> ImgLabeling<I, ?> createLabelingWithType(I type) {

		ImgLabeling<I, ?> imgLabeling = ((ImgLabeling<I, ?>) ops
				.createlabeling(new long[] { 10, 10, 10 }));
		imgLabeling.cursor().next().add(type);
		return imgLabeling;
	}
}
