
package net.imagej.ops.segment.hough;

import java.util.List;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.hybrid.Hybrids;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Context;
import org.scijava.app.AppService;
import org.scijava.io.IOService;
import org.scijava.ui.UIService;

public class HoughDemo {

	public static void main(final String... args) throws Exception {
		final Context context = new Context();
		final UIService ui = context.service(UIService.class);
		final IOService io = context.service(IOService.class);
		final OpService ops = context.service(OpService.class);
		final AppService app = context.service(AppService.class);

		ui.showUI();

		final String path = app.getApp().getBaseDirectory() +
			// TEMP: The need for "/../.." is a bug, which is
			// fixed on SJC master now, but for now we need it.
			"/../../src/test/resources/net/imagej/ops/segment/hough/circles.png";
		System.out.println(path);
		final Img<UnsignedByteType> input = (Img<UnsignedByteType>) io.open(path);
		final Img<BitType> img = (Img<BitType>) ops.run(Ops.Convert.Bit.class,
			input);

		/*
		 * Hough transform.
		 */
		final long minCircleRadius = 40;
		final long maxCircleRadius = 250;
		final long sigma = 1;
		final long stepRadius = 3;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final HoughTransformOpNoWeights<BitType> hough =
			(HoughTransformOpNoWeights) Hybrids.unaryCF(ops,
				HoughTransformOpNoWeights.class, Img.class, img, minCircleRadius,
				maxCircleRadius, stepRadius);

		final Img<DoubleType> voteImg = hough.calculate(img);

		ui.show("Two circles", img);
		ui.show("Vote", voteImg);

		/*
		 * Hough detection.
		 */

		final double sensitivity = 1.;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final HoughCircleDetectorDogOp<DoubleType> houghDetector =
			(HoughCircleDetectorDogOp) Functions.unary(ops,
				HoughCircleDetectorDogOp.class, List.class, voteImg, minCircleRadius,
				stepRadius, sigma, sensitivity);
		final List<HoughCircle> circles = houghDetector.calculate(voteImg);
		System.out.println("Found " + circles.size() + " circles:");
		for (final HoughCircle circle : circles) {
			System.out.println(circle);
		}
	}

}
