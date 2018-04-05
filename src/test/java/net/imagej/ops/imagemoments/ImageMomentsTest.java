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

package net.imagej.ops.imagemoments;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.ImageMoments}.
 * 
 * @author Daniel Seebacher
 */
public class ImageMomentsTest extends AbstractOpTest {

	private static final double EPSILON = 1e-8;
	private static Img<UnsignedByteType> img;

	@BeforeClass
	public static void createImg() {

		Img<UnsignedByteType> tmp =
			ArrayImgs.unsignedBytes(new long[] { 100, 100 });

		Random rand = new Random(1234567890L);
		final Cursor<UnsignedByteType> cursor = tmp.cursor();
		while (cursor.hasNext()) {
			cursor.next().set(rand.nextInt((int) tmp.firstElement().getMaxValue()));
		}

		img = tmp;
	}

	/**
	 * Test the Moment Ops.
	 */
	@Test
	public void testMoments() {

		assertEquals(Ops.ImageMoments.Moment00.NAME, 1277534.0, ops.imagemoments().moment00(img)
			.getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.Moment10.NAME, 6.3018047E7, ops.imagemoments().moment10(img)
			.getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.Moment01.NAME, 6.3535172E7, ops.imagemoments().moment01(img)
			.getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.Moment11.NAME, 3.12877962E9, ops.imagemoments().moment11(img)
			.getRealDouble(), EPSILON);
	}

	/**
	 * Test the Central Moment Ops.
	 */
	@Test
	public void testCentralMoments() {
		assertEquals(Ops.ImageMoments.CentralMoment11.NAME, -5275876.956702709, ops.imagemoments()
			.centralMoment11(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment02.NAME, 1.0694469880269902E9, ops.imagemoments()
			.centralMoment02(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment20.NAME, 1.0585772432642114E9, ops.imagemoments()
			.centralMoment20(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment12.NAME, 5478324.271281097, ops.imagemoments()
			.centralMoment12(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment21.NAME, -2.1636455685489437E8, ops
			.imagemoments().centralMoment21(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment30.NAME, 1.7355602329912126E8, ops.imagemoments()
			.centralMoment30(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.CentralMoment03.NAME, -4.099421316116555E8, ops
			.imagemoments().centralMoment03(img).getRealDouble(), EPSILON);
	}

	/**
	 * Test the Normalized Central Moment Ops.
	 */
	@Test
	public void testNormalizedCentralMoments() {
		assertEquals(Ops.ImageMoments.NormalizedCentralMoment11.NAME, -3.2325832933879204E-6, ops
			.imagemoments().normalizedCentralMoment11(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment02.NAME, 6.552610106398286E-4, ops
			.imagemoments().normalizedCentralMoment02(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment20.NAME, 6.486010078361372E-4, ops
			.imagemoments().normalizedCentralMoment20(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment12.NAME, 2.969727272701925E-9, ops
			.imagemoments().normalizedCentralMoment12(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment21.NAME, -1.1728837022440002E-7, ops
			.imagemoments().normalizedCentralMoment21(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment30.NAME, 9.408242926327751E-8, ops
			.imagemoments().normalizedCentralMoment30(img).getRealDouble(), EPSILON);

		assertEquals(Ops.ImageMoments.NormalizedCentralMoment03.NAME, -2.22224218245127E-7, ops
			.imagemoments().normalizedCentralMoment03(img).getRealDouble(), EPSILON);
	}

	/**
	 * Test the Hu Moment Ops.
	 */
	@Test
	public void testHuMoments() {
		assertEquals(Ops.ImageMoments.HuMoment1.NAME, 0.001303862018475966, ops.imagemoments()
			.huMoment1(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment2.NAME, 8.615401633994056e-11, ops.imagemoments()
			.huMoment2(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment3.NAME, 2.406124306990366e-14, ops.imagemoments()
			.huMoment3(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment4.NAME, 1.246879188175627e-13, ops.imagemoments()
			.huMoment4(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment5.NAME, -6.610443880647384e-27, ops.imagemoments()
			.huMoment5(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment6.NAME, 1.131019166855569e-18, ops.imagemoments()
			.huMoment6(img).getRealDouble(), EPSILON);
		assertEquals(Ops.ImageMoments.HuMoment7.NAME, 1.716256940536518e-27, ops.imagemoments()
			.huMoment7(img).getRealDouble(), EPSILON);
	}

}
