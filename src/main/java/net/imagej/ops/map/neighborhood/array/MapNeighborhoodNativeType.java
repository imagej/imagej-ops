
package net.imagej.ops.map.neighborhood.array;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.map.neighborhood.AbstractMapNeighborhood;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.type.NativeType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Optimized neighborhood map implementation for 1D/2D/3D {@link Img}. This
 * implementation uses access to the underlying types, which bypasses
 * OutOfBounds checks, though. This means that pixels which are out of bounds
 * are not considered as belonging to the neighborhood of a pixel. This can
 * change results of averages over a neighborhood in comparison to using an out
 * of bounds strategy which "creates" pixels in the neighborhood where there are
 * none after the bounds of the image.
 * 
 * @author Jonathan Hale
 * @param <I> Input {@link NativeType}
 * @param <O> Ouput {@link NativeType}
 * @see MapNeighborhoodWithCenterNativeType
 */
@Plugin(type = Op.class, name = Ops.Map.NAME, priority = Priority.LOW_PRIORITY +
	10)
public class MapNeighborhoodNativeType<I extends NativeType<I>, O extends NativeType<O>>
	extends
	AbstractMapNeighborhood<Iterable<I>, O, ArrayImg<I, ?>, ArrayImg<O, ?>, RectangleShape, UnaryComputerOp<Iterable<I>, O>>
	implements Contingent
{

	/**
	 * Interval for input and output images to constrain interation to.
	 */
	@Parameter(required = false)
	private Interval interval;

	@Override
	public void compute2(final ArrayImg<I, ?> input, final RectangleShape shape,
		final ArrayImg<O, ?> output)
	{
		final I in = input.firstElement();
		final O out = output.firstElement();

		final int width = (int) input.dimension(0);
		final int height = (input.numDimensions() > 1) ? (int) input.dimension(1)
			: 1;
		final int depth = (input.numDimensions() > 2) ? (int) input.dimension(2)
			: 1;

		final Interval i = (interval == null) ? input : interval;

		final int minX = (int) i.min(0);
		final int minY = (i.numDimensions() > 1) ? (int) i.min(1) : 0;
		final int minZ = (i.numDimensions() > 2) ? (int) i.min(2) : 0;

		final int maxX = (int) i.max(0);
		final int maxY = (i.numDimensions() > 1) ? (int) i.max(1) : 0;
		final int maxZ = (i.numDimensions() > 2) ? (int) i.max(2) : 0;

		final int skipX = width - (maxX - minX + 1);
		final int skipY = width * (height - (maxY - minY + 1));

		final UnaryComputerOp<Iterable<I>, O> op = getOp();

		int index = minX + minY * width + minZ * height * width;
		in.updateIndex(index);
		out.updateIndex(index);

		for (int z = minZ; z <= maxZ; ++z) {
			for (int y = minY; y <= maxY; ++y) {
				for (int x = minX; x <= maxX; ++x) {
					// save the current index, since it will be changed by the
					// NeighborhoodIterable. Increment to save doing that later.
					index = in.getIndex() + 1;

					final Iterable<I> neighborhood = new NeighborhoodIterableNativeType<>(
						in, x, y, z, width, height, depth, shape.getSpan());

					op.compute1(neighborhood, out);

					in.updateIndex(index);
					out.incIndex();
				}
				in.incIndex(skipX);
				out.incIndex(skipX);
			}
			in.incIndex(skipY);
			out.incIndex(skipY);
		}
	}

	/**
	 * @return The interval which constraints iteration or <code>null</code>, if
	 *         none is set.
	 */
	public Interval getInterval() {
		return interval;
	}

	/**
	 * Set the interval which constraints iteration.
	 */
	public void setInterval(Interval i) {
		interval = i;
	}

	/**
	 * Convenience method to set the interval which constraints iteration.
	 * 
	 * @see #setInterval(Interval)
	 */
	public void setInterval(long[] min, long[] max) {
		interval = new FinalInterval(min, max);
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() > 0 && in().numDimensions() <= 3 && !in2()
			.isSkippingCenter();
	}

}
