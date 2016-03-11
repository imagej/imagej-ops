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

package net.imagej.ops.features.shape;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Namespace for shape features.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Namespace.class)
public class ShapeNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "shape";
	}

	//-- shapeIndex --

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@OpMethod(op = net.imagej.ops.features.shape.DefaultShapeIndex.class)
	public <T extends RealType<T>> RandomAccessibleInterval<FloatType> shapeIndex(
		final RandomAccessibleInterval<FloatType> out,
		final RandomAccessibleInterval<T> in)
	{
		final RandomAccessibleInterval<FloatType> result =
			(RandomAccessibleInterval) ops().run(
				net.imagej.ops.features.shape.DefaultShapeIndex.class, out, in);
		return result;
	}
	
}
