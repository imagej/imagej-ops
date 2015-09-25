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
package net.imagej.ops.geometric;

import java.lang.reflect.Type;

import org.scijava.Priority;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Geometric2D.Contour;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegion;

/**
 * 
 * Converts a RandomAccessibleInterval to a polygon
 * 
 * @author Daniel Seebacher, University of Konstanz
 *
 */
@SuppressWarnings("rawtypes")
@Plugin(type = Converter.class, priority = Priority.FIRST_PRIORITY)
public class RandomAccessibleIntervalToPolygonConverter
		extends
			AbstractConverter<LabelRegion, Polygon> {

	@Parameter
	private OpService ops;
	private FunctionOp<Object, Object> contourFunc;

	@SuppressWarnings({"unchecked"})
	@Override
	public <T> T convert(final Object src, final Class<T> dest) {
		if (contourFunc == null) {
			contourFunc = (FunctionOp) ops.function(Contour.class, dest, src,
					true, true);
		}
		// FIXME: can we make this faster?
		final Polygon p = (Polygon) contourFunc.compute(src);
		return (T) p;
	}

	@Override
	public Class<Polygon> getOutputType() {
		return Polygon.class;
	}

	@Override
	public Class<LabelRegion> getInputType() {
		return LabelRegion.class;
	}

	@Override
	public boolean supports(final ConversionRequest request) {

		Object sourceObject = request.sourceObject();
		Class<?> sourceClass = request.sourceClass();

		
		if (sourceObject != null && !(sourceObject instanceof LabelRegion)) {
			return false;
		} else
			if (sourceClass != null
					&& !(LabelRegion.class.isAssignableFrom(sourceClass))) {
			return false;
		}

		Class<?> destClass = request.destClass();
		Type destType = request.destType();

		if (destClass != null && !(destClass == Polygon.class)) {
			return false;
		} else if (destType != null && !(destType == Polygon.class)) {
			return false;
		}

		
		
		return true;
	}

}
