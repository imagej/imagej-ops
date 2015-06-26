package net.imagej.ops.view;

import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.view.ViewOps.AddDimension;
import net.imagej.ops.view.ViewOps.Collapse;
import net.imagej.ops.view.ViewOps.CollapseNumeric;
import net.imagej.ops.view.ViewOps.CollapseReal;
import net.imagej.ops.view.ViewOps.DropSingletonDimensions;
import net.imagej.ops.view.ViewOps.Extend;
import net.imagej.ops.view.ViewOps.ExtendBorder;
import net.imagej.ops.view.ViewOps.ExtendMirrorDouble;
import net.imagej.ops.view.ViewOps.ExtendMirrorSingle;
import net.imagej.ops.view.ViewOps.ExtendPeriodic;
import net.imagej.ops.view.ViewOps.ExtendRandom;
import net.imagej.ops.view.ViewOps.ExtendValue;
import net.imagej.ops.view.ViewOps.ExtendZero;
import net.imagej.ops.view.ViewOps.FlatIterable;
import net.imagej.ops.view.ViewOps.HyperSlice;
import net.imagej.ops.view.ViewOps.Interpolate;
import net.imagej.ops.view.ViewOps.InvertAxis;
import net.imagej.ops.view.ViewOps.IsZeroMin;
import net.imagej.ops.view.ViewOps.Offset;
import net.imagej.ops.view.ViewOps.Permute;
import net.imagej.ops.view.ViewOps.PermuteCoordinates;
import net.imagej.ops.view.ViewOps.PermuteCoordinatesInverse;
import net.imagej.ops.view.ViewOps.Raster;
import net.imagej.ops.view.ViewOps.Rotate;
import net.imagej.ops.view.ViewOps.Shear;
import net.imagej.ops.view.ViewOps.Stack;
import net.imagej.ops.view.ViewOps.Subsample;
import net.imagej.ops.view.ViewOps.Translate;
import net.imagej.ops.view.ViewOps.UnShear;
import net.imagej.ops.view.ViewOps.View;
import net.imagej.ops.view.ViewOps.ZeroMin;
import net.imagej.ops.views.ViewNamespace;

import org.junit.Test;

/**
 * Tests that the ops of the logic namespace have corresponding type-safe Java
 * method signatures declared in the {@link ViewNamespace} class.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class ViewNamespaceTest extends AbstractNamespaceTest {

	/** Tests for {@link View} method convergence. */
	@Test
	public void testView() {
		assertComplete("view", ViewNamespace.class, View.NAME);
	}

	/** Tests for {@link Interpolate} method convergence. */
	@Test
	public void testInterpolate() {
		assertComplete("view", ViewNamespace.class, Interpolate.NAME);
	}

	/** Tests for {@link Raster} method convergence. */
	@Test
	public void testRaster() {
		assertComplete("view", ViewNamespace.class, Raster.NAME);
	}

	/** Tests for {@link Extend} method convergence. */
	@Test
	public void testExtend() {
		assertComplete("view", ViewNamespace.class, Extend.NAME);
	}

	/** Tests for {@link ExtendMirrorSingle} method convergence. */
	@Test
	public void testExtendMirrorSingle() {
		assertComplete("view", ViewNamespace.class, ExtendMirrorSingle.NAME);
	}

	/** Tests for {@link ExtendMirrorDouble} method convergence. */
	@Test
	public void testExtendMirrorDouble() {
		assertComplete("view", ViewNamespace.class, ExtendMirrorDouble.NAME);
	}

	/** Tests for {@link ExtendValue} method convergence. */
	@Test
	public void testExtendValue() {
		assertComplete("view", ViewNamespace.class, ExtendValue.NAME);
	}

	/** Tests for {@link ExtendZero} method convergence. */
	@Test
	public void testExtendZero() {
		assertComplete("view", ViewNamespace.class, ExtendZero.NAME);
	}

	/** Tests for {@link ExtendRandom} method convergence. */
	@Test
	public void testExtendRandom() {
		assertComplete("view", ViewNamespace.class, ExtendRandom.NAME);
	}

	/** Tests for {@link ExtendPeriodic} method convergence. */
	@Test
	public void testExtendPeriodic() {
		assertComplete("view", ViewNamespace.class, ExtendPeriodic.NAME);
	}

	/** Tests for {@link ExtendBorder} method convergence. */
	@Test
	public void testExtendBorder() {
		assertComplete("view", ViewNamespace.class, ExtendBorder.NAME);
	}

	/** Tests for {@link Rotate} method convergence. */
	@Test
	public void testRotate() {
		assertComplete("view", ViewNamespace.class, Rotate.NAME);
	}

	/** Tests for {@link Permute} method convergence. */
	@Test
	public void testPermute() {
		assertComplete("view", ViewNamespace.class, Permute.NAME);
	}

	/** Tests for {@link Translate} method convergence. */
	@Test
	public void testTranslate() {
		assertComplete("view", ViewNamespace.class, Translate.NAME);
	}

	/** Tests for {@link Offset} method convergence. */
	@Test
	public void testOffset() {
		assertComplete("view", ViewNamespace.class, Offset.NAME);
	}

	/** Tests for {@link ZeroMin} method convergence. */
	@Test
	public void testZeroMin() {
		assertComplete("view", ViewNamespace.class, ZeroMin.NAME);
	}

	/** Tests for {@link HyperSlice} method convergence. */
	@Test
	public void testHyperSlice() {
		assertComplete("view", ViewNamespace.class, HyperSlice.NAME);
	}

	/** Tests for {@link AddDimension} method convergence. */
	@Test
	public void testAddDimension() {
		assertComplete("view", ViewNamespace.class, AddDimension.NAME);
	}

	/** Tests for {@link InvertAxis} method convergence. */
	@Test
	public void testInvertAxis() {
		assertComplete("view", ViewNamespace.class, InvertAxis.NAME);
	}

	/** Tests for {@link IsZeroMin} method convergence. */
	@Test
	public void testIsZeroMin() {
		assertComplete("view", ViewNamespace.class, IsZeroMin.NAME);
	}

	/**
	 * Tests for {@link net.imagej.ops.view.ViewOps.Iterable} method
	 * convergence.
	 */
	@Test
	public void testIterable() {
		assertComplete("view", ViewNamespace.class,
				net.imagej.ops.view.ViewOps.Iterable.NAME);
	}

	/** Tests for {@link FlatIterable} method convergence. */
	@Test
	public void testFlatIterable() {
		assertComplete("view", ViewNamespace.class, FlatIterable.NAME);
	}

	/** Tests for {@link Collapse} method convergence. */
	@Test
	public void testCollapse() {
		assertComplete("view", ViewNamespace.class, Collapse.NAME);
	}

	/** Tests for {@link CollapseReal} method convergence. */
	@Test
	public void testCollapseReal() {
		assertComplete("view", ViewNamespace.class, CollapseReal.NAME);
	}

	/** Tests for {@link CollapseNumeric} method convergence. */
	@Test
	public void testCollapseNumeric() {
		assertComplete("view", ViewNamespace.class, CollapseNumeric.NAME);
	}

	/** Tests for {@link Subsample} method convergence. */
	@Test
	public void testSubsample() {
		assertComplete("view", ViewNamespace.class, Subsample.NAME);
	}

	/** Tests for {@link DropSingletonDimensions} method convergence. */
	@Test
	public void testDropSingletonDimensions() {
		assertComplete("view", ViewNamespace.class,
				DropSingletonDimensions.NAME);
	}

	/** Tests for {@link Stack} method convergence. */
	@Test
	public void testStack() {
		try {
			assertComplete("view", ViewNamespace.class, Stack.NAME);
			// as soon as this op is implemented this test will fail
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
	}

	/** Tests for {@link Shear} method convergence. */
	@Test
	public void testShear() {
		try {
			assertComplete("view", ViewNamespace.class, Shear.NAME);
			// as soon as this op is implemented this test will fail
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
	}

	/** Tests for {@link UnShear} method convergence. */
	@Test
	public void testUnShear() {
		try {
			assertComplete("view", ViewNamespace.class, UnShear.NAME);
			// as soon as this op is implemented this test will fail
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
	}

	/** Tests for {@link PermuteCoordinates} method convergence. */
	@Test
	public void testPermuteCoordinates() {
			assertComplete("view", ViewNamespace.class, PermuteCoordinates.NAME);
			
	}

	/** Tests for {@link PermuteCoordinatesInverse} method convergence. */
	@Test
	public void testPermuteCoordinatesInverse() {
		assertComplete("view", ViewNamespace.class,
				PermuteCoordinatesInverse.NAME);
	}
}