package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelingMapping;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;

/**
 * Test @link {@link CopyLabelingMapping}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz 
 *
 */
public class CopyLabelingMappingTest extends AbstractOpTest {

	private LabelingMapping<String> input;
	
	@Before
	public void createData() {
		ImgLabeling<String, IntType> imgL = (ImgLabeling<String, IntType>) ops.create().imgLabeling(new long[] {
			10, 10 }, new IntType());

		final Cursor<LabelingType<String>> inc = imgL.cursor();

		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}

		// and another loop to construct some ABs
		while (inc.hasNext()) {
			inc.next().add(Math.random() > 0.5 ? "A" : "B");
		}
		
		input = imgL.getMapping();
	}
	
	@Test
	public void copyLabelingWithoutOutputTest() {
		
		LabelingMapping<String> out = (LabelingMapping<String>) ops.run(CopyLabelingMapping.class, input);
		
		Iterator<String> outIt = out.getLabels().iterator();
		
		for (String l : input.getLabels()) {
			assertEquals(l, outIt.next());
		}
	}
	
	@Test
	public void copyLabelingWithOutputTest() {
		
		LabelingMapping<String> out = ops.create().labelingMapping(); 
				
	    ops.run(CopyLabelingMapping.class, out, input);
		
		Iterator<String> outIt = out.getLabels().iterator();
		
		for (String l : input.getLabels()) {
			assertEquals(l, outIt.next());
		}
	}
}
