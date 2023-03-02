/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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
package net.imagej.ops.topology.eulerCharacteristic;

import static net.imagej.ops.topology.eulerCharacteristic.TestHelper.drawCube;
import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;

import org.junit.Test;

/**
 * Tests for the {@link EulerCharacteristic26NFloating} class
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class EulerCharacteristic26NFloatingTest extends AbstractOpTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConforms() throws AssertionError {
        final Img<BitType> img = ArrayImgs.bits(3, 3);

        ops.topology().eulerCharacteristic26NFloating(img);
    }

    /**
     * Test with a single voxel (=solid cube) that floats in the middle of a 3x3x3 space
     * <p>
     * Here χ = β_0 - β_1 + β_2 = 1 - 0 + 0 = 1.<br>
     * The formula χ = vertices - edges + faces for surfaces of polyhedra doesn't apply because the cuboid is solid.
     * </p>
     */
    @Test
    public void testCube() throws Exception {
        final Img<BitType> img = drawCube(1, 1, 1, 1);

        final double result = ops.topology().eulerCharacteristic26NFloating(img).get();

        assertEquals("Euler characteristic (χ) is incorrect", 1.0, result, 1e-12);
    }

    /**
     * Test with a single voxel (=solid cube) in a 1x1x1 space
     * <p>
     * In this op result shouldn't differ from {@link #testCube} because space is zero extended
     * </p>
     */
    @Test
    public void testEdgeCube() throws Exception {
        final Img<BitType> img = drawCube(1, 1, 1, 0);

        final double result = ops.topology().eulerCharacteristic26NFloating(img).get();

        assertEquals("Euler characteristic (χ) is incorrect", 1.0, result, 1e-12);
    }

    /**
     * Test with a cube that has a cavity inside
     * <p>
     * Here χ = β_0 - β_1 + β_2 = 1 - 0 + 1 = 2
     * </p>
     */
    @Test
    public void testHollowCube() throws Exception {
        final Img<BitType> img = drawCube(3, 3, 3, 1);
        final RandomAccess<BitType> access = img.randomAccess();

        // Add cavity
        access.setPosition(new long[]{2, 2, 2});
        access.get().setZero();

        final double result = ops.topology().eulerCharacteristic26NFloating(img).get();

        assertEquals("Euler characteristic (χ) is incorrect", 2.0, result, 1e-12);
    }

    /**
     * Test with a cube that has a "handle"
     * <p>
     * Here χ = β_0 - β_1 + β_2 = 1 - 1 + 0 = 0
     * </p>
     */
    @Test
    public void testHandleCube() throws Exception {
        final Img<BitType> cube = drawCube(9, 9, 9, 5);
        final RandomAccess<BitType> access = cube.randomAccess();

        // Draw a handle on the front xy-face of the cuboid
        access.setPosition(9, 0);
        access.setPosition(6, 1);
        access.setPosition(4, 2);
        access.get().setOne();
        access.setPosition(3, 2);
        access.get().setOne();
        access.setPosition(7, 1);
        access.get().setOne();
        access.setPosition(8, 1);
        access.get().setOne();
        access.setPosition(4, 2);
        access.get().setOne();

        final double result = ops.topology().eulerCharacteristic26NFloating(cube).get();

        assertEquals("Euler characteristic (χ) is incorrect", 0.0, result, 1e-12);
    }
}
