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

package net.imagej.ops.logic;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * An Op that works the same way as the <code>?:</code> operator. It sets the
 * output to the <code>ifTrueVal</code> if the input is evaluated as true, or
 * <code>ifFalseVal</code> otherwise.
 * 
 * @author Leon Yang
 */
@Plugin(type = Ops.Logic.Conditional.class)
public class If<I extends BooleanType<I>, O extends Type<O>> extends
	AbstractUnaryHybridCF<I, O> implements Ops.Logic.Conditional

{

	@Parameter
	private O ifTrueVal;

	@Parameter
	private O ifFalseVal;

	@Override
	public void compute(final I input, final O output) {
		output.set(input.get() ? ifTrueVal : ifFalseVal);
	}

	@Override
	public O createOutput(final I input) {
		return ifTrueVal.createVariable();
	}

}
