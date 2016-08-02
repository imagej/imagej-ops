/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.morphology.floodFill;

import java.util.Arrays;
import java.util.LinkedList;

import net.imagej.ops.Ops;
import net.imagej.ops.copy.CopyRAI;
import net.imagej.ops.create.img.CreateImgFromInterval;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractBinaryHybridCF;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.labeling.ConnectedComponents.StructuringElement;
import net.imglib2.type.Type;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn (University of Konstanz)
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Morphology.FloodFill.class)
public class DefaultFloodFill<T extends Type<T> & Comparable<T>> extends
	AbstractBinaryHybridCF<RandomAccessibleInterval<T>, Localizable, RandomAccessibleInterval<T>>
	implements Ops.Morphology.FloodFill
{

	@Parameter()
	private StructuringElement structElement = StructuringElement.EIGHT_CONNECTED;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> copyComp;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createFunc;

	@Override
	public void initialize() {
		copyComp = RAIs.computer(ops(), CopyRAI.class, in());
		createFunc = RAIs.function(ops(), CreateImgFromInterval.class, in());
	}

	@Override
	public void compute2(RandomAccessibleInterval<T> op0, Localizable loc,
		RandomAccessibleInterval<T> r)
	{
		copyComp.compute1(op0, r);

		long[] posAsArray = new long[loc.numDimensions()];
		loc.localize(posAsArray);

		final RandomAccess<T> rc = r.randomAccess();
		final RandomAccess<T> op0c = op0.randomAccess();
		op0c.setPosition(loc);
		final T floodVal = op0c.get().copy();
		final LinkedList<long[]> q = new LinkedList<>();
		q.addFirst(posAsArray.clone());
		long[] pos, nextPos;
		long[] perm = new long[r.numDimensions()];
		while (!q.isEmpty()) {
			pos = q.removeLast();
			rc.setPosition(pos);
			if (rc.get().compareTo(floodVal) == 0) {
				continue;
			}
			op0c.setPosition(pos);
			if (op0c.get().compareTo(floodVal) == 0) {
				// set new label
				rc.get().set(floodVal);
				switch (structElement) {
					case EIGHT_CONNECTED:
						Arrays.fill(perm, -1);
						int i = r.numDimensions() - 1;
						boolean add;
						while (i > -1) {
							nextPos = pos.clone();
							add = true;
							// Modify position
							for (int j = 0; j < r.numDimensions(); j++) {
								nextPos[j] += perm[j];
								// Check boundaries
								if (nextPos[j] < 0 || nextPos[j] >= r.dimension(j)) {
									add = false;
									break;
								}
							}
							if (add) {
								q.addFirst(nextPos);
							}
							// Calculate next permutation
							for (i = perm.length - 1; i > -1; i--) {
								if (perm[i] < 1) {
									perm[i]++;
									for (int j = i + 1; j < perm.length; j++) {
										perm[j] = -1;
									}
									break;
								}
							}
						}
						break;
					case FOUR_CONNECTED:
					default:
						for (int j = 0; j < r.numDimensions(); j++) {
							if (pos[j] + 1 < r.dimension(j)) {
								nextPos = pos.clone();
								nextPos[j]++;
								q.addFirst(nextPos);
							}
							if (pos[j] - 1 >= 0) {
								nextPos = pos.clone();
								nextPos[j]--;
								q.addFirst(nextPos);
							}
						}
						break;
				}
			}
		}
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(
		RandomAccessibleInterval<T> input1, Localizable input2)
	{
		return createFunc.compute1(input1);
	}
}
