/*-
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

package net.imagej.ops.coloc;

import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

import org.junit.Test;

public class ShuffledViewTest extends ColocalisationTest {

	// for(UnsignedByteType t:Views.iterable(shuffled)) System.out.print(t + ",
	// "); //for printing off shuffled values

	/*
	 *  Is input same as output?  Should be NO.
	 */
	@Test
	public void testShuffleView() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> actualInputImage = ArrayImgs
			.unsignedBytes(new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
		}, 6, 6);
		int[] blockSize = { 2, 2 };
		long seed = 0xdeadbeef;
		ShuffledView<UnsignedByteType> shuffled = new ShuffledView<>(
			actualInputImage, blockSize, seed);
		ArrayImg<UnsignedByteType, ByteArray> expected = ArrayImgs.unsignedBytes(
			new byte[] { //
				27, 28, 3, 4, 15, 16, //
				33, 34, 9, 10, 21, 22, //
				5, 6, 17, 18, 13, 14, //
				11, 12, 23, 24, 19, 20, //
				1, 2, 29, 30, 25, 26, //
				7, 8, 35, 36, 31, 32 //
			}, 6, 6);
		assertIterationsEqual(expected, Views.iterable(shuffled));
	}

	/*
	 *  If I run it multiple times with the same seed... should be the same.
	 */
	@Test
	public void testSameSeed() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> inputImage = ArrayImgs.unsignedBytes(
			new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
			}, 6, 6);
		int[] blockSize = { 2, 2 };
		long seed = 0xdeadbeef;
		ShuffledView<UnsignedByteType> shuffled01 = new ShuffledView<>(inputImage,
			blockSize, seed);
		ShuffledView<UnsignedByteType> shuffled02 = new ShuffledView<>(inputImage,
			blockSize, seed);
		assertIterationsEqual(Views.iterable(shuffled01), Views.iterable(
			shuffled02));
	}

	/*
	 *  If I change seed... should be different outputs
	 */
	@Test
	public <T extends RealType<T>, U extends RealType<U>> void testDiffSeeds() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> inputImage = ArrayImgs.unsignedBytes(
			new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
			}, 6, 6);
		int[] blockSize = { 2, 2 };
		long seed1 = 0xdeadbeef;
		long seed2 = 0x22334455;
		ShuffledView<UnsignedByteType> shuffled1 = new ShuffledView<>(inputImage,
			blockSize, seed1);
		ArrayImg<UnsignedByteType, ByteArray> expected1 = ArrayImgs.unsignedBytes(
			new byte[] { //
				27, 28, 3, 4, 15, 16, //
				33, 34, 9, 10, 21, 22, //
				5, 6, 17, 18, 13, 14, //
				11, 12, 23, 24, 19, 20, //
				1, 2, 29, 30, 25, 26, //
				7, 8, 35, 36, 31, 32 //
			}, 6, 6);
		ShuffledView<UnsignedByteType> shuffled2 = new ShuffledView<>(inputImage,
			blockSize, seed2);
		ArrayImg<UnsignedByteType, ByteArray> expected2 = ArrayImgs.unsignedBytes(
			new byte[] { //
				29, 30, 25, 26, 17, 18, //
				35, 36, 31, 32, 23, 24, //
				5, 6, 27, 28, 15, 16, //
				11, 12, 33, 34, 21, 22, //
				3, 4, 13, 14, 1, 2, //
				9, 10, 19, 20, 7, 8 //
			}, 6, 6);
		assertIterationsEqual(expected1, Views.iterable(shuffled1));
		assertIterationsEqual(expected2, Views.iterable(shuffled2));
	}

	/*
	 *  Test non-square blocks {2,3}.
	 */
	@Test
	public void testNonSquareBlocks1() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> actualInputImage = ArrayImgs
			.unsignedBytes(new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
		}, 6, 6);
		int[] blockSize = { 2, 3 };
		long seed = 0xdeadbeef;
		ShuffledView<UnsignedByteType> shuffled = new ShuffledView<>(
			actualInputImage, blockSize, seed);
		ArrayImg<UnsignedByteType, ByteArray> expected = ArrayImgs.unsignedBytes(
			new byte[] { //
				21, 22, 5, 6, 1, 2, //
				27, 28, 11, 12, 7, 8, //
				33, 34, 17, 18, 13, 14, //
				23, 24, 3, 4, 19, 20, //
				29, 30, 9, 10, 25, 26, //
				35, 36, 15, 16, 31, 32 //
			}, 6, 6);
		assertIterationsEqual(expected, Views.iterable(shuffled));
	}

	/*
	 *  Test non-square blocks {3,2}.
	 */
	@Test
	public void testNonSquareBlocks2() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> actualInputImage = ArrayImgs
			.unsignedBytes(new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
		}, 6, 6);
		int[] blockSize = { 3, 2 };
		long seed = 0xdeadbeef;
		ShuffledView<UnsignedByteType> shuffled = new ShuffledView<>(
			actualInputImage, blockSize, seed);
		ArrayImg<UnsignedByteType, ByteArray> expected = ArrayImgs.unsignedBytes(
			new byte[] { //
				25, 26, 27, 13, 14, 15, //
				31, 32, 33, 19, 20, 21, //
				1, 2, 3, 28, 29, 30, //
				7, 8, 9, 34, 35, 36, //
				4, 5, 6, 16, 17, 18, //
				10, 11, 12, 22, 23, 24 //
			}, 6, 6);
		assertIterationsEqual(expected, Views.iterable(shuffled));
	}

	/*
	 *  Test block of {1,1}.
	 */
	@Test
	public void testAllShuffle() {
		// FIRST - create 6x6 image filled with known values
		ArrayImg<UnsignedByteType, ByteArray> actualInputImage = ArrayImgs
			.unsignedBytes(new byte[] { //
				1, 2, 3, 4, 5, 6, //
				7, 8, 9, 10, 11, 12, //
				13, 14, 15, 16, 17, 18, //
				19, 20, 21, 22, 23, 24, //
				25, 26, 27, 28, 29, 30, //
				31, 32, 33, 34, 35, 36 //
		}, 6, 6);
		int[] blockSize = { 1, 1 };
		long seed = 0xdeadbeef;
		ShuffledView<UnsignedByteType> shuffled = new ShuffledView<>(
			actualInputImage, blockSize, seed);
		ArrayImg<UnsignedByteType, ByteArray> expected = ArrayImgs.unsignedBytes(
			new byte[] { //
				33, 19, 14, 36, 31, 32, //
				34, 21, 17, 30, 35, 1, //
				7, 28, 29, 20, 9, 12, //
				5, 18, 27, 3, 8, 2, //
				11, 25, 4, 24, 26, 6, //
				23, 10, 13, 15, 22, 16 //
			}, 6, 6);
		assertIterationsEqual(expected, Views.iterable(shuffled));
	}
}
