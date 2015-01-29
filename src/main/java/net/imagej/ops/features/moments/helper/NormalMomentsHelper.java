package net.imagej.ops.features.moments.helper;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.features.moments.NormalMomentsResult;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "NormalMomentsHelper")
public class NormalMomentsHelper
		extends
		AbstractOutputFunction<IterableInterval<? extends RealType<?>>, NormalMomentsResult> {

	@Override
	public NormalMomentsResult createOutput(
			IterableInterval<? extends RealType<?>> input) {
		return new NormalMomentsResult();
	}

	@Override
	protected NormalMomentsResult safeCompute(
			IterableInterval<? extends RealType<?>> input,
			NormalMomentsResult output) {

		Cursor<? extends RealType<?>> cur = input.localizingCursor();
		while (cur.hasNext()) {
			cur.fwd();

			double x = cur.getDoublePosition(0);
			double y = cur.getDoublePosition(1);

			double val = cur.get().getRealDouble();

			output.addToMoment00(x, y, val);
			output.addToMoment01(x, y, val);
			output.addToMoment10(x, y, val);
			output.addToMoment11(x, y, val);

		}

		return output;
	}

}
