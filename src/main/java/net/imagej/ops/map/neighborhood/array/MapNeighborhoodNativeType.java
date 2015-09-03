
package net.imagej.ops.map.neighborhood.array;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.map.AbstractMapComputer;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.type.NativeType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Optimized neighborhood map implementation for {@link Img}. This
 * implementation uses access to the underlying types, which bypasses
 * OutOfBounds checks though. This means that pixels which are out of bounds are
 * not considered as belonging to the neighborhood of a pixel. This can change
 * results of averages over a neighborhood in comparison to using an out of
 * bounds strategy which "creates" pixels in the neighborhood where there are
 * non after the bounds of the image.
 * 
 * @author Jonathan Hale
 * @param <I> Input {@link NativeType}
 * @param <O> Ouput {@link NativeType}
 */
@Plugin(type = Op.class, name = Ops.Map.NAME,
	priority = Priority.LOW_PRIORITY + 10)
public class MapNeighborhoodNativeType<I extends NativeType<I>, O extends NativeType<O>>
	extends AbstractMapComputer<Iterable<I>, O, ArrayImg<I, ?>, ArrayImg<O, ?>>
	implements Contingent
{

	@Parameter
	private int span;

	@Override
	public void compute(final ArrayImg<I, ?> input, final ArrayImg<O, ?> output) {
		final I in = input.firstElement();
		final O out = output.firstElement();

		final int width = (int) input.dimension(0);
		final int height = (int) input.dimension(1);

		final ComputerOp<Iterable<I>, O> op = getOp();

		int index;

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				// save the current index, since it will be changed by the
				// NeighborhoodIterable. Increment to save doing that later.
				index = in.getIndex() + 1;

				final Iterable<I> neighborhood =
					new NeighborhoodIterableNativeType<I>(in, x, y, width, height, span);

				op.compute(neighborhood, out);

				in.updateIndex(index);
				out.incIndex();
			}
		}
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() == 2;
	}

}
