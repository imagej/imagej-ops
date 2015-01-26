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
package net.imagej.ops;


/**
 * An {@link OpRef} holds all information required by the
 * {@link FeatureService} to create an {@link Op}. This means the {@link Class}
 * of the {@link Op} and all parameters which can't be auto-guessed by the
 * {@link FeatureService}.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public class OpRef {

	/*
	 * The Op to be created in the FeatureService
	 */
	private Class<? extends Op> op;

	/*
	 * Additional Parameters
	 */
	private Object[] parameters;

	/**
	 * @param op
	 *            class of {@link Op}
	 * @param parameters
	 *            additional parameters which can't be auto-guessed by
	 *            {@link FeatureService}
	 */
	public OpRef(Class<? extends Op> op, Object... parameters) {
		this.op = op;
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		int hash = 31;
		for (Object o : parameters) {
			hash = hash + o.hashCode() * 31;
		}
		return (hash = op.hashCode() * 31 + hash);
	}

	/**
	 * Type of the {@link Op}
	 * 
	 * @return
	 */
	public Class<? extends Op> getType() {
		return op;
	}

	/**
	 * @return additional parameters required by {@link Op}
	 */
	public Object[] getParameters() {
		return parameters;
	}
}
