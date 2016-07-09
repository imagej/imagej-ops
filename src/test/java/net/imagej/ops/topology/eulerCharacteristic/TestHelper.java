package net.imagej.ops.topology.eulerCharacteristic;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;

/**
 * A class to generate cubes used in testing
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class TestHelper {
    public static Img<BitType> drawCube(final long width, final long height, final long depth, final long padding) {
        final long totalPadding = 2 * padding;
        final Img<BitType> cube = ArrayImgs.bits(width + totalPadding, height + totalPadding, depth + totalPadding);
        final long x1 = padding + width;
        final long y1 = padding + height;
        final long z1 = padding + depth;
        final RandomAccess<BitType> access = cube.randomAccess();

        for (long z = padding; z < z1; z++) {
            access.setPosition(z, 2);
            for (long y = padding; y < y1; y++) {
                access.setPosition(y, 1);
                for (long x = padding; x < x1; x++) {
                    access.setPosition(x, 0);
                    access.get().setOne();
                }
            }
        }

        return cube;
    }
}
