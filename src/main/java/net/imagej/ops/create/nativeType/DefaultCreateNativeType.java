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

package net.imagej.ops.create.nativeType;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of the "create.nativeType" op.
 *
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Create.NativeType.class)
public class DefaultCreateNativeType<T extends NativeType<T>> extends
	AbstractNullaryFunctionOp<T> implements Ops.Create.NativeType
{

	@Parameter(required = false)
	private Class<T> type;

	@Override
	public T compute0() {
		return type != null ? createTypeFromClass() : createTypeFromScratch();
	}

	// -- Helper methods --

	private T createTypeFromClass() {
		try {
			return type.newInstance();
		}
		catch (final InstantiationException exc) {
			throw new IllegalArgumentException(exc);
		}
		catch (final IllegalAccessException exc) {
			throw new IllegalStateException(exc);
		}
	}

	private T createTypeFromScratch() {
		// NB: No type given; we can only guess.
		// HACK: For Java 6 compiler.
		final Object o = new DoubleType();
		@SuppressWarnings("unchecked")
		final T t = (T) o;
		return t;
	}

}
