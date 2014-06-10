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

package net.imagej.ops.descriptors.descriptorsets;

import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.haralick.CoocMatrixCreate;
import net.imagej.ops.descriptors.haralick.features.ASM;
import net.imagej.ops.descriptors.haralick.features.ClusterPromenence;
import net.imagej.ops.descriptors.haralick.features.ClusterShade;
import net.imagej.ops.descriptors.haralick.features.Contrast;
import net.imagej.ops.descriptors.haralick.features.Correlation;
import net.imagej.ops.descriptors.haralick.features.DifferenceEntropy;
import net.imagej.ops.descriptors.haralick.features.DifferenceVariance;
import net.imagej.ops.descriptors.haralick.features.Entropy;
import net.imagej.ops.descriptors.haralick.features.HaralickVariance;
import net.imagej.ops.descriptors.haralick.features.ICM1;
import net.imagej.ops.descriptors.haralick.features.ICM2;
import net.imagej.ops.descriptors.haralick.features.IFDM;
import net.imagej.ops.descriptors.haralick.features.SumAverage;
import net.imagej.ops.descriptors.haralick.features.SumEntropy;
import net.imagej.ops.descriptors.haralick.features.SumVariance;
import net.imagej.ops.histogram.CooccurrenceMatrix.MatrixOrientation;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Context;
import org.scijava.module.Module;

/**
 * TODO: JavaDoc
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class HaralickDescriptorSet<I> extends ADoubleTypeDescriptorSet<I> {

	@SuppressWarnings("unchecked")
	public static final Class<? extends OutputOp<DoubleType>>[] OPS = new Class[] {
			ASM.class, ClusterPromenence.class, ClusterShade.class,
			Contrast.class, Correlation.class, DifferenceEntropy.class,
			DifferenceVariance.class, Entropy.class, ICM1.class, ICM2.class,
			IFDM.class, SumAverage.class, SumEntropy.class, SumVariance.class,
			HaralickVariance.class };

	public HaralickDescriptorSet(final Context context, final Class<I> type) {
		super(context, type);

		for (final Class<? extends OutputOp<?>> op : OPS) {
			addOp(op);
		}

		addOp(CoocMatrixCreate.class);
	}

	public void updateParameterNrGrayLevels(final int nrGrayLevels) {
		final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
		final Module module = compiledModules.get(CoocMatrixCreate.class);
		module.setInput("nrGrayLevels", nrGrayLevels);
	}

	public void updateParameterDistance(final int distance) {
		final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
		final Module module = compiledModules.get(CoocMatrixCreate.class);
		module.setInput("distance", distance);
	}

	public void updateParameterOrientation(final MatrixOrientation orientation) {
		final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
		final Module module = compiledModules.get(CoocMatrixCreate.class);
		module.setInput("orientation", orientation.toString());
	}

	@Override
	protected Class<? extends OutputOp<DoubleType>>[] descriptors() {
		return OPS;
	}
}
