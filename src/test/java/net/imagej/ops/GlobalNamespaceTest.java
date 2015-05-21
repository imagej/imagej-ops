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

package net.imagej.ops;

import net.imagej.ops.Ops.ASCII;
import net.imagej.ops.Ops.CreateImg;
import net.imagej.ops.Ops.Equation;
import net.imagej.ops.Ops.Eval;
import net.imagej.ops.Ops.FFT;
import net.imagej.ops.Ops.Gauss;
import net.imagej.ops.Ops.Help;
import net.imagej.ops.Ops.IFFT;
import net.imagej.ops.Ops.Identity;
import net.imagej.ops.Ops.Invert;
import net.imagej.ops.Ops.Join;
import net.imagej.ops.Ops.LogKernel;
import net.imagej.ops.Ops.Lookup;
import net.imagej.ops.Ops.Loop;
import net.imagej.ops.Ops.Map;
import net.imagej.ops.Ops.Max;
import net.imagej.ops.Ops.Mean;
import net.imagej.ops.Ops.Median;
import net.imagej.ops.Ops.Min;
import net.imagej.ops.Ops.MinMax;
import net.imagej.ops.Ops.Normalize;
import net.imagej.ops.Ops.Project;
import net.imagej.ops.Ops.Scale;
import net.imagej.ops.Ops.Size;
import net.imagej.ops.Ops.Slicewise;
import net.imagej.ops.Ops.StdDeviation;

import org.junit.Test;

/**
 * Tests that the ops of the global namespace have corresponding type-safe Java
 * method signatures declared in the {@link OpService} class.
 *
 * @author Curtis Rueden
 */
public class GlobalNamespaceTest extends AbstractNamespaceTest {

	/** Tests for {@link CreateImg} method convergence. */
	@Test
	public void testCreateImg() {
		assertComplete(null, OpService.class, CreateImg.NAME);
	}

	/** Tests for {@link ASCII} method convergence. */
	@Test
	public void testASCII() {
		assertComplete(null, OpService.class, ASCII.NAME);
	}

	/** Tests for {@link Equation} method convergence. */
	@Test
	public void testEquation() {
		assertComplete(null, OpService.class, Equation.NAME);
	}

	/** Tests for {@link Eval} method convergence. */
	@Test
	public void testEval() {
		assertComplete(null, OpService.class, Eval.NAME);
	}

	/** Tests for {@link FFT} method convergence. */
	@Test
	public void testFFT() {
		assertComplete(null, OpService.class, FFT.NAME);
	}

	/** Tests for {@link Gauss} method convergence. */
	@Test
	public void testGauss() {
		assertComplete(null, OpService.class, Gauss.NAME);
	}

	/** Tests for {@link Help} method convergence. */
	@Test
	public void testHelp() {
		assertComplete(null, OpService.class, Help.NAME);
	}

	/** Tests for {@link Identity} method convergence. */
	@Test
	public void testIdentity() {
		assertComplete(null, OpService.class, Identity.NAME);
	}

	/** Tests for {@link IFFT} method convergence. */
	@Test
	public void testIFFT() {
		assertComplete(null, OpService.class, IFFT.NAME);
	}

	/** Tests for {@link Invert} method convergence. */
	@Test
	public void testInvert() {
		assertComplete(null, OpService.class, Invert.NAME);
	}

	/** Tests for {@link Join} method convergence. */
	@Test
	public void testJoin() {
		assertComplete(null, OpService.class, Join.NAME);
	}

	/** Tests for {@link LogKernel} method convergence. */
	@Test
	public void testLogKernel() {
		assertComplete(null, OpService.class, LogKernel.NAME);
	}

	/** Tests for {@link Lookup} method convergence. */
	@Test
	public void testLookup() {
		assertComplete(null, OpService.class, Lookup.NAME);
	}

	/** Tests for {@link Loop} method convergence. */
	@Test
	public void testLoop() {
		assertComplete(null, OpService.class, Loop.NAME);
	}

	/** Tests for {@link Map} method convergence. */
	@Test
	public void testMap() {
		assertComplete(null, OpService.class, Map.NAME);
	}

	/** Tests for {@link Max} method convergence. */
	@Test
	public void testMax() {
		assertComplete(null, OpService.class, Max.NAME);
	}

	/** Tests for {@link Mean} method convergence. */
	@Test
	public void testMean() {
		assertComplete(null, OpService.class, Mean.NAME);
	}

	/** Tests for {@link Median} method convergence. */
	@Test
	public void testMedian() {
		assertComplete(null, OpService.class, Median.NAME);
	}

	/** Tests for {@link Min} method convergence. */
	@Test
	public void testMin() {
		assertComplete(null, OpService.class, Min.NAME);
	}

	/** Tests for {@link MinMax} method convergence. */
	@Test
	public void testMinMax() {
		assertComplete(null, OpService.class, MinMax.NAME);
	}

	/** Tests for {@link Normalize} method convergence. */
	@Test
	public void testNormalize() {
		assertComplete(null, OpService.class, Normalize.NAME);
	}

	/** Tests for {@link Project} method convergence. */
	@Test
	public void testProject() {
		assertComplete(null, OpService.class, Project.NAME);
	}

	/** Tests for {@link Scale} method convergence. */
	@Test
	public void testScale() {
		assertComplete(null, OpService.class, Scale.NAME);
	}

	/** Tests for {@link Size} method convergence. */
	@Test
	public void testSize() {
		assertComplete(null, OpService.class, Size.NAME);
	}

	/** Tests for {@link Slicewise} method convergence. */
	@Test
	public void testSlicewise() {
		assertComplete(null, OpService.class, Slicewise.NAME);
	}

	/** Tests for {@link StdDeviation} method convergence. */
	@Test
	public void testStdDeviation() {
		assertComplete(null, OpService.class, StdDeviation.NAME);
	}
}
