/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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
package net.imagej.ops.topology;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * The topology namespace contains operations for calculating topology characteristics
 *
 * @author Richard Domander (Royal Veterinary College, London)
 */
@Plugin(type = Namespace.class)
public class TopologyNamespace extends AbstractNamespace {

    @Override
    public String getName() {
        return "topology";
    }

	@OpMethod(op = net.imagej.ops.topology.BoxCount.class)
	public <B extends BooleanType<B>> List boxCount(
		final RandomAccessibleInterval<B> in)
	{
		return (List) ops().run(net.imagej.ops.Ops.Topology.BoxCount.class, in);
	}

	@OpMethod(op = net.imagej.ops.topology.BoxCount.class)
	public <B extends BooleanType<B>> List boxCount(
		final RandomAccessibleInterval<B> in, final Long maxSize)
	{
		return (List) ops().run(net.imagej.ops.Ops.Topology.BoxCount.class, in,
			maxSize);
	}

	@OpMethod(op = net.imagej.ops.topology.BoxCount.class)
	public <B extends BooleanType<B>> List boxCount(
		final RandomAccessibleInterval<B> in, final Long maxSize,
		final Long minSize)
	{
		return (List) ops().run(net.imagej.ops.Ops.Topology.BoxCount.class, in,
			maxSize, minSize);
	}

	@OpMethod(op = net.imagej.ops.topology.BoxCount.class)
	public <B extends BooleanType<B>> List boxCount(
		final RandomAccessibleInterval<B> in, final Long maxSize,
		final Long minSize, final Double scaling)
	{
		return (List) ops().run(net.imagej.ops.Ops.Topology.BoxCount.class, in,
			maxSize, minSize, scaling);
	}

	@OpMethod(op = net.imagej.ops.topology.BoxCount.class)
	public <B extends BooleanType<B>> List boxCount(
		final RandomAccessibleInterval<B> in, final Long maxSize,
		final Long minSize, final Double scaling, final Long gridMoves)
	{
		return (List) ops().run(net.imagej.ops.Ops.Topology.BoxCount.class, in,
			maxSize, minSize, scaling, gridMoves);
	}

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26N.class)
    public <B extends BooleanType<B>> DoubleType eulerCharacteristic26N(final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26N.class, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26N.class)
    public <B extends BooleanType<B>> DoubleType eulerCharacteristic26N(final DoubleType out,
            final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26N.class, out, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26NFloating.class)
    public <B extends BooleanType<B>> DoubleType eulerCharacteristic26NFloating(final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26NFloating.class, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCharacteristic26NFloating.class)
    public <B extends BooleanType<B>> DoubleType eulerCharacteristic26NFloating(final DoubleType out,
            final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCharacteristic26NFloating.class, out, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCorrection.class)
    public <B extends BooleanType<B>> DoubleType eulerCorrection(final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCorrection.class, in);
    }

    @OpMethod(op = net.imagej.ops.topology.eulerCharacteristic.EulerCorrection.class)
    public <B extends BooleanType<B>> DoubleType eulerCorrection(final DoubleType out,
            final RandomAccessibleInterval<B> in) {
        return (DoubleType) ops().run(net.imagej.ops.Ops.Topology.EulerCorrection.class, out, in);
    }


}
