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

package net.imagej.ops.morphology.blackTopHat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.morphology.BlackTopHat;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.HorizontalLineShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link net.imagej.ops.Ops.Morphology.BlackTopHat}
 * 
 * @author Leon Yang
 */
public class BlackTopHatTest extends AbstractOpTest {

	private Img<ByteType> in;

	@Before
	public void initialize() {
		in = generateByteArrayTestImg(true, 10, 10);
	}

	@Test
	public void testSingleBlackTopHat() {
		final Shape shape = new DiamondShape(1);
		final List<Shape> shapes = Arrays.asList(shape);
		@SuppressWarnings("unchecked")
		final Img<ByteType> out1 = (Img<ByteType>) ops.run(ListBlackTopHat.class,
			Img.class, in, shapes);
		final Img<ByteType> out2 = BlackTopHat.blackTopHat(in, shape, 1);
		assertIterationsEqual(out2, out1);
	}

	@Test
	public void testListBlackTopHat() {
		final List<Shape> shapes = new ArrayList<>();
		shapes.add(new DiamondShape(1));
		shapes.add(new DiamondShape(1));
		shapes.add(new RectangleShape(1, false));
		shapes.add(new HorizontalLineShape(2, 1, false));
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> out1 = (IterableInterval<ByteType>) ops
			.run(ListBlackTopHat.class, IterableInterval.class, in, shapes);
		final Img<ByteType> out2 = BlackTopHat.blackTopHat(in, shapes, 1);
		assertIterationsEqual(out2, out1);
	}
}
