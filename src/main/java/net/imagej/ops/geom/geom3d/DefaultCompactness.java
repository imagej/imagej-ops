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

package net.imagej.ops.geom.geom3d;

import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link net.imagej.ops.Ops.Geometric.Compactness}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Ops.Geometric.Compactness.class,
	label = "Geometric (3D): Compactness", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultCompactness extends AbstractUnaryFunctionOp<Mesh, DoubleType>
	implements Ops.Geometric.Compactness
{

	private UnaryFunctionOp<Mesh, DoubleType> surfacePixel;

	private UnaryFunctionOp<Mesh, DoubleType> volume;

	@Override
	public void initialize() {
		surfacePixel = Functions.unary(ops(), Ops.Geometric.BoundaryPixelCount.class,
			DoubleType.class, in());
		volume = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
	}

	@Override
	public DoubleType compute1(final Mesh input) {
		double s3 = Math.pow(surfacePixel.compute1(input).get(), 3);
		double v2 = Math.pow(volume.compute1(input).get(), 2);

		return new DoubleType((v2 * 36.0 * Math.PI) / s3);
	}

}
