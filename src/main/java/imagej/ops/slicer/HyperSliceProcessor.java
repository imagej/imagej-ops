/*
 * #%L
 * A framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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

        final Interval[] ins = HyperSliceUtils.resolveIntervals(axes, in);

        final Interval[] outs = HyperSliceUtils.resolveIntervals(axes, out);

        assert (ins.length == outs.length);

        final Future<?>[] futures = new Future<?>[ins.length];

        for (int i = 0; i < ins.length; i++) {
            futures[i] =
                    threadService.run(createUnaryFunctionTask(ins[i], outs[i],
                            op));

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
                (UnaryFunction<RandomAccessibleInterval<?>, RandomAccessibleInterval<?>>)op
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
