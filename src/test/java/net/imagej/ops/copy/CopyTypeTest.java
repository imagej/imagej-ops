package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Test {@link CopyType}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class CopyTypeTest extends AbstractOpTest {

	private DoubleType dt;

	@Before
	public void createData() {
		dt = new DoubleType(2);
	}
	
	@Test
	public void copyTypeNoOutputTest() {
		Object out = ops.run(CopyType.class, dt);
		
		if (out instanceof DoubleType) {
			assertEquals(dt.get(), ((DoubleType) out).get(), 0.0);
		} else {
			assertTrue("Copy is not instance of DoubleType.",false);
		}
		
	}
	
	@Test
	public void copyTypeWithOutputTest() {
		DoubleType out = new DoubleType();
		ops.run(CopyType.class, out, dt);
		
		assertEquals(dt.get(), out.get(), 0.0);
		
	}
}
