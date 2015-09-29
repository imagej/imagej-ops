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

package net.imagej.ops.map.neighborhood;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.map.neighborhood.array.MapNeighborhoodNativeType;
import net.imagej.ops.map.neighborhood.array.MapNeighborhoodNativeTypeExtended;
import net.imagej.ops.map.neighborhood.array.MapNeighborhoodWithCenterNativeType;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Pair;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for {@link MapNeighborhood} and {@link MapNeighborhoodWithCenter}.
 *
 * @author Jonathan Hale (University of Konstanz)
 */
public class MapNeighborhoodTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 11, 10);
		out = generateByteTestImg(false, 11, 10);
	}

	/**
	 * Test if every neighborhood pixel of the image was really accessed during
	 * the map operation.
	 *
	 * @see MapNeighborhood
	 */
	@Test
	public void testMapNeighborhoodsAccess() {
		final Op mapOp =
			ops.op(MapNeighborhood.class, out, in, new CountNeighbors(),
				new RectangleShape(1, false));
		mapOp.run();

		for (final ByteType t : out) {
			assertEquals(9, t.get());
		}
	}

	@Test
	@Ignore("There is no way to throw an error for invalid typed computers at the moment.")
	public
		void testMapNeighoodsWrongArgs() {
		final Op mapOp =
			ops.op(MapNeighborhood.class, out, in, new Increment(),
				new RectangleShape(1, false));

		// ClassCastException will be thrown
		mapOp.run();
	}

	/**
	 * Test if every neighborhood pixel of the image was really accessed during
	 * the map operation.
	 *
	 * @see MapNeighborhoodWithCenter
	 */
	@Test
	public void testMapNeighborhoodsWithCenterAccess() {
		final Op mapOp =
			ops.op(MapNeighborhoodWithCenter.class, out, in,
				new CountNeighborsWithCenter(), new RectangleShape(1, false));
		mapOp.run();

		for (final ByteType t : out) {
			assertEquals(9, t.get());
		}

		for (final ByteType t : in) {
			assertEquals(9, t.get());
		}
	}

	/**
	 * Test if every neighborhood pixel of the 2D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeType
	 */
	@Test
	public void testMapNeighborhoodsArrayImage2D() {
		final Op functional =
			ops.op(MapNeighborhoodNativeType.class, out, in,
				new CountNeighborsWithAccess(), 1);
		functional.run();

		final byte[] expected =
			new byte[] { 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 9, 9, 9, 9, 9, 9, 9, 9,
				9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6,
				6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9,
				9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9,
				9, 9, 9, 9, 9, 9, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4 };

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", expected[index++], t.get());
		}
	}
	
	/**
	 * Test if every neighborhood pixel of the 2D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeTypeExtended
	 */
	@Test
	public void testMapNeighborhoodsArrayImageAlias2D() {
		in = generateByteTestImg(true, 7, 7);
		out = generateByteTestImg(false, 7, 7);

		final Op functional =
			ops.op(MapNeighborhoodNativeTypeExtended.class, out, in,
				new CountNeighbors(), new RectangleShape(1, false));
		functional.run();

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", 9, t.get());
			index++;
		}
	}

	/**
	 * Test if every neighborhood pixel of the 1D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeType
	 */
	@Test
	public void testMapNeighborhoodsArrayImage1D() {
		in = generateByteTestImg(true, 7);
		out = generateByteTestImg(false, 7);

		final Op functional =
			ops.op(MapNeighborhoodNativeType.class, out, in,
				new CountNeighborsWithAccess(), 1);
		functional.run();

		final byte[] expected = new byte[] { 2, 3, 3, 3, 3, 3, 2 };

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", expected[index++], t.get());
		}
	}

	/**
	 * Test if every neighborhood pixel of the 1D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeTypeExtended
	 */
	@Test
	public void testMapNeighborhoodsArrayImageAlias1D() {
		in = generateByteTestImg(true, 7);
		out = generateByteTestImg(false, 7);

		final Op functional =
			ops.op(MapNeighborhoodNativeTypeExtended.class, out, in,
				new CountNeighbors(), new RectangleShape(1, false));
		functional.run();

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", 3, t.get());
			index++;
		}
	}

	/**
	 * Test if every neighborhood pixel of the 3D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeType
	 */
	@Test
	public void testMapNeighborhoodsArrayImage3D() {
		in = generateByteTestImg(true, 3, 3, 3);
		out = generateByteTestImg(false, 3, 3, 3);

		final Op functional =
			ops.op(MapNeighborhoodNativeType.class, out, in,
				new CountNeighborsWithAccess(), 1);
		functional.run();

		final byte[] expected =
			new byte[] { 8, 12, 8, 12, 18, 12, 8, 12, 8, 12, 18, 12, 18, 27, 18, 12,
				18, 12, 8, 12, 8, 12, 18, 12, 8, 12, 8 };

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", expected[index++], t.get());
		}
	}
	
	/**
	 * Test if every neighborhood pixel of the 3D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodNativeTypeExtended
	 */
	@Test
	public void testMapNeighborhoodsArrayImageAlias3D() {
		in = generateByteTestImg(true, 7, 7, 7);
		out = generateByteTestImg(false, 7, 7, 7);

		final Op functional =
			ops.op(MapNeighborhoodNativeTypeExtended.class, out, in,
				new CountNeighbors(), new RectangleShape(1, false));
		functional.run();

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", 27, t.get());
			index++;
		}
	}

	/**
	 * Test if every neighborhood pixel of the 2D image was really accessed during
	 * the map operation.
	 * 
	 * @see MapNeighborhoodWithCenterNativeType
	 */
	@Test
	public void testMapNeighborhoodsWithCenterAccessArrayImage2D() {
		final Op functional =
			ops.op(MapNeighborhoodWithCenterNativeType.class, out, in,
				new CountNeighborsWithAccessWithCenter(), 1);
		functional.run();

		final byte[] expected =
			new byte[] { 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 9, 9, 9, 9, 9, 9, 9, 9,
				9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6,
				6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9,
				9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 6, 9, 9, 9,
				9, 9, 9, 9, 9, 9, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4 };

		int index = 0;
		for (ByteType t : out) {
			assertEquals("Index " + index + ": ", expected[index++], t.get());
		}
	}
	
	

	/**
	 * Function which increments the output value for every pixel in the
	 * neighborhood.
	 *
	 * @author Jonathan Hale
	 */
	private static class CountNeighbors extends
		AbstractComputerOp<Iterable<ByteType>, ByteType>
	{

		@Override
		public void compute(final Iterable<ByteType> input, final ByteType output) {
			for (final ByteType b : input) {
				output.inc();
			}
		}
	}

	/**
	 * Function which increments a outputPixel for every neighboring pixel defined
	 * by the mapping.
	 *
	 * @author Jonathan Hale
	 */
	private static class CountNeighborsWithCenter extends
		AbstractCenterAwareComputerOp<ByteType, ByteType>
	{

		@Override
		public void compute(final Pair<ByteType, Iterable<ByteType>> input,
			final ByteType output)
		{
			ByteType a = input.getA();

			a.set((byte) 0);
			output.set((byte) 0);

			for (final ByteType b : input.getB()) {
				output.inc();
				a.inc();
			}
		}
	}

	/**
	 * Computer which increments a outputPixel for every neighboring pixel defined
	 * by the mapping and tries to access the input pixels value to ensure that no
	 * access is out of bounds.
	 *
	 * @author Jonathan Hale
	 */
	private static class CountNeighborsWithAccess extends
		AbstractComputerOp<Iterable<ByteType>, ByteType>
	{

		@Override
		public void compute(final Iterable<ByteType> input, final ByteType output) {
			try {
				for (final ByteType t : input) {
					output.inc();
					t.inc();
				}
			}
			catch (final Exception e) {
				fail(e.toString());
			}
		}
	}

	/**
	 * Computer which increments a outputPixel for every neighboring pixel defined
	 * by the mapping and tries to access the input pixels value to ensure that no
	 * access is out of bounds.
	 *
	 * @author Jonathan Hale
	 */
	private static class CountNeighborsWithAccessWithCenter extends
		AbstractCenterAwareComputerOp<ByteType, ByteType>
	{

		@Override
		public void compute(final Pair<ByteType, Iterable<ByteType>> input,
			final ByteType output)
		{
			try {
				input.getA().inc();

				for (final ByteType t : input.getB()) {
					output.inc();
					t.inc();
				}
			}
			catch (final Exception e) {
				fail(e.toString());
			}
		}
	}

	/**
	 * Computer which sets a outputPixel to <code>input.get() + 1</code>.
	 * Generally, this computer is invalid as input to neighborhood maps.
	 *
	 * @author Jonathan Hale
	 */
	private static class Increment extends AbstractComputerOp<ByteType, ByteType>
	{

		@Override
		public void compute(final ByteType input, final ByteType output) {
			output.set((byte) (input.get() + 1));
		}
	}

}
