/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.equation;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.script.ScriptLanguage;
import org.scijava.script.ScriptService;

/**
 * Computes an image using an equation.
 * <p>
 * The equation is evaluated using Javascript. The image's position coordinates
 * are available to the equation via the {@code p} array; e.g.:
 * {@code Math.cos(0.1*p[0]) + Math.sin(0.1*p[1])}.
 * </p>
 * <p>
 * Note that this op is rather slow; it is intended mainly for demonstration
 * purposes, and to easily generate small images for testing OPS workflows.
 * </p>
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Op.class, name = Equation.NAME)
public class DefaultEquation<T extends RealType<T>> extends
	AbstractFunction<String, IterableInterval<T>> implements Equation<T>
{

	@Parameter
	private ScriptService scriptService;

	@Parameter
	private LogService log;

	@Override
	public IterableInterval<T> compute(final String input,
		final IterableInterval<T> output)
	{
		final IterableInterval<T> image;
		if (output == null) {
			// produce a 256x256 float64 array-backed image by default
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final IterableInterval<T> newImage =
				(IterableInterval) ArrayImgs.doubles(256, 256);
			image = newImage;
		}
		else image = output;

		final String equation = input + ";";

		// evaluate the equation using Javascript!
		final ScriptLanguage js = scriptService.getLanguageByName("javascript");
		final ScriptEngine engine = js.getScriptEngine();
		final Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

		final Cursor<T> c = image.localizingCursor();
		final long[] pos = new long[image.numDimensions()];
		bindings.put("p", pos);

		try {
			while (c.hasNext()) {
				c.fwd();
				c.localize(pos);
				final Object o = engine.eval(equation);
				final double d =
					o instanceof Number ? ((Number) o).doubleValue() : Double.NaN;
				c.get().setReal(d);
			}
		}
		catch (final ScriptException exc) {
			log.error(exc);
		}
		return image;
	}

}
