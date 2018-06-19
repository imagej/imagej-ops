package net.imagej.ops.geom;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import org.scijava.plugin.Plugin;
import org.scijava.vecmath.Tuple3d;
import org.scijava.vecmath.Vector3d;

import java.util.Collection;

/**
 * Calculates the centroid (geometrical centre) of the given tuples
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Ops.Geometric.Centroid.class)
public class CentroidTuple3D extends AbstractUnaryFunctionOp<Collection<? extends Tuple3d>, Vector3d>
        implements Ops.Geometric.Centroid {
    @Override
    public Vector3d calculate(final Collection<? extends Tuple3d> input) {
        final int vectors = input.size();
        if (vectors == 0) {
            return new Vector3d(Double.NaN, Double.NaN, Double.NaN);
        }

        final Vector3d sum = new Vector3d(0.0, 0.0, 0.0);
        input.stream().forEach(sum::add);
        sum.scale(1.0 / vectors);

        return sum;
    }
}
