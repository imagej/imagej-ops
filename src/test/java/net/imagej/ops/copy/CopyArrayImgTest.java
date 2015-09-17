
package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyArrayImg}
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CopyArrayImgTest extends AbstractOpTest {

	private Img<UnsignedByteType> input;

	@Before
	public void createData() {
		input =
			new ArrayImgFactory<UnsignedByteType>().create(new int[] { 120, 100 },
				new UnsignedByteType());

		final Random r = new Random(System.currentTimeMillis());

		final Cursor<UnsignedByteType> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().setReal(r.nextDouble() * 255);
		}
	}

	@Test
	public void copyArrayImgNoOutputTest() {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<UnsignedByteType> output =
			(RandomAccessibleInterval<UnsignedByteType>) ops.run(CopyArrayImg.class,
				input);

		final Cursor<UnsignedByteType> inc = input.localizingCursor();
		final RandomAccess<UnsignedByteType> outRA = output.randomAccess();

		while (inc.hasNext()) {
			inc.fwd();
			outRA.setPosition(inc);
			assertEquals(inc.get().get(), outRA.get().get());
		}
	}

	@Test
	public void copyArrayImgWithOutputTest() {
		final Img<UnsignedByteType> output =
			input.factory().create(input, input.firstElement());

		ops.run(CopyArrayImg.class, output, input);

		final Cursor<UnsignedByteType> inc = input.cursor();
		final Cursor<UnsignedByteType> outc = output.cursor();

		while (inc.hasNext()) {
			assertTrue(outc.next().equals(inc.next()));
		}
	}
}
