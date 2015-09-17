package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Test {@link CopyImg}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class CopyImgTest extends AbstractOpTest {
	private Img<DoubleType> input;

	@Before
	public void createData() {
		input = new ArrayImgFactory<DoubleType>().create(new int[] { 120, 100 },
			new DoubleType());

		final Random r = new Random(System.currentTimeMillis());

		final Cursor<DoubleType> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().set(r.nextDouble());
		}
	}

	@Test
	public void copyImgNoOutputTest() {
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<DoubleType> output =
			(RandomAccessibleInterval<DoubleType>) ops.run(CopyImg.class,
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
	public void copyImgWithOutputTest() {
		Img<DoubleType> output = input.factory().create(input, input
			.firstElement());

		ops.run(CopyImg.class, output, input);

		final Cursor<DoubleType> inc = input.cursor();
		final Cursor<DoubleType> outc = output.cursor();

		while (inc.hasNext()) {
			assertEquals(inc.next().get(), outc.next().get(), 0.0);
		}
	}
}
