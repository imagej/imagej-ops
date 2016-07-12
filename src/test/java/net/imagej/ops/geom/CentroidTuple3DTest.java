package net.imagej.ops.geom;

import net.imagej.ops.AbstractOpTest;
import org.junit.Test;
import org.scijava.vecmath.Vector3d;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CentroidTuple3D}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class CentroidTuple3DTest extends AbstractOpTest {
    @Test
    public void testCompute1EmptyCollection() throws Exception {
        final List<Vector3d> emptyList = Collections.emptyList();

        final Vector3d result = (Vector3d) ops.run(CentroidTuple3D.class, emptyList);

        assertTrue("Coordinates should all be NaN",
                DoubleStream.of(result.x, result.y, result.z).allMatch(Double::isNaN));
    }

    @Test
    public void testCompute1SingleVector() throws Exception {
        final Vector3d vector = new Vector3d(1.0, 2.0, 3.0);
        final List<Vector3d> vectors = Collections.singletonList(vector);

        final Vector3d result = (Vector3d) ops.run(CentroidTuple3D.class, vectors);

        assertEquals("Incorrect centroid vector", vector, result);
    }

    @Test
    public void testCompute1() throws Exception {
        final Vector3d expected = new Vector3d(0.5, 0.5, 0.5);
        final List<Vector3d> cubeVectors = Arrays.asList(
                new Vector3d(0.0, 0.0, 0.0),
                new Vector3d(1.0, 0.0, 0.0),
                new Vector3d(1.0, 1.0, 0.0),
                new Vector3d(0.0, 1.0, 0.0),
                new Vector3d(0.0, 0.0, 1.0),
                new Vector3d(1.0, 0.0, 1.0),
                new Vector3d(1.0, 1.0, 1.0),
                new Vector3d(0.0, 1.0, 1.0)
        );

        final Vector3d result = (Vector3d) ops.run(CentroidTuple3D.class, cubeVectors);

        assertEquals("Incorrect centroid vector", expected, result);
    }
}