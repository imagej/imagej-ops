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

import java.util.List;
import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OutputFunction;

import org.scijava.service.Service;

/**
 * 
 * The {@link FeatureService} can be used to compile a {@link Set} of
 * {@link Feature}s. The {@link Feature}s and their parameters are described in
 * {@link FeatureInfo}s. Compiling in this case means that, given some input
 * <I>, all required {@link Feature}s and {@link Op}s are automatically
 * resolved. If a required {@link Feature} or {@link Op} has some additional
 * parameters and therefore can't be automatically resolved, one can add this
 * {@link Op} as an invisible {@link Op}, which will not be exposed in the
 * resulting {@link List} of {@link FeatureResult}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <I>
 */
public interface FeatureService<I> extends Service {

	/**
	 * Creates an {@link OutputFunction} which, given some input <I>, returns a
	 * {@link List} of {@link FeatureResult}s. The {@link FeatureResult}
	 * correspond to the set of {@link Feature}s which are described in the
	 * {@link FeatureInfo}s.
	 * 
	 * @param visible
	 *            {@link Feature}s which will be part of the {@link List} of
	 *            {@link FeatureResult}s
	 * @param invisible
	 *            {@link Op}s which can't be automatically resolved, but are
	 *            required by at least one of the {@link Feature}s which are
	 *            calculated.
	 * @param inputType
	 *            type of the input. The {@link OutputFunction} will can be
	 *            updated with instances of this type only.
	 * @return {@link OutputFunction} which can be used to calculate features
	 *         for objects of type <I>.
	 */
	OutputFunction<I, List<FeatureResult>> compile(
			final Set<FeatureInfo> visible, Set<OpInfo> invisible,
			final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, List<FeatureResult>> compile(final FeatureInfo feature,
			final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, List<FeatureResult>> compile(final FeatureInfo feature,
			Set<OpInfo> invisible, final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, List<FeatureResult>> compile(
			final Set<FeatureInfo> visible, final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, FeatureResult> compile(
			final Class<? extends Feature> feature,
			final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, FeatureResult> compile(
			final Class<? extends Feature> feature, Set<OpInfo> helpers,
			final Class<? extends I> inputType);

	/**
	 * Simple convenience method.
	 * 
	 * see {@link FeatureService}{@link #compileFeatureSet(Set, Set, Class)}
	 */
	OutputFunction<I, FeatureResult> compile(
			final Class<? extends Feature> feature, OpInfo helper,
			final Class<? extends I> inputType);

}
