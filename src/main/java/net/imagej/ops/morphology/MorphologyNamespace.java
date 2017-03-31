/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.morphology;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.IterableInterval;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The morphology namespace contains morphology operations.
 * 
 * @author Leon Yang
 */
@Plugin(type = Namespace.class)
public class MorphologyNamespace extends AbstractNamespace {

	// -- Morphology namespace ops --

	@OpMethod(op = net.imagej.ops.morphology.blackTopHat.ListBlackTopHat.class)
	public <T extends RealType<T>> IterableInterval<T> blackTopHat(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.BlackTopHat.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.blackTopHat.ListBlackTopHat.class)
	public <T extends RealType<T>> IterableInterval<T> blackTopHat(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.BlackTopHat.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.close.ListClose.class)
	public <T extends RealType<T>> IterableInterval<T> close(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Close.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.close.ListClose.class)
	public <T extends RealType<T>> IterableInterval<T> close(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Close.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.DefaultDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final RandomAccessibleInterval<T> in1, final Shape in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.DefaultDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.DefaultDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2, final boolean isFull)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, out, in1, in2, isFull);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.DefaultDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2, final boolean isFull,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> f)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, out, in1, in2, isFull, f);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.ListDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.ListDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.dilate.ListDilate.class)
	public <T extends RealType<T>> IterableInterval<T> dilate(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2, final boolean isFull)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Dilate.class, out, in1, in2, isFull);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.DefaultErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final RandomAccessibleInterval<T> in1, final Shape in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.DefaultErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.DefaultErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2, final boolean isFull)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, out, in1, in2, isFull);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.DefaultErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final Shape in2, final boolean isFull,
		final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> f)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, out, in1, in2, isFull, f);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.ListErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.ListErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.erode.ListErode.class)
	public <T extends RealType<T>> IterableInterval<T> erode(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2, final boolean isFull)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Erode.class, out, in1, in2, isFull);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.open.ListOpen.class)
	public <T extends RealType<T>> IterableInterval<T> open(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Open.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.open.ListOpen.class)
	public <T extends RealType<T>> IterableInterval<T> open(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.Open.class, out, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.outline.Outline.class)
	public <B extends BooleanType<B>> RandomAccessibleInterval<BitType> outline(
		final RandomAccessibleInterval<BitType> out,
		final RandomAccessibleInterval<B> in, final Boolean excludeEdges)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(
				net.imagej.ops.Ops.Morphology.Outline.class, out, in, excludeEdges);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.outline.Outline.class)
	public <B extends BooleanType<B>> RandomAccessibleInterval<BitType> outline(
		final RandomAccessibleInterval<B> in, final Boolean excludeEdges)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(
				net.imagej.ops.Ops.Morphology.Outline.class, in, excludeEdges);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.topHat.ListTopHat.class)
	public <T extends RealType<T>> IterableInterval<T> topHat(
		final RandomAccessibleInterval<T> in1, final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.TopHat.class, in1, in2);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.topHat.ListTopHat.class)
	public <T extends RealType<T>> IterableInterval<T> topHat(
		final IterableInterval<T> out, final RandomAccessibleInterval<T> in1,
		final List<Shape> in2)
	{
		@SuppressWarnings("unchecked")
		final IterableInterval<T> result = (IterableInterval<T>) ops().run(
			net.imagej.ops.Ops.Morphology.TopHat.class, out, in1, in2);
		return result;
	}

	// -- Named methods --
	@OpMethod(op = net.imagej.ops.morphology.extractHoles.DefaultExtractHolesComputer.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> extractHoles(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.extractHoles.DefaultExtractHolesComputer.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.extractHoles.DefaultExtractHolesComputer.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> extractHoles(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in, final Shape structElement) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.extractHoles.DefaultExtractHolesComputer.class, out, in, structElement);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.extractHoles.DefaultExtractHolesFunction.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> extractHoles(final RandomAccessibleInterval<T> in) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.extractHoles.DefaultExtractHolesFunction.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.extractHoles.DefaultExtractHolesFunction.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> extractHoles(final RandomAccessibleInterval<T> in,
			final Shape structElement) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.extractHoles.DefaultExtractHolesFunction.class, in, structElement);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> fillHoles(final RandomAccessibleInterval<T> in) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> fillHoles(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class)
	public <T extends BooleanType<T>> RandomAccessibleInterval<T> fillHoles(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in, final Shape structElement) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result = (RandomAccessibleInterval<T>) ops()
				.run(net.imagej.ops.morphology.fillHoles.DefaultFillHoles.class, out, in, structElement);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.floodFill.DefaultFloodFill.class)
	public <T extends Type<T> & Comparable<T>> RandomAccessibleInterval<T>
		floodFill(final RandomAccessibleInterval<T> out,
			final RandomAccessibleInterval<T> in, final Localizable startPos,
			final Shape structElement)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.morphology.floodFill.DefaultFloodFill.class, out, in, startPos,
				structElement);
		return result;
	}

	@OpMethod(op = net.imagej.ops.morphology.floodFill.DefaultFloodFill.class)
	public <T extends Type<T> & Comparable<T>> RandomAccessibleInterval<T>
		floodFill(final RandomAccessibleInterval<T> in1, final Localizable in2,
			final Shape structElement)
	{
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<T> result =
			(RandomAccessibleInterval<T>) ops().run(
				net.imagej.ops.morphology.floodFill.DefaultFloodFill.class, in1, in2,
				structElement);
		return result;
	}

	@Override
	public String getName() {
		return "morphology";
	}

}
