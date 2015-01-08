package net.imagej.ops.features.moments.helper;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.features.moments.CentralMomentResults;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "CentralMomentsHelper")
public class CentralMomentsHelper
		extends
		AbstractOutputFunction<IterableInterval<? extends RealType<?>>, CentralMomentResults> {

	@Parameter(type = ItemIO.INPUT)
	private NormalMomentsHelper momentsHelper;

	@Override
	public CentralMomentResults createOutput(
			IterableInterval<? extends RealType<?>> input) {
		return new CentralMomentResults();
	}

	@Override
	protected CentralMomentResults safeCompute(
			IterableInterval<? extends RealType<?>> input,
			CentralMomentResults output) {

		final double centerX = momentsHelper.getOutput().getMoment10()
				/ momentsHelper.getOutput().getMoment00();
		final double centerY = momentsHelper.getOutput().getMoment01()
				/ momentsHelper.getOutput().getMoment00();

		output.setCentralMoment00(momentsHelper.getOutput().getMoment00());
		output.setCentralMoment10(0);
		output.setCentralMoment01(0);
		output.setCentralMoment11(momentsHelper.getOutput().getMoment11()
				- centerX * momentsHelper.getOutput().getMoment01());

		final Cursor<? extends RealType<?>> it = input.localizingCursor();
		while (it.hasNext()) {
			it.fwd();
			final double x = it.getIntPosition(0) - centerX;
			final double y = it.getIntPosition(1) - centerY;
			final double val = it.get().getRealDouble();

			output.addToCentralMoment20(x, y, val);
			output.addToCentralMoment02(x, y, val);
			output.addToCentralMoment21(x, y, val);
			output.addToCentralMoment12(x, y, val);
			output.addToCentralMoment30(x, y, val);
			output.addToCentralMoment03(x, y, val);

		}

		return output;
	}

}
