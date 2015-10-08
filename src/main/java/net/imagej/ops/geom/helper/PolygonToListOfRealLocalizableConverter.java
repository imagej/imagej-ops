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
package net.imagej.ops.geom.helper;

import java.util.ArrayList;
import java.util.List;

import org.scijava.Priority;
import org.scijava.convert.AbstractConverter;
import org.scijava.convert.ConversionRequest;
import org.scijava.convert.Converter;
import org.scijava.plugin.Plugin;

import net.imglib2.RealLocalizable;

/**
 * 
 * Converts a RandomAccessibleInterval to a Mesh
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = Converter.class, priority = Priority.FIRST_PRIORITY)
public class PolygonToListOfRealLocalizableConverter extends
		AbstractConverter<ThePolygon, List<RealLocalizable>> {
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object src, Class<T> dest) {
		
		if (src instanceof ThePolygon) {	
			return (T) new ArrayList<RealLocalizable>(((ThePolygon)src).getVertices());
		}
		return null;
	}

	@Override
	public Class<ThePolygon> getInputType() {
		return ThePolygon.class;
	}

	@Override
	public boolean supports(ConversionRequest request) {
		return super.supports(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<List<RealLocalizable>> getOutputType() {
		List<RealLocalizable> l = new ArrayList<RealLocalizable>();
		return (Class<List<RealLocalizable>>)l.getClass();
	}
}
