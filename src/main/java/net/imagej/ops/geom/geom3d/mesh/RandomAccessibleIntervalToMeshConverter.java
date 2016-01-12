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

package net.imagej.ops.geom.geom3d.mesh;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.Priority;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Converts a RandomAccessibleInterval to a Mesh
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Converter.class, priority = Priority.VERY_HIGH_PRIORITY)
public class RandomAccessibleIntervalToMeshConverter extends
	AbstractConverter<RandomAccessibleInterval, Mesh>
{

	@Parameter
	private OpService ops;
	private UnaryFunctionOp<RandomAccessibleInterval, Mesh> marchingCubesFunc;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {
		if (marchingCubesFunc == null) {
			marchingCubesFunc = Functions.unary(ops, Ops.Geometric.MarchingCubes.class, Mesh.class,
				(RandomAccessibleInterval) src);
		}
		if (src instanceof IterableInterval<?>) {
			return (T) marchingCubesFunc.compute1((RandomAccessibleInterval) src);
		}
		return null;
	}

	@Override
	public Class<Mesh> getOutputType() {
		return Mesh.class;
	}

	@Override
	public Class<RandomAccessibleInterval> getInputType() {
		return RandomAccessibleInterval.class;
	}

	@Override
	public boolean supports(ConversionRequest request) {
		return super.supports(request) && ((IterableInterval) request
			.sourceObject()).numDimensions() == 3;
	}
}
