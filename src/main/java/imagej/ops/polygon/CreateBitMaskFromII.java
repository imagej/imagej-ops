
package imagej.ops.polygon;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = CreateBitMask.NAME, label = CreateBitMask.LABEL)
public class CreateBitMaskFromII
	extends
	AbstractFunction<IterableInterval<RealType<?>>, RandomAccessibleInterval<BitType>>

{

	@Override
	public RandomAccessibleInterval<BitType> compute(
		final IterableInterval<RealType<?>> input,
		RandomAccessibleInterval<BitType> output)
	{
		// TODO: This is a case where we can't know which container type we should
		// use. This is ugly! ;-)
		if (output == null) output =
			new ArrayImgFactory<BitType>().create(input, new BitType());

		final RandomAccess<BitType> maskRA = output.randomAccess();

		final Cursor<? extends RealType<?>> cur = input.localizingCursor();
		while (cur.hasNext()) {
			cur.fwd();
			for (int d = 0; d < cur.numDimensions(); d++) {
				maskRA.setPosition(cur.getLongPosition(d) - input.min(d), d);
			}

			maskRA.get().set(true);
		}
		return output;
	}
}
