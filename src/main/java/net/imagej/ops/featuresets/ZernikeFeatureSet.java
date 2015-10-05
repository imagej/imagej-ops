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

package net.imagej.ops.featuresets;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.features.zernike.helper.ZernikeComputer;
import net.imagej.ops.features.zernike.helper.ZernikeMoment;
import net.imagej.ops.stats.StatOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * {@link FeatureSet} to calculate {@link StatOp}s.
 * 
 * @author Daniel Seebacher, University of Konstanz
 * @author Tim-Oliver Buchholz, University of Konstanz
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Zernike Features", description = "Calculates the Zernike Features")
public class ZernikeFeatureSet<I extends RealType<I>>
		extends
			AbstractCachedFeatureSet<IterableInterval<I>, DoubleType> {

	@Parameter(type = ItemIO.INPUT, label = "Number of Bins", description = "The number of bins of the histogram", min = "1", max = "2147483647", stepSize = "1")
	private int numBins = 256;

	@Parameter(type = ItemIO.INPUT, label = "Repetition of Zernike Moment", description = "The repetition of the zernike moment to be calculated.", min = "1", max = "2147483647", stepSize = "1")
	private double repetition = 2;

	@Parameter(type = ItemIO.INPUT, label = "Order of Zernike Moment", description = "The order of the zernike moment to be calculated.", min = "1", max = "2147483647", stepSize = "1")
	private double order = 4;

	private FunctionOp<IterableInterval<I>, ZernikeMoment> zernikeComputer;

	@Override
	public void initialize() {
		super.initialize();
		zernikeComputer = ops().function(ZernikeComputer.class,
				ZernikeMoment.class, in(), order, repetition);
	}

	@Override
	public Collection<NamedFeature> getFeatures() {
		return Arrays.asList(new NamedFeature("Magnitude"),
				new NamedFeature("Phase"));
	}

	@Override
		public Map<NamedFeature, DoubleType> compute(IterableInterval<I> input) {
			HashMap<NamedFeature, DoubleType> map = new HashMap<NamedFeature, DoubleType>();
			
			ZernikeMoment results = zernikeComputer.compute(input);
			
			map.put(new NamedFeature("Magnitude"), new DoubleType(results.getMagnitude()));
			map.put(new NamedFeature("Phase"), new DoubleType(results.getPhase()));
			
			return map;
		}

}
