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

package net.imagej.ops.featuresets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.scijava.Priority;
import org.scijava.command.CommandInfo;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.cached.CachedOpEnvironment;

/**
 * In an {@link AbstractCachedFeatureSet} intermediate results are cached during
 * computation, avoiding redundant computations of the same feature @see
 * {@link CachedOpEnvironment}.
 * 
 * @author Christian Dietz, University of Konstanz.
 * @param <I> type of the input
 * @param <O> type of the output
 */
public abstract class AbstractCachedFeatureSet<I, O> extends
	AbstractFunctionOp<I, Map<NamedFeature, O>>implements FeatureSet<I, O>
{

	@Parameter
	private PluginService ps;

	@Parameter
	private Class<? extends Op>[] prioritizedOps;

	@Override
	public void initialize() {
		final List<CommandInfo> infos = new ArrayList<CommandInfo>();
		if (prioritizedOps != null) {
			for (final Class<? extends Op> prio : prioritizedOps) {
				final CommandInfo info = new CommandInfo(prio);
				info.setPriority(Priority.FIRST_PRIORITY);
				infos.add(info);
			}
		}
		setEnvironment(new CachedOpEnvironment(ops(), infos));
	}

}
