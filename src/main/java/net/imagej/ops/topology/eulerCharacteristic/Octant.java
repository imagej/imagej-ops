package net.imagej.ops.topology.eulerCharacteristic;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.view.Views;

import java.util.Arrays;

/**
 * A convenience class for storing a 2x2x2 voxel neighborhood in an image
 *
 * @author Richard Domander (Royal Veterinary College, London)
 * @author Mark Hiner
 */
public class Octant<B extends BooleanType<B>> {
    private final boolean[] neighborhood = new boolean[8];
    private final RandomAccess<B> access;
    private int foregroundNeighbors;

    /**
     * Constructs a new 2x2x2 neighborhood
     *
     * <p>
     * <em>NB</em>: Copies reference
     * </p>
     *
     * @param interval       Image space where the neighborhood is located
     */
    public Octant(final RandomAccessibleInterval<B> interval) {
        access = Views.extendZero(interval).randomAccess();
    }

    /** Returns the number of foreground voxels in the neighborhood */
    public int getNeighborCount() {
        return foregroundNeighbors;
    }

    /**
     * Check if the nth neighbor in the 8-neighborhood is foreground
     *
     * @param n number of neighbor, {@literal 1 <= n <= 8}
     */
    public boolean isNeighborForeground(final int n) {
        return neighborhood[n - 1];
    }

    /** True if none of the elements in the neighborhood are foreground (true) */
    public boolean isNeighborhoodEmpty() {
        return foregroundNeighbors == 0;
    }

    /**
     * Set the starting coordinates of the neighborhood in the interval
     * 
     * NB: All voxels outside the image bounds are considered 0
     */
    public void setNeighborhood(final long x, final long y, final long z) {
        Arrays.fill(neighborhood, false);

        neighborhood[0] = getAtLocation(access, x - 1, y - 1, z - 1);
        neighborhood[1] = getAtLocation(access, x - 1, y, z - 1);
        neighborhood[2] = getAtLocation(access, x, y - 1, z - 1);
        neighborhood[3] = getAtLocation(access, x, y, z - 1);
        neighborhood[4] = getAtLocation(access, x - 1, y - 1, z);
        neighborhood[5] = getAtLocation(access, x - 1, y, z);
        neighborhood[6] = getAtLocation(access, x, y - 1, z);
        neighborhood[7] = getAtLocation(access, x, y, z);

        countForegroundNeighbors();
    }

    private void countForegroundNeighbors() {
        foregroundNeighbors = 0;
        for (boolean neighbor : neighborhood) {
            if (neighbor) {
                foregroundNeighbors++;
            }
        }
    }

    private boolean getAtLocation(final RandomAccess<B> access, final long x, final long y, final long z) {
        access.setPosition(x, 0);
        access.setPosition(y, 1);
        access.setPosition(z, 2);
        return access.get().get();
    }
}
