/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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
package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.LabeledFeatures;
import net.imagej.ops.features.zernike.ZernikeComputer;
import net.imagej.ops.features.zernike.ZernikeMoment;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Zernike Moments {@link Feature}s
 *
 * @author Andreas Graumann, University of Konstanz
 *
 * @param <I>
 */
@Plugin(type = FeatureSet.class, label = "Zernike Moment Features")
public class ZernikeFeatureSet<T extends RealType<T>> extends
		AbstractFeatureSet<IterableInterval<T>, List<DoubleType>> implements
		Contingent, LabeledFeatures<IterableInterval<T>, DoubleType> {

	@Parameter
	private OpService ops;

	@Parameter(label = "Compute Magnitude", description = "If the Magnitude should be computed")
	private boolean computeMagnitude = true;

	@Parameter(label = "Compute Phase", description = "If the Phase should be computed")
	private boolean computePhase = true;

	@Parameter(label = "Order Min", description = "The minimum order", min = "1", max = "10", stepSize = "1")
	private int orderMin = 2;

	@Parameter(label = "Oder Max", description = "The maximum order", min = "1", max = "10", stepSize = "1")
	private int orderMax = 6;

	private ZernikeComputer op;

	@Override
	public boolean conforms() {
		// something to compute?
		if (!this.computeMagnitude && !this.computePhase) {
			return false;
		}

		if (orderMax <= orderMin) {
			return false;
		}

		// dimension must be 2
		if (!(getInput().numDimensions() == 2)) {
			return false;
		}

		return true;
	}

	@Override
	public void run() {
		// get ZernikeComputer
		if (this.op == null) {
			try {
				this.op = this.ops.op(ZernikeComputer.class, getInput(),
						this.orderMin, this.orderMax);
			} catch (final Exception e) {
				throw new IllegalStateException(
						"Can not find suitable op! Error message: "
								+ e.getMessage());
			}

			setOutput(new HashMap<OpRef<? extends Op>, List<DoubleType>>());
		}

		// run zernike computer
		this.op.run();

		final List<DoubleType> res = new ArrayList<DoubleType>();
		for (final ZernikeMoment moment : this.op.getAllZernikeMoments()) {
			if (this.computeMagnitude) {
				res.add(new DoubleType(moment.getMagnitude()));
			}
			if (this.computePhase) {
				res.add(new DoubleType(moment.getPhase()));
			}
		}

		getOutput().clear();
		getOutput().put(
				new OpRef<ZernikeComputer>(ZernikeComputer.class,
						this.computeMagnitude, this.computePhase,
						this.orderMin, this.orderMax), res);
	}

	@Override
	public List<Pair<String, DoubleType>> getFeatureList(
			final IterableInterval<T> input) {
		compute(input);
		final List<Pair<String, DoubleType>> res = new ArrayList<Pair<String, DoubleType>>();
		for (final ZernikeMoment moment : this.op.getAllZernikeMoments()) {
			if (this.computeMagnitude) {
				final String featureName = "Zernike Magnitude of order "
						+ moment.getN() + " and repitition " + moment.getM();
				res.add(new ValuePair<String, DoubleType>(featureName,
						new DoubleType(moment.getMagnitude())));
			}
			if (this.computePhase) {
				final String featureName = "Zernike Phase of order "
						+ moment.getN() + " and repitition " + moment.getM();
				res.add(new ValuePair<String, DoubleType>(featureName,
						new DoubleType(moment.getPhase())));
			}
		}

		return res;
	}

	@Override
	public Map<OpRef<? extends Op>, List<DoubleType>> getFeaturesByRef(
			IterableInterval<T> input) {
		return compute(input);
	}
}
