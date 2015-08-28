/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops.map.neighborhood;

import java.util.Iterator;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPositionable;
import net.imglib2.Sampler;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates a {@link CenterAwareComputerOp} for each {@link Neighborhood} on
 * the input {@link RandomAccessibleInterval} and sets the value of the
 * corresponding pixel on the output {@link RandomAccessibleInterval}. Similar
 * to {@link MapNeighborhood}, but passes the center pixel to the op aswell.
 * 
 * @author Jonathan Hale
 * @see OpService#map(RandomAccessibleInterval, RandomAccessibleInterval,
 *      CenterAwareComputerOp, Shape)
 * @see CenterAwareComputerOp
 */
@Plugin(type = Op.class, name = Ops.Map.NAME,
	priority = Priority.LOW_PRIORITY + 1)
public class MapNeighborhoodWithCenter<I, O>
	extends
	AbstractMapCenterAwareComputer<I, O, RandomAccessibleInterval<I>, RandomAccessibleInterval<O>>
{

	@Parameter
	private Shape shape;

	@Parameter
	private OpService ops;

	@Override
	public void compute(final RandomAccessibleInterval<I> input,
		final RandomAccessibleInterval<O> output)
	{
		final IterableInterval<Neighborhood<I>> neighborhoods =
			shape.neighborhoods(input);

		final CenterAwareComputerOp<I, O> op = getOp();

		ops.map(Views.iterable(output), new NeighborhoodWithCenterIterableInterval(
			neighborhoods, input), op);
	}

	/**
	 * IterableInterval wrapping a {@link IterableInterval}<{@link Neighborhood}>
	 * and a {@link RandomAccessibleInterval}. Depending on the iterator order of
	 * both, a matching cursor will be chosen appropriately.
	 * 
	 * @author Jonathan Hale (University of Konstanz)
	 */
	class NeighborhoodWithCenterIterableInterval implements
		IterableInterval<Pair<I, Iterable<I>>>
	{

		private final IterableInterval<Neighborhood<I>> neighborhoods;
		private final RandomAccessibleInterval<I> input;

		public NeighborhoodWithCenterIterableInterval(
			IterableInterval<Neighborhood<I>> neighborhoods,
			RandomAccessibleInterval<I> input)
		{
			this.neighborhoods = neighborhoods;
			this.input = input;
		}

		@Override
		public Iterator<Pair<I, Iterable<I>>> iterator() {
			return cursor();
		}

		@Override
		public long size() {
			return neighborhoods.size();
		}

		@Override
		public Pair<I, Iterable<I>> firstElement() {
			return cursor().next();
		}

		@Override
		public Object iterationOrder() {
			return neighborhoods.iterationOrder();
		}

		@Override
		public double realMin(int d) {
			return neighborhoods.realMin(d);
		}

		@Override
		public void realMin(double[] min) {
			neighborhoods.realMin(min);
		}

		@Override
		public void realMin(RealPositionable min) {
			neighborhoods.realMin(min);
		}

		@Override
		public double realMax(int d) {
			return neighborhoods.realMax(d);
		}

		@Override
		public void realMax(double[] max) {
			neighborhoods.realMax(max);
		}

		@Override
		public void realMax(RealPositionable max) {
			neighborhoods.realMax(max);
		}

		@Override
		public int numDimensions() {
			return neighborhoods.numDimensions();
		}

		@Override
		public long min(int d) {
			return neighborhoods.min(d);
		}

		@Override
		public void min(long[] min) {
			neighborhoods.min(min);
		}

		@Override
		public void min(Positionable min) {
			neighborhoods.min(min);
		}

		@Override
		public long max(int d) {
			return neighborhoods.max(d);
		}

		@Override
		public void max(long[] max) {
			neighborhoods.max(max);
		}

		@Override
		public void max(Positionable max) {
			neighborhoods.max(max);
		}

		@Override
		public void dimensions(long[] dimensions) {
			neighborhoods.dimensions(dimensions);
		}

		@Override
		public long dimension(int d) {
			return neighborhoods.dimension(d);
		}

		@Override
		public Cursor<Pair<I, Iterable<I>>> cursor() {
			return localizingCursor();
		}

		@Override
		public Cursor<Pair<I, Iterable<I>>> localizingCursor() {
			final IterableInterval<I> inputIterable = Views.iterable(input);

			if (inputIterable.iterationOrder().equals(neighborhoods.iterationOrder()))
			{
				// optimizable through cursor use.
				return new NeighborhoodWithCenterCursorII(neighborhoods, inputIterable);
			}

			return new NeighborhoodWithCenterCursorRA(neighborhoods, input);
		}
	}

	/**
	 * Abstract implementation for NeighborhoodWithCenterCursors. Contains the
	 * common implementations for methods of the Cursors specific for
	 * {@link RandomAccessibleInterval} and {@link IterableInterval}.
	 * 
	 * @author Jonathan Hale (University of Konstanz)
	 */
	abstract class AbstractNeighborhoodWithCenterCursor implements
		Cursor<Pair<I, Iterable<I>>>
	{

		protected final Cursor<Neighborhood<I>> cNeigh;

		public AbstractNeighborhoodWithCenterCursor(
			IterableInterval<Neighborhood<I>> neighborhoods)
		{
			cNeigh = neighborhoods.localizingCursor();
		}

		public AbstractNeighborhoodWithCenterCursor(
			AbstractNeighborhoodWithCenterCursor c)
		{
			cNeigh = c.cNeigh.copyCursor();
		}

		@Override
		public boolean hasNext() {
			return cNeigh.hasNext();
		}

		@Override
		public Pair<I, Iterable<I>> next() {
			fwd();
			return get();
		}

		@Override
		public void localize(float[] position) {
			cNeigh.localize(position);
		}

		@Override
		public void localize(double[] position) {
			cNeigh.localize(position);
		}

		@Override
		public float getFloatPosition(int d) {
			return cNeigh.getFloatPosition(d);
		}

		@Override
		public double getDoublePosition(int d) {
			return cNeigh.getDoublePosition(d);
		}

		@Override
		public int numDimensions() {
			return cNeigh.numDimensions();
		}

		@Override
		public Sampler<Pair<I, Iterable<I>>> copy() {
			return copyCursor();
		}

		@Override
		public void jumpFwd(long steps) {
			cNeigh.jumpFwd(steps);
		}

		@Override
		public void fwd() {
			cNeigh.fwd();
		}

		@Override
		public void reset() {
			cNeigh.reset();
		}

		@Override
		public void localize(int[] position) {
			cNeigh.localize(position);
		}

		@Override
		public void localize(long[] position) {
			cNeigh.localize(position);
		}

		@Override
		public int getIntPosition(int d) {
			return cNeigh.getIntPosition(d);
		}

		@Override
		public long getLongPosition(int d) {
			return cNeigh.getLongPosition(d);
		}

		@Override
		public void remove() {
			// NB: no action.
		}
	}

	/**
	 * Cursor implementation for a {@link RandomAccessibleInterval} input. Should
	 * be used when iteration order of input and output are not equal.
	 * 
	 * @author Jonathan Hale (University of Konstanz)
	 */
	class NeighborhoodWithCenterCursorRA extends
		AbstractNeighborhoodWithCenterCursor
	{

		private final RandomAccess<I> raIn;

		public NeighborhoodWithCenterCursorRA(
			IterableInterval<Neighborhood<I>> neighborhoods,
			RandomAccessibleInterval<I> input)
		{
			super(neighborhoods);
			raIn = input.randomAccess();
		}

		public NeighborhoodWithCenterCursorRA(NeighborhoodWithCenterCursorRA c) {
			super(c);
			raIn = c.raIn.copyRandomAccess();
		}

		@Override
		public final Pair<I, Iterable<I>> get() {
			raIn.setPosition(cNeigh);
			return new ValuePair<I, Iterable<I>>(raIn.get(), cNeigh.get());
		}

		@Override
		public Cursor<Pair<I, Iterable<I>>> copyCursor() {
			return new NeighborhoodWithCenterCursorRA(this);
		}
	}

	/**
	 * Cursor implementation for a {@link IterableInterval} input. Should only be
	 * used when iteration order of input and output are equal.
	 * 
	 * @author Jonathan Hale (University of Konstanz)
	 */
	class NeighborhoodWithCenterCursorII extends
		AbstractNeighborhoodWithCenterCursor
	{

		private final Cursor<I> cIn;

		public NeighborhoodWithCenterCursorII(
			IterableInterval<Neighborhood<I>> neighborhoods, IterableInterval<I> input)
		{
			super(neighborhoods);
			if (!input.iterationOrder().equals(neighborhoods.iterationOrder())) {
				throw new IllegalArgumentException(
					"Iteration order of neighborhood InterableInterval and input InterableInterval are not equal!");
			}

			cIn = input.cursor();
		}

		public NeighborhoodWithCenterCursorII(NeighborhoodWithCenterCursorII c) {
			super(c);
			cIn = c.cIn.copyCursor();
		}

		@Override
		public final void fwd() {
			super.fwd();
			cIn.fwd();
		}

		@Override
		public void jumpFwd(long steps) {
			super.jumpFwd(steps);
			cIn.jumpFwd(steps);
		}

		@Override
		public final Pair<I, Iterable<I>> get() {
			return new ValuePair<I, Iterable<I>>(cIn.get(), cNeigh.get());
		}

		@Override
		public Cursor<Pair<I, Iterable<I>>> copyCursor() {
			return new NeighborhoodWithCenterCursorII(this);
		}
	}

}
