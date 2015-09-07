
package net.imagej.ops.map.neighborhood.array;

import java.util.ArrayList;
import java.util.concurrent.Future;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.map.AbstractMapComputer;
import net.imagej.ops.map.neighborhood.MapNeighborhood;
import net.imglib2.FinalInterval;
import net.imglib2.algorithm.neighborhood.CenteredRectangleShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
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
@Plugin(type = Op.class, name = Ops.Map.NAME,
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
	private CenteredRectangleShape shape;

	@Parameter(required = false)
	private OutOfBoundsFactory<I, I> oobFactory;

	@Override
	public void compute(final ArrayImg<I, ?> input, final ArrayImg<O, ?> output) {
		// all dimensions are equal as ensured in conforms() */
		final int span =
			(int) ((shape.neighborhoodsRandomAccessible(input).randomAccess().get()
				.dimension(0) - 1) / 2);

		final int numDimensions = input.numDimensions();

		// calculate intervals
		FinalInterval[] intervals;
		FinalInterval center;
		final long dim0 = input.dimension(0);
		final long max0 = dim0 - span - 1; // maximal extension of horizonally
																				// centered intervals

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
			new FinalInterval(new long[] { dim0 - span }, new long[] { span }) };
			/* center	*/
			center = new FinalInterval(new long[] { span + 1 }, new long[] { max0 });
		}
		else if (numDimensions == 2) {
			final long dim1 = input.dimension(1);
			final long max1 = dim1 - span - 1; // maximal extension of vertically
																					// centered intervals

			intervals =
				new FinalInterval[] {
					/* top */
					new FinalInterval(new long[] { 0, 0 }, new long[] { dim0, span }),
					/* bottom */
					new FinalInterval(new long[] { 0, dim1 - span }, new long[] { dim0,
						dim1 }),
					/* left */
					new FinalInterval(new long[] { 0, span + 1 },
						new long[] { span, span }),
					/* right */
					new FinalInterval(new long[] { dim0 - span, span + 1 }, new long[] {
						span, max1 }), };
			/* center */
			center =
				new FinalInterval(new long[] { span + 1, span + 1 }, new long[] { max0,
					max1 });
		}
		else {
			// numDimensions == 3, guranteed by conforms()

			final long dim1 = input.dimension(1);
			final long dim2 = input.dimension(2);
			final long max1 = dim1 - span - 1; // maximal extension of vertically
																					// centered intervals
			final long max2 = dim2 - span - 1; // maximal extension of depth centered
																					// intervals
			final long spanPlus1 = span + 1;

			intervals =
				new FinalInterval[] {
					new FinalInterval(new long[] { 0, 0 },
					/* front */
					new long[] { dim0, dim1, span }),
					/* back */
					new FinalInterval(new long[] { 0, 0, dim2 - span }, new long[] {
						dim0, dim1, dim2 }),
					/* top */
					new FinalInterval(new long[] { 0, 0, spanPlus1 }, new long[] { dim0,
						span, max2 }),
					/* bottom */
					new FinalInterval(new long[] { 0, dim1 - span, spanPlus1 },
						new long[] { dim0, dim1, max2 }),
					/* left */
					new FinalInterval(new long[] { 0, spanPlus1, spanPlus1 }, new long[] {
						span, span, max2 }),
					/* right */
					new FinalInterval(new long[] { dim0 - span, spanPlus1 }, new long[] {
						span, max1, max2 }), };
			/* center */
			center =
				new FinalInterval(new long[] { spanPlus1, spanPlus1, spanPlus1 },
					new long[] { max0, max1, max2 });
		}

		ArrayList<Future<?>> futures =
			new ArrayList<Future<?>>(2 * numDimensions + 1);

		for (final FinalInterval interval : intervals) {
			futures.add(threadService.run(new Runnable() {

				@Override
				public void run() {
					ops.run(MapNeighborhood.class, Views.interval(output, interval),
						Views.interval(input, interval), getOp(), shape, oobFactory);
				}
			}));
		}

		final FinalInterval finalCenter = center;
		futures.add(threadService.run(new Runnable() {

			@Override
			public void run() {
				ops.map(Views.interval(output, finalCenter), Views.interval(input,
					finalCenter), getOp(), span);
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
		if (!(getInput().numDimensions() > 0 || getInput().numDimensions() <= 3)) {
			return false;
		}

		/* all dimensions of the neighborhood have to be equal to be able to use
		   MapNeighborhoodNativeType */
		Neighborhood<I> neighborhood =
			shape.neighborhoodsRandomAccessible(getInput()).randomAccess().get();
		final long dimension0 = neighborhood.dimension(0);

		for (int i = 1; i < neighborhood.numDimensions(); ++i) {
			if (dimension0 != neighborhood.dimension(i)) {
				return false;
			}
		}

		return true;
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
