package net.imagej.ops.topology.eulerCharacteristic;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Octant Octant} convenience class
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class OctantTest {
    @Test
    public void testIsNeighborhoodEmpty() throws Exception {
        final Img<BitType> img = ArrayImgs.bits(2, 2, 2);
        Octant<BitType> octant = new Octant<>(img);

        octant.setNeighborhood(1, 1, 1);

        assertTrue("Neighborhood should be empty", octant.isNeighborhoodEmpty());

        img.forEach(BitType::setOne);
        octant.setNeighborhood(1, 1, 1);

        assertFalse("Neighborhood should not be empty", octant.isNeighborhoodEmpty());
    }

    @Test
    public void testSetNeighborhood() throws Exception {
        final Img<BitType> img = ArrayImgs.bits(3, 3, 3);
        Octant<BitType> octant = new Octant<>(img);

        final RandomAccess<BitType> access = img.randomAccess();
        for (int z = 0; z < 2; z++) {
            access.setPosition(z, 2);
            for (int y = 0; y < 2; y++) {
                access.setPosition(y, 1);
                for (int x = 0; x < 2; x++) {
                    access.setPosition(x, 0);
                    access.get().setOne();
                }
            }
        }

        octant.setNeighborhood(1, 1, 1);
        assertEquals("All neighbours should be foreground", 8, octant.getNeighborCount());

        octant.setNeighborhood(2, 2, 2);
        assertEquals("Wrong number of foreground neighbors", 1, octant.getNeighborCount());
    }
}