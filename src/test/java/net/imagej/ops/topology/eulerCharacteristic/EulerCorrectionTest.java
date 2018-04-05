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
package net.imagej.ops.topology.eulerCharacteristic;

import static net.imagej.ops.topology.eulerCharacteristic.TestHelper.drawCube;
import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.topology.eulerCharacteristic.EulerCorrection.Traverser;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;

import org.junit.Test;

/**
 * Tests {@link EulerCorrection}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class EulerCorrectionTest extends AbstractOpTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConforms() throws Exception {
        final Img<BitType> img = ArrayImgs.bits(3, 3);

        ops.run(EulerCorrection.class, img);
    }

    @Test
    public void testCube() throws Exception {
        final Img<BitType> cube = drawCube(3, 3, 3, 1);
        final Traverser<BitType> traverser = new Traverser<>(cube);

        final int vertices = EulerCorrection.stackCorners(traverser);
        assertEquals("Number of stack vertices is incorrect", 0, vertices);

        final long edges = EulerCorrection.stackEdges(traverser);
        assertEquals("Number stack edge voxels is incorrect", 0, edges);

        final int faces = EulerCorrection.stackFaces(traverser);
        assertEquals("Number stack face voxels is incorrect", 0, faces);

        final long voxelEdgeIntersections = EulerCorrection.voxelEdgeIntersections(traverser);
        assertEquals("Number intersections is incorrect", 0, voxelEdgeIntersections);

        final long voxelFaceIntersections = EulerCorrection.voxelFaceIntersections(traverser);
        assertEquals("Number intersections is incorrect", 0, voxelFaceIntersections);

        final long voxelEdgeFaceIntersections = EulerCorrection.voxelEdgeFaceIntersections(traverser);
        assertEquals("Number intersections is incorrect", 0, voxelEdgeFaceIntersections);

        final double result = ops.topology().eulerCorrection(cube).get();
        assertEquals("Euler correction is incorrect", 0, result, 1e-12);
    }

    @Test
    public void testEdgeCube() throws Exception {
        final int edges = 12;
        final int cubeSize = 3;
        final int edgeSize = cubeSize - 2;
        final Img<BitType> cube = drawCube(cubeSize, cubeSize, cubeSize, 0);
        final Traverser<BitType> traverser = new Traverser<>(cube);

        final int vertices = EulerCorrection.stackCorners(traverser);
        assertEquals("Number of stack vertices is incorrect", 8, vertices);

        final long stackEdges = EulerCorrection.stackEdges(traverser);
        assertEquals("Number stack edge voxels is incorrect", edges * edgeSize, stackEdges);

        final int faces = EulerCorrection.stackFaces(traverser);
        assertEquals("Number stack face voxels is incorrect", 6 * edgeSize * edgeSize, faces);

        final long voxelEdgeIntersections = EulerCorrection.voxelEdgeIntersections(traverser);
        // you can fit n - 1 2x1 edges on edges whose size is n
        final long expectedVEIntersections = edges * (cubeSize - 1);
        assertEquals("Number intersections is incorrect", expectedVEIntersections, voxelEdgeIntersections);

        final long xyVFIntersections = (cubeSize + 1) * (cubeSize + 1);
        final long yzVFIntersections = (cubeSize - 1) * (cubeSize + 1);
        final long xzVFIntersections = (cubeSize - 1) * (cubeSize - 1);
        final long expectedVFIntersections = xyVFIntersections * 2 + yzVFIntersections * 2 + xzVFIntersections * 2;
        final long voxelFaceIntersections = EulerCorrection.voxelFaceIntersections(traverser);
        assertEquals("Number intersections is incorrect", expectedVFIntersections, voxelFaceIntersections);

        final long voxelEdgeFaceIntersections = EulerCorrection.voxelEdgeFaceIntersections(traverser);
        assertEquals("Number intersections is incorrect", 108, voxelEdgeFaceIntersections);

        final double result = ops.topology().eulerCorrection(cube).get();
        assertEquals("Euler contribution is incorrect", 1, result, 1e-12);
    }
}
