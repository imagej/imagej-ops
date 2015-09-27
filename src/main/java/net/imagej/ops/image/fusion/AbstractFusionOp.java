package net.imagej.ops.image.fusion;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public abstract class AbstractFusionOp<T extends RealType<T>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> {

	@Parameter(type = ItemIO.INPUT)
	protected RandomAccessibleInterval<T> in2;

	@Parameter(type = ItemIO.INPUT)
	protected long[] offset;

	@Parameter
	protected OpService ops;

	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> in1) {
		FinalInterval outInterval = calculateOutputSize(in1, in2, offset);

		// extend img1 with the value the selected op needs to calculate the
		// fusion
		T extensionValue = getExtensionValue(in2.randomAccess().get().createVariable());
		RandomAccess<T> img1 = Views.extendValue(in1, extensionValue).randomAccess();

		// extend in2 with the same criteria and move
		// in2 such that in1 and in2 have the same point in their origin
		RandomAccess<T> img2 = Views.offset(Views.extendValue(in2, extensionValue), offset).randomAccess();

		@SuppressWarnings("unchecked")
		T type = (T) ops.create().nativeType(img1.get().getClass());
		Img<T> outImg = ops.create().img(outInterval, type);
		Cursor<T> outCursor = outImg.localizingCursor();
		long[] pos = new long[outImg.numDimensions()];
		long[] img1Pos = new long[outImg.numDimensions()];
		long[] img2Pos = new long[outImg.numDimensions()];
		while (outCursor.hasNext()) {
			outCursor.fwd();
			outCursor.localize(pos);

			// moving cursor positions according to the offset, otherwise we
			// miss the real origin in certain situations
			img1Pos = updatePosition(pos, offset);
			img2Pos = updatePosition(pos, offset);

			img1.setPosition(img1Pos);
			img2.setPosition(img2Pos);

			T img1Value = img1.get();
			T img2Value = img2.get();

			outCursor.get().set(getPixelValue(img1Value, img2Value));
		}
		return outImg;
	}

	private FinalInterval calculateOutputSize(RandomAccessibleInterval<T> input1, RandomAccessibleInterval<T> input2,
			long[] offset) {
		long[] outImgsize = new long[input1.numDimensions()];
		for (int i = 0; i < input1.numDimensions(); i++) {
			outImgsize[i] = input1.dimension(i) + input2.dimension(i) - (input1.dimension(i) - Math.abs(offset[i]));
		}

		FinalInterval outInterval = Intervals.createMinMax(0, 0, outImgsize[0], outImgsize[1]);
		return outInterval;
	}

	private long[] updatePosition(long[] currentPosition, long[] offset) {
		long[] newPosition = new long[currentPosition.length];
		newPosition[0] = currentPosition[0];
		newPosition[1] = currentPosition[1];

		if (offset[0] > -1) {
			newPosition[0] -= offset[0];
		}
		if (offset[1] > -1) {
			newPosition[1] -= offset[1];
		}
		return newPosition;
	}

	public abstract T getPixelValue(T in1, T in2);

	/**
	 * Sets the parameter to the value the
	 * 
	 * @param type
	 *            a T which shall hold the extension
	 * @return the T set to the extension value
	 */
	public abstract T getExtensionValue(T type);
}
