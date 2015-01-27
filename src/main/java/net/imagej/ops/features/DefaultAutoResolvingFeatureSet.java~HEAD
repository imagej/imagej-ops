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
package net.imagej.ops.features;

import java.util.HashSet;
import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;

/**
 * @author Christian Dietz (University of Konstanz)
 *
 * @param <I>
 */
public class DefaultAutoResolvingFeatureSet<I, O> extends
		AbstractAutoResolvingFeatureSet<I, O> {

	private final HashSet<OpRef<?>> outputOps;

	private final HashSet<OpRef<?>> hiddenOps;

	public DefaultAutoResolvingFeatureSet() {
		this.outputOps = new HashSet<OpRef<?>>();
		this.hiddenOps = new HashSet<OpRef<?>>();
	}

	@Override
	public Set<OpRef<?>> getOutputOps() {
		return outputOps;
	}

	@Override
	public Set<OpRef<?>> getHiddenOps() {
		return hiddenOps;
	}

	public <OP extends Op> void addHiddenOp(Class<OP> type, Object... params) {
		addHiddenOp(new OpRef<OP>(type, params));
	}

	public <OP extends Op> void addOutputOp(Class<OP> type, Object... params) {
		addOutputOp(new OpRef<OP>(type, params));
	}

	public void addOutputOp(OpRef<?> ref) {
		outputOps.add(ref);
	}

	public void addHiddenOp(OpRef<?> ref) {
		hiddenOps.add(ref);
	}
}
