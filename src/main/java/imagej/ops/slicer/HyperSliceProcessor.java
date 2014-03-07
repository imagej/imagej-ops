package imagej.ops.slicer;

import imagej.Cancelable;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import imagej.ops.UnaryFunctionTask;

import java.util.concurrent.Future;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

public class HyperSliceProcessor<I extends RandomAccessibleInterval<?>, O extends RandomAccessibleInterval<?>>
		implements Op, Cancelable {

	@Parameter
	private HyperSlicingService hyperSlicingService;

	@Parameter
	private StatusService statusService;

	@Parameter
	private ThreadService threadService;

	@Parameter
	private int[] axes;

	@Parameter
	private I in;

	@Parameter
	private O out;

	@Parameter
	private UnaryFunction<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>> op;

	// reason for cancellation if internally canceled (exception for example)
	protected String cancelReason;

	@Override
	public void run() {

		final Interval[] ins = hyperSlicingService.resolveIntervals(axes, in);

		final Interval[] outs = hyperSlicingService.resolveIntervals(axes, out);

		assert (ins.length == outs.length);

		final Future<?>[] futures = new Future<?>[ins.length];

		for (int i = 0; i < ins.length; i++) {
			futures[i] = threadService.run(createUnaryFunctionTask(ins[i],
					outs[i], op));

			statusService.showStatus("Running " + op.toString()
					+ " on interval " + ins[i] + outs[i]);
		}

		for (final Future<?> f : futures) {
			if (f.isCancelled()) {
				return;
			}

			try {
				f.get();
			} catch (final Exception e) {
				cancelReason = e.getCause().getMessage();
			}

			if (isCanceled()) {
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private UnaryFunctionTask<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>> createUnaryFunctionTask(
			final Interval inInterval,
			final Interval outInterval,
			final UnaryFunction<? extends RandomAccessibleInterval<?>, ? extends RandomAccessibleInterval<?>> op) {

		return new UnaryFunctionTask<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>>(
				(UnaryFunction<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>>) op
						.copy(),
				hyperSlicingService.hyperSlice(in, inInterval),
				hyperSlicingService.hyperSlice(out, outInterval));
	}

	@Override
	public String getCancelReason() {
		return cancelReason;
	}

	@Override
	public boolean isCanceled() {
		return cancelReason != null;
	}

}
