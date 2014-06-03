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

package net.imagej.ops.descriptors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imglib2.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 * TODO: Clean-up TODO: JavaDoc
 * 
 * @author Christian Dietz
 */
@Plugin(type = Service.class)
public class DefaultDescriptorService extends AbstractService implements
		DescriptorService {

	@Parameter
	private OpService ops;

	@Parameter
	private OpMatchingService matcher;

	/*
	 * Recursively checking if we can automatically instantiate a descriptor set
	 * and setting all instances
	 */
	private CachedDescriptorModule resolveModule(final Class<? extends Op> op,
			final Source<?> inputSource,
			final Map<Class<?>, CachedDescriptorModule> compiledModules,
			final Collection<Class<? extends Op>> opsToCompile)
			throws ModuleException {

		if (compiledModules.containsKey(op)) {
			return compiledModules.get(op);
		}

		// get all candidate ops for this module type
		final List<ModuleInfo> candidates = matcher.findCandidates(null, op);

		// if there are no canidates, we can't resolve this module (and we fail)
		if (candidates.size() == 0)
			return null;

		// now: we check for the candidates. A candidate can be used, if all
		// fields can be resolved, given the available set of operations.
		// the only exceptions are special fields which are neither of
		// inputType nor a DescriptorParameterSet.
		loop: for (final ModuleInfo required : candidates) {

			final List<Object> parameters = new ArrayList<Object>();

			final List<CachedDescriptorModule> dependencies = new ArrayList<CachedDescriptorModule>();

			final HashMap<Class<?>, CachedDescriptorModule> tmpCompiledModules = new HashMap<Class<?>, CachedDescriptorModule>(
					compiledModules);

			// we have to parse our items for other ops/features
			for (final ModuleItem<?> item : required.inputs()) {
				final Class<?> itemType = item.getType();

				// Ignore if it is a service
				if (Service.class.isAssignableFrom(itemType)) {
					continue;
				}

				if (itemType.isAssignableFrom(inputSource.getType())) {
					parameters.add(itemType);
					continue;
				}

				// it's an dialog element (i.e. a primitive type)
				if (itemType.isPrimitive()
						|| String.class.isAssignableFrom(itemType)) {
					parameters.add(item.getInitializer());
					continue;
				}

				// Handle operation
				if (Op.class.isAssignableFrom(itemType)) {
					@SuppressWarnings("unchecked")
					final Class<? extends Op> opToResolve = (Class<? extends Op>) itemType;

					if (tmpCompiledModules.containsKey(opToResolve)) {
						parameters.add(tmpCompiledModules.get(opToResolve)
								.getDelegateObject());
						dependencies.add(tmpCompiledModules.get(opToResolve));
					} else {
						final CachedDescriptorModule res = resolveModule(
								opToResolve, inputSource, tmpCompiledModules,
								opsToCompile);

						if (res == null)
							continue loop;

						dependencies.add(res);
						parameters.add(res.getDelegateObject());
					}

					continue;
				}

				if (!item.isRequired()) {
					continue;
				}

				// If it can't be resolved from another op, we can't use this
				final CachedDescriptorModule matchingModule = findMatchingOutputOp(
						itemType, inputSource, tmpCompiledModules, opsToCompile);
				if (matchingModule != null) {
					parameters.add(itemType);
					dependencies.add(matchingModule);
					tmpCompiledModules.put(itemType, matchingModule);
					continue;
				}

				continue loop;
			}

			final CachedDescriptorModule module = new CachedDescriptorModule(
					ops.module(op, parameters.toArray()));

			// set-up graph...
			for (final CachedDescriptorModule dependency : dependencies) {
				dependency.addSuccessor(module);
				module.addPredecessor(dependency);
			}

			// we build our "tree"
			tmpCompiledModules.put(op, module);

			// we know that only additional modules are in local map
			for (final Entry<Class<?>, CachedDescriptorModule> entry : tmpCompiledModules
					.entrySet()) {
				compiledModules.put(entry.getKey(), entry.getValue());
			}

			return module;
		}

		return null;
	}

	/*
	 * Tries to find an Op which has at east one output of type outputType and
	 * which can be resolved using inputType
	 */
	private CachedDescriptorModule findMatchingOutputOp(
			final Class<?> outputType, final Source<?> inputSource,
			final HashMap<Class<?>, CachedDescriptorModule> compiledModules,
			final Collection<Class<? extends Op>> opsToCompile)
			throws ModuleException {

		final List<Pair<ModuleInfo, Class<? extends Op>>> candidates = new ArrayList<Pair<ModuleInfo, Class<? extends Op>>>();
		for (final Class<? extends Op> op : opsToCompile) {
			for (final ModuleInfo info : matcher.findCandidates(null, op)) {
				final Iterable<ModuleItem<?>> outputs = info.outputs();

				for (final ModuleItem<?> output : outputs) {
					if (outputType.isAssignableFrom(output.getType())) {
						candidates
								.add(new ValuePair<ModuleInfo, Class<? extends Op>>(
										info, op));
						break;
					}
				}
			}
		}

		// Sort by priority
		Collections.sort(candidates,
				new Comparator<Pair<ModuleInfo, Class<? extends Op>>>() {

					@Override
					public int compare(
							final Pair<ModuleInfo, Class<? extends Op>> o1,
							final Pair<ModuleInfo, Class<? extends Op>> o2) {
						return o1.getA().compareTo(o2.getA());
					}
				});

		for (final Pair<ModuleInfo, Class<? extends Op>> candidate : candidates) {

			final CachedDescriptorModule module = resolveModule(
					candidate.getB(), inputSource, compiledModules,
					opsToCompile);

			if (module != null) {
				return module;
			}
		}

		// we didn't find a matching module
		return null;
	}

	/*
	 * Set InputUpdaters. These classes listen, if some input is updated from
	 * outside, i.e. the input or some parameters.
	 */
	private void postProcess(
			final Map<Class<?>, CachedDescriptorModule> modulePool,
			final Source<?> inputSource) {

		for (final Entry<Class<?>, CachedDescriptorModule> entry : modulePool
				.entrySet()) {
			if (!Op.class.isAssignableFrom(entry.getKey())) {
				continue;
			}

			final CachedDescriptorModule module = entry.getValue();

			for (final ModuleItem<?> item : module.getInfo().inputs()) {
				final Class<?> type = item.getType();

				// fields we can ignore during post-processing
				if (Op.class.isAssignableFrom(type)
						|| Service.class.isAssignableFrom(type)
						|| !item.isRequired()) {
					continue;
				}

				// TODO: we need to take care about generics here.
				final InputUpdateListener listener = createUpdateListener(
						module, item);

				if (type.isAssignableFrom(inputSource.getType())) {
					inputSource.registerListener(listener);
					continue;
				}

				// now we check if the update is performed by an internal
				// operation or from outside
				final CachedDescriptorModule internalModule = modulePool
						.get(type);

				// from inside, then its an update listener
				if (internalModule != null) {
					internalModule.registerOutputReceiver(item, listener);
				}

				continue;
			}

		}
	}

	/* Create one update listener */
	private InputUpdateListener createUpdateListener(
			final CachedDescriptorModule module, final ModuleItem<?> item) {
		return new InputUpdateListener() {

			@Override
			public void update(final Object o) {
				module.setInput(item.getName(), o);
			}

			// TODO: be more restrictive concerning generics here
			@Override
			public boolean listensTo(final Class<?> clazz) {
				return item.getType().isAssignableFrom(clazz);
			}

			@Override
			public String toString() {
				return module.getInfo().getName();
			}
		};
	}

	/**
	 * Simple Interface to mark Descriptors which listen for updates of external
	 * inputs (i.e. inputs which are not generated by an {@link Op}).
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	public interface InputUpdateListener {

		void update(Object o);

		boolean listensTo(Class<?> clazz);
	}

	@Override
	public Map<Class<? extends Op>, Module> compile(
			final List<Class<? extends Op>> ops, final Source<?> inputSource)
			throws ModuleException {
		final Map<Class<?>, CachedDescriptorModule> modulePool = new HashMap<Class<?>, CachedDescriptorModule>();

		for (final Class<? extends Op> op : ops) {
			final CachedDescriptorModule module = resolveModule(op,
					inputSource, modulePool, ops);
			if (module == null)
				throw new IllegalArgumentException(
						"Can't compile DescriptorSet!" + " Reason:"
								+ op.getSimpleName()
								+ " can't be instantiated!");
		}

		postProcess(modulePool, inputSource);

		final Map<Class<? extends Op>, Module> compiledOps = new HashMap<Class<? extends Op>, Module>();

		for (final Class<? extends Op> op : ops) {
			compiledOps.put(op, modulePool.get(op));
		}

		return compiledOps;

	}
}
