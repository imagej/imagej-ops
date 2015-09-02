
package net.imagej.ops.copy;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyImgLabeling}
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CopyImgLabelingTest extends AbstractOpTest {

	private ImgLabeling<String, IntType> input;

	@SuppressWarnings("unchecked")
	@Before
	public void createData() {
		input = (ImgLabeling<String, IntType>) ops.create().imgLabeling(new long[] {
			10, 10 }, new IntType());

		final Cursor<LabelingType<String>> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}

		// and another loop to construct some ABs
		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}

	}

	@Test
	public void copyImgLabeling() {
		ImgLabeling<String, IntType> copy = (ImgLabeling<String, IntType>) ops.copy().imgLabeling(
			input);
		assertNotNull(copy);

		Cursor<LabelingType<String>> inCursor = input.cursor();
		for (final LabelingType<String> type : copy) {
			type.equals(inCursor.next());
		}
	}
}
