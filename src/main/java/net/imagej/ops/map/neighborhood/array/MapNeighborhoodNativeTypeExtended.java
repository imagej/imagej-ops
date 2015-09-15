
package net.imagej.ops.map.neighborhood.array;

import java.util.ArrayList;
import java.util.concurrent.Future;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.map.AbstractMapComputer;
import net.imagej.ops.map.neighborhood.MapNeighborhood;
import net.imglib2.FinalInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.view.Views;

import org.scijava.Cancelable;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Optimized implementation of MapNeighborhood which uses
 * {@link MapNeighborhoodNativeType} for a center interval which does not
 * require out of bounds checks and {@link MapNeighborhood} for external
 * intervals.
 * 
 * @author Jonathan Hale
 * @param <I> Input {@link NativeType}
 * @param <O> Ouput {@link NativeType}
 * @see MapNeighborhoodWithCenterNativeType
 * @see MapNeighborhood
 */
@Plugin(type = net.imagej.ops.Op.class, name = Ops.Map.NAME,
	priority = Priority.LOW_PRIORITY + 10)
public class MapNeighborhoodNativeTypeExtended<I extends NativeType<I>, O extends NativeType<O>>
	extends AbstractMapComputer<Iterable<I>, O, ArrayImg<I, ?>, ArrayImg<O, ?>>
	implements Contingent, Cancelable
{

	@Parameter
	protected ThreadService threadService;

	@Parameter
	protected OpService ops;

	@Parameter
	protected LogService log;

	@Parameter
	private RectangleShape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, I> oobFactory;

	@Override
	public void compute(final ArrayImg<I, ?> input, final ArrayImg<O, ?> output) {
		// all dimensions are equal as ensured in conforms() */
		final int span = shape.getSpan();

		final int numDimensions = input.numDimensions();

		// calculate intervals
		FinalInterval[] intervals;
		FinalInterval center;
		final long dim0 = input.dimension(0);
		final long max0 = input.max(0);
		final long maxSafe0 = max0 - span; // maximal extension of horizontally
																				// centered intervals
		final long spanPlus1 = span + 1;
		/* Note about ordering of intervals:
		 * Since done parallel, the idea is to queue work by decreasing size.
		 * The center interval is usually the biggest, but can be best optimized.
		 * Following: front/back, top/bottom, left/right. (Depending on dimensions, 
		 * some might not be available.)
		 * 
		 * The intervals were chosen intentionally to optimize access to storage,
		 * which is expected to be in order of dimensions (0 first, then 1, ...).
		 * 
		 * The order for queuing the tasks is: Unoptimized intervals in order of size, 
		 * then optimized center interval
		 */
		/* build intervals */
		if (numDimensions == 1) {
			intervals = new FinalInterval[] {
			/* left */
			new FinalInterval(new long[] { 0 }, new long[] { span }),
			/* right */
			new FinalInterval(new long[] { dim0 - span }, new long[] { max0 }) };
			/* center	*/
			center =
				new FinalInterval(new long[] { spanPlus1 }, new long[] { maxSafe0 });
		}
		else if (numDimensions == 2) {
			final long dim1 = input.dimension(1);
			final long max1 = input.max(1);
			final long maxSafe1 = max1 - span; // maximal extension of vertically
																					// centered intervals

			intervals =
				new FinalInterval[] {
					/* top */
					new FinalInterval(new long[] { 0, 0 }, new long[] { max0, span }),
					/* bottom */
					new FinalInterval(new long[] { 0, dim1 - span }, new long[] { max0,
						max1 }),
					/* left */
					new FinalInterval(new long[] { 0, spanPlus1 }, new long[] { span,
						max1 - span }),
					/* right */
					new FinalInterval(new long[] { dim0 - span, spanPlus1 }, new long[] {
						max0, maxSafe1 }) };
			/* center */
			center =
				new FinalInterval(new long[] { spanPlus1, spanPlus1 }, new long[] {
					maxSafe0, maxSafe1 });
		}
		else {
			// numDimensions == 3, guaranteed by conforms()

			final long dim1 = input.dimension(1);
			final long dim2 = input.dimension(2);
			final long max1 = input.max(1);
			final long max2 = input.max(2);
			final long maxSafe1 = max1 - span; // maximal extension of vertically
																					// centered intervals
			final long maxSafe2 = max2 - span; // maximal extension of depth
																					// centered intervals
			intervals =
				new FinalInterval[] {
					/* front */
					new FinalInterval(new long[] { 0, 0, 0 }, new long[] { max0, max1,
						span }),
					/* back */
					new FinalInterval(new long[] { 0, 0, dim2 - span }, new long[] {
						max0, max1, max2 }),
					/* top */
					new FinalInterval(new long[] { 0, 0, spanPlus1 }, new long[] { max0,
						span, maxSafe2 }),
					/* bottom */
					new FinalInterval(new long[] { 0, dim1 - span, spanPlus1 },
						new long[] { max0, max1, maxSafe2 }),
					/* left */
					new FinalInterval(new long[] { 0, spanPlus1, spanPlus1 }, new long[] {
						span, maxSafe1, maxSafe2 }),
					/* right */
					new FinalInterval(new long[] { dim0 - span, spanPlus1, spanPlus1 },
						new long[] { max0, maxSafe1, maxSafe2 }) };
			/* center */
			center =
				new FinalInterval(new long[] { spanPlus1, spanPlus1, spanPlus1 },
					new long[] { maxSafe0, maxSafe1, maxSafe2 });
		}

		ArrayList<Future<?>> futures =
			new ArrayList<Future<?>>(2 * numDimensions + 1);

		for (final FinalInterval interval : intervals) {
			futures.add(threadService.run(new Runnable() {

				@Override
				public void run() {
					if (oobFactory == null) {
						ops.run(MapNeighborhood.class, Views.interval(output, interval),
							Views.interval(input, interval), getOp(), shape);
					}
					else {
						ops.run(MapNeighborhood.class, Views.interval(output, interval),
							Views.interval(input, interval), getOp(), shape, oobFactory);
					}
				}
			}));

		}

		final FinalInterval finalCenter = center;
		futures.add(threadService.run(new Runnable() {

			@Override
			public void run() {
				ops.run(MapNeighborhoodNativeType.class, output, input, getOp(), span,
					finalCenter);
			}
		}));

		// wait for tasks to complete
		for (final Future<?> future : futures) {
			try {
				if (isCanceled()) {
					break;
				}
				future.get();
			}
			catch (final Exception e) {
				log.error(e);
				cancel(e.getMessage());
				break;
			}
		}
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() > 0 && getInput().numDimensions() <= 3 &&
			!shape.isSkippingCenter();
	}

	// --- Cancelable methods ---

	private String cancelReason = null;
	private boolean canceled;

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void cancel(String reason) {
		cancelReason = reason;
		canceled = true;
	}

	@Override
	public String getCancelReason() {
		return cancelReason;
	}
}
