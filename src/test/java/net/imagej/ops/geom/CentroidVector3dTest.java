
package net.imagej.ops.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

import net.imagej.ops.AbstractOpTest;

import org.joml.Vector3d;
import org.junit.Test;

/**
 * Unit tests for {@link CentroidVector3d}
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
public class CentroidVector3dTest extends AbstractOpTest {

	@Test
	public void testCalculate() {
		final Vector3d expected = new Vector3d(0.5, 0.5, 0.5);
		//@formatter:off
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
        //@formatter:on

		final Vector3d result = (Vector3d) ops.run(CentroidVector3d.class,
			cubeVectors);

		assertEquals("Incorrect centroid vector", expected, result);
	}

	@Test
	public void testCalculateEmptyCollection() {
		final List<Vector3d> emptyList = Collections.emptyList();

		final Vector3d result = (Vector3d) ops.run(CentroidVector3d.class,
			emptyList);

		assertTrue("Coordinates should all be NaN", DoubleStream.of(result.x,
			result.y, result.z).allMatch(Double::isNaN));
	}

	@Test
	public void testCalculateSingleVector() {
		final Vector3d vector = new Vector3d(1.0, 2.0, 3.0);
		final List<Vector3d> vectors = Collections.singletonList(vector);

		final Vector3d result = (Vector3d) ops.run(CentroidVector3d.class, vectors);

		assertEquals("Incorrect centroid vector", vector, result);
	}
}
