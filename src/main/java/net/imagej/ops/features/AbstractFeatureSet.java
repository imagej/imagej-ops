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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Computer;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.functionbuilder.ComputerBuilder;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.PluginService;

/**
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
public abstract class AbstractFeatureSet<I> extends
		AbstractOutputFunction<I, List<Pair<String, DoubleType>>> implements
		FeatureSet<I, Pair<String, DoubleType>> {

	@Parameter
	private PluginService ps;

	@Parameter
	private OpService ops;

	@Parameter
	private ComputerBuilder fs;

	/* internal stuff */

	/*
	 * Set containing all visible features, i.e. features will will be available
	 * as FeatureResults
	 */
	private HashSet<OpRef> visible;

	/*
	 * Set containing all invisible features, i.e. features which are required
	 * by visible features.
	 */
	private HashSet<OpRef> invisible;

	/*
	 * function representing the compiled feature set
	 */
	private Computer<I, List<DoubleType>> func;

	public AbstractFeatureSet() {
		this.visible = new HashSet<OpRef>();
		this.invisible = new HashSet<OpRef>();
	}

	protected List<Pair<String, DoubleType>> safeCompute(final I input,
			final List<Pair<String, DoubleType>> output) {
		output.clear();

		if (func == null) {
			init();
			func = fs.build(visible, DoubleType.class,
					(Class<I>) input.getClass(),
					invisible.toArray(new OpRef[invisible.size()]));
		}
		final List<Pair<String, DoubleType>> namedList = new ArrayList<Pair<String, DoubleType>>();

		Iterator<OpRef> it = visible.iterator();
		for (final DoubleType type : func.compute(input)) {
			// TODO: we may want to use
			namedList.add(new ValuePair<String, DoubleType>(it.next().getType()
					.getSimpleName(), type));
		}

		return namedList;
	}

	protected abstract void init();

	public List<Pair<String, DoubleType>> createOutput(I input) {
		return new ArrayList<Pair<String, DoubleType>>();
	};

	/**
	 * Add a visible feature, this means a feature which will be available as a
	 * {@link FeatureResult}
	 * 
	 * @param op
	 * @param parameters
	 */
	protected void addInvisible(Class<? extends Op> op, Object... parameters) {
		invisible.add(new OpRef(op, parameters));
	}

	/**
	 * Add an invisible feature, this means a feature which will not be
	 * available as a {@link FeatureResult}, but is required by some visible
	 * {@link Feature}
	 * 
	 * @param op
	 * @param parameters
	 */
	protected void addVisible(Class<? extends Op> op, Object... parameters) {
		visible.add(new OpRef(op, parameters));
	}
}
