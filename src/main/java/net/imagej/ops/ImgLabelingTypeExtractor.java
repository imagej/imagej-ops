/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

import java.lang.reflect.Type;

import net.imglib2.roi.labeling.ImgLabeling;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.types.TypeExtractor;
import org.scijava.types.TypeService;

/**
 * {@link TypeExtractor} plugin which operates on {@link ImgLabeling} objects.
 * <p>
 * For performance reasons, we examine only the first element of the iteration,
 * which may be a more specific type than later elements. Hence the generic type
 * given by this extraction may be overly constrained.
 * </p>
 *
 * @author Curtis Rueden
 */
@Plugin(type = TypeExtractor.class, priority = Priority.NORMAL)
public class ImgLabelingTypeExtractor implements
	TypeExtractor<ImgLabeling<?, ?>>
{

	@Parameter
	private TypeService typeService;

	@Override
	public Type reify(final ImgLabeling<?, ?> i, final int n) {
		if (n < 0 || n > 1) throw new IndexOutOfBoundsException();

		if (n == 0) return typeService.reify(i.getMapping().getLabels().iterator()
			.next());
		return typeService.reify(i.getSource());

	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class<ImgLabeling<?, ?>> getRawType() {
		return (Class) ImgLabeling.class;
	}

}
