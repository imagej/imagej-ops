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

package net.imagej.ops.geom.geom3d;

import net.imagej.mesh.Mesh;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link net.imagej.ops.Ops.Geometric.Compactness}.
 * 
 * Based on http://www.sciencedirect.com/science/article/pii/S003132030700324X.
 * 
 * In the paper compactness is defined as area^3/volume^2. For a sphere this is
 * minimized and results in 36*PI. To get values between (0,1] we use
 * (36*PI)/(area^3/volume^2).
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.Compactness.class, label = "Geometric (3D): Compactness", priority = Priority.VERY_HIGH)
public class DefaultCompactness extends AbstractUnaryHybridCF<Mesh, DoubleType> implements Ops.Geometric.Compactness {

	private UnaryFunctionOp<Mesh, DoubleType> surfaceArea;

	private UnaryFunctionOp<Mesh, DoubleType> volume;

	@Override
	public void initialize() {
		surfaceArea = Functions.unary(ops(), Ops.Geometric.BoundarySize.class, DoubleType.class, in());
		volume = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
	}

	@Override
	public void compute(final Mesh input, final DoubleType output) {
		final double s3 = Math.pow(surfaceArea.calculate(input).get(), 3);
		final double v2 = Math.pow(volume.calculate(input).get(), 2);
		final double c = s3 / v2;
		output.set((36.0 * Math.PI) / c);
	}

	@Override
	public DoubleType createOutput(Mesh input) {
		return new DoubleType();
	}

}
