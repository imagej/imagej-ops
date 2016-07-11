package net.imagej.ops.topology.eulerCharacteristic;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import org.junit.Test;

import static net.imagej.ops.topology.eulerCharacteristic.TestHelper.drawCube;
import static org.junit.Assert.assertEquals;

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