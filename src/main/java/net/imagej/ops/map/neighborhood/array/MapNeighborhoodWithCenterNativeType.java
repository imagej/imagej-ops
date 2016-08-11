
package net.imagej.ops.map.neighborhood.array;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.AbstractMapNeighborhood;
import net.imagej.ops.map.neighborhood.CenterAwareComputerOp;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.type.NativeType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Optimized center aware neighborhood map implementation for 1D/2D/3D
 * {@link Img}. This implementation uses access to the underlying types, which
 * bypasses OutOfBounds checks, though. This means that pixels which are out of
 * bounds are not considered as belonging to the neighborhood of a pixel. This
 * can change results of averages over a neighborhood in comparison to using an
 * out of bounds strategy which "creates" pixels in the neighborhood where there
 * are none after the bounds of the image.
 * 
 * @author Jonathan Hale
 * @param <I> Input {@link NativeType}
 * @param <O> Ouput {@link NativeType}
 * @see MapNeighborhoodNativeType
 */
@Plugin(type = Op.class, name = Ops.Map.NAME, priority = Priority.LOW_PRIORITY +
	22)
public class MapNeighborhoodWithCenterNativeType<I extends NativeType<I>, O extends NativeType<O>>
	extends
	AbstractMapNeighborhood<I, O, ArrayImg<I, ?>, ArrayImg<O, ?>, RectangleShape, CenterAwareComputerOp<I, O>>
	implements Contingent
{

	@Override
	public void compute2(final ArrayImg<I, ?> input, final RectangleShape shape,
		final ArrayImg<O, ?> output)
	{
		final I in = input.firstElement();
		final O out = output.firstElement();

		final int width = (int) input.dimension(0);
		final int height = (int) input.dimension(1);
		final int depth = Math.max(1, (int) input.dimension(2));

		final CenterAwareComputerOp<I, O> op = getOp();

		int index;

		for (int z = 0; z < depth; ++z) {
			for (int y = 0; y < height; ++y) {
				for (int x = 0; x < width; ++x) {
					// save the current index, since it will be changed by the
					// NeighborhoodIterable. Increment to save doing that later.
					index = in.getIndex() + 1;

					// copy for center pixel access, since it will get changed, again, by
					// NeighborhoodIterable.
					final I center = in.copy();

					final Iterable<I> neighborhood = new NeighborhoodIterableNativeType<>(
						in, x, y, z, width, height, depth, shape.getSpan());

					op.compute2(neighborhood, center, out);

					in.updateIndex(index);
					out.incIndex();
				}
			}
		}
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() > 0 && in().numDimensions() <= 3 && !in2()
			.isSkippingCenter();
	}

}
