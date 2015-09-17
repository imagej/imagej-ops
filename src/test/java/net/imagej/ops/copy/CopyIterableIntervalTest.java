
package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyIterableInterval}
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CopyIterableIntervalTest extends AbstractOpTest {

	private Img<DoubleType> input;

	@Before
	public void createData() {
		input = new PlanarImgFactory<DoubleType>().create(new int[] { 120, 100 },
			new DoubleType());

		final Random r = new Random(System.currentTimeMillis());

		final Cursor<DoubleType> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().set(r.nextDouble());
		}
	}

	@Test
	public void copyRAINoOutputTest() {
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<DoubleType> output =
			(RandomAccessibleInterval<DoubleType>) ops.run(CopyIterableInterval.class,
				input);

		Cursor<DoubleType> inc = input.localizingCursor();
		RandomAccess<DoubleType> outRA = output.randomAccess();

		while (inc.hasNext()) {
			inc.fwd();
			outRA.setPosition(inc);
			assertEquals(inc.get().get(), outRA.get().get(), 0.0);
		}
	}

	@Test
	public void copyRAIWithOutputTest() {
		Img<DoubleType> output = input.factory().create(input, input
			.firstElement());

		ops.run(CopyIterableInterval.class, output, input);

		final Cursor<DoubleType> inc = input.cursor();
		final Cursor<DoubleType> outc = output.cursor();

		while (inc.hasNext()) {
			assertEquals(inc.next().get(), outc.next().get(), 0.0);
		}
	}
}
