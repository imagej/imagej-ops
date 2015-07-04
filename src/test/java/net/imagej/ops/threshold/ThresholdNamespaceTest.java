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

package net.imagej.ops.threshold;

import net.imagej.ops.AbstractNamespaceTest;
import net.imagej.ops.ThresholdOps.Huang;
import net.imagej.ops.ThresholdOps.Intermodes;
import net.imagej.ops.ThresholdOps.IsoData;
import net.imagej.ops.ThresholdOps.Li;
import net.imagej.ops.ThresholdOps.MaxEntropy;
import net.imagej.ops.ThresholdOps.MaxLikelihood;
import net.imagej.ops.ThresholdOps.Mean;
import net.imagej.ops.ThresholdOps.MinError;
import net.imagej.ops.ThresholdOps.Minimum;
import net.imagej.ops.ThresholdOps.Moments;
import net.imagej.ops.ThresholdOps.Otsu;
import net.imagej.ops.ThresholdOps.Percentile;
import net.imagej.ops.ThresholdOps.RenyiEntropy;
import net.imagej.ops.ThresholdOps.Shanbhag;
import net.imagej.ops.ThresholdOps.Triangle;
import net.imagej.ops.ThresholdOps.Yen;

import org.junit.Test;

/**
 * Tests that the ops of the {@code threshold} namespace have corresponding
 * type-safe Java method signatures declared in the {@link ThresholdNamespace}
 * class.
 *
 * @author Alison Walter
 */
public class ThresholdNamespaceTest extends AbstractNamespaceTest {

	/** Tests for {@link Huang} method convergence. */
	@Test
	public void testHuang() {
		assertComplete(ThresholdNamespace.class, Huang.NAME);
	}

	/** Tests for {@link Intermodes} method convergence. */
	@Test
	public void testIntermodes() {
		assertComplete(ThresholdNamespace.class, Intermodes.NAME);
	}

	/** Tests for {@link IsoData} method convergence. */
	@Test
	public void testIsoData() {
		assertComplete(ThresholdNamespace.class, IsoData.NAME);
	}

	/** Tests for {@link Li} method convergence. */
	@Test
	public void testLi() {
		assertComplete(ThresholdNamespace.class, Li.NAME);
	}

	/** Tests for {@link MaxEntropy} method convergence. */
	@Test
	public void testMaxEntropy() {
		assertComplete(ThresholdNamespace.class, MaxEntropy.NAME);
	}

	/** Tests for {@link MaxLikelihood} method convergence. */
	@Test
	public void testMaxLikelihood() {
		assertComplete(ThresholdNamespace.class, MaxLikelihood.NAME);
	}

	/** Tests for {@link Mean} method convergence. */
	@Test
	public void testMean() {
		assertComplete(ThresholdNamespace.class, Mean.NAME);
	}

	/** Tests for {@link MinError} method convergence. */
	@Test
	public void testMinError() {
		assertComplete(ThresholdNamespace.class, MinError.NAME);
	}

	/** Tests for {@link Minimum} method convergence. */
	@Test
	public void testMinimum() {
		assertComplete(ThresholdNamespace.class, Minimum.NAME);
	}

	/** Tests for {@link Moments} method convergence. */
	@Test
	public void testMoments() {
		assertComplete(ThresholdNamespace.class, Moments.NAME);
	}

	/** Tests for {@link Otsu} method convergence. */
	@Test
	public void testOtsu() {
		assertComplete(ThresholdNamespace.class, Otsu.NAME);
	}

	/** Tests for {@link Percentile} method convergence. */
	@Test
	public void testPercentile() {
		assertComplete(ThresholdNamespace.class, Percentile.NAME);
	}

	/** Tests for {@link RenyiEntropy} method convergence. */
	@Test
	public void testRenyiEntropy() {
		assertComplete(ThresholdNamespace.class, RenyiEntropy.NAME);
	}

	/** Tests for {@link Shanbhag} method convergence. */
	@Test
	public void testShanbhag() {
		assertComplete(ThresholdNamespace.class, Shanbhag.NAME);
	}

	/** Tests for {@link Triangle} method convergence. */
	@Test
	public void testTriangle() {
		assertComplete(ThresholdNamespace.class, Triangle.NAME);
	}

	/** Tests for {@link Yen} method convergence. */
	@Test
	public void testYen() {
		assertComplete(ThresholdNamespace.class, Yen.NAME);
	}

}
