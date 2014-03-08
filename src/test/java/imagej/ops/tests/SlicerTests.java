
package imagej.ops.tests;

import static org.junit.Assert.assertTrue;
import imagej.ops.OpService;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.labeling.Labeling;
import net.imglib2.labeling.NativeImgLabeling;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;

public class SlicerTests {

	private Context context;

	private OpService ops;

	private Img<ByteType> in;

	@Before
	public void setUp() {
		context = new Context(OpService.class);
		ops = context.getService(OpService.class);
		assertTrue(ops != null);

		in = ArrayImgs.bytes(20, 20, 20);
	}

	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
		}
	}

	@Test
	public void testSlicerTypes() {

		// Set-up interval
		final Interval defInterval =
			new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 19, 19, 19 });

		final Interval smallerInterval =
			new FinalInterval(new long[] { 0, 0, 0 }, new long[] { 19, 19, 18 });

		// check if result is ImgView
		assertTrue(ops.run("slicer", in, defInterval) instanceof Img);

		// check if result is LabelingView
		assertTrue(ops.run("slicer", new NativeImgLabeling<String, ByteType>(in),
			defInterval) instanceof Labeling);

		// check if result is ImgPlus
		assertTrue(ops.run("slicer", new ImgPlus<ByteType>(in), defInterval) instanceof ImgPlus);

		// check if result is RandomAccessibleInterval
		final Object run =
			ops.run("slicer", Views.interval(in, smallerInterval), smallerInterval);
		assertTrue(run instanceof RandomAccessibleInterval && !(run instanceof Img));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSlicerResults() {

		// Case 1: fix one dimension
		Img<ByteType> res =
			(Img<ByteType>) ops.run("slicer", in, new FinalInterval(new long[] { 0,
				0, 5 }, new long[] { 19, 19, 5 }));

		assertTrue(res.numDimensions() == 2);
		assertTrue(res.min(0) == 0);
		assertTrue(res.max(0) == 19);

		// Case B: Fix one dimension and don't start at zero
		res =
			(Img<ByteType>) ops.run("slicer", in, new FinalInterval(new long[] { 0,
				0, 5 }, new long[] { 19, 0, 10 }));

		assertTrue(res.numDimensions() == 2);
		assertTrue(res.min(0) == 0);
		assertTrue(res.max(1) == 5);

		// Case C: fix two dimensions
		res =
			(Img<ByteType>) ops.run("slicer", in, new FinalInterval(new long[] { 0,
				0, 0 }, new long[] { 0, 15, 0 }));

		assertTrue(res.numDimensions() == 1);
		assertTrue(res.max(0) == 15);
	}
}
