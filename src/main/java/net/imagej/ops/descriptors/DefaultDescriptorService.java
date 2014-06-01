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
import java.util.Collections;
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
	private CachedDescriptorModule resolveModule(
			final Class<? extends Op> moduleType, final Class<?> inputType,
			final Map<Class<?>, CachedDescriptorModule> availableModules)
			throws ModuleException {

		if (availableModules.containsKey(moduleType)) {
			return availableModules.get(moduleType);
		}

		// get all candidate ops for this module type
		final List<ModuleInfo> candidates = matcher.findCandidates(null,
				moduleType);

		// if there are no canidates, we can't resolve this module (and we fail)
		if (candidates.size() == 0)
			return null;

		// now: we check for the candidates. A candidate can be used, if all
		// fields can be resolved, given the available set of operations.
		// the only expceptions are special fields which are neither of
		// inputType nor a DescriptorParameterSet.
		loop: for (final ModuleInfo required : candidates) {

			final List<Object> parameters = new ArrayList<Object>();

			final List<CachedDescriptorModule> dependencies = new ArrayList<CachedDescriptorModule>();

			final HashMap<Class<?>, CachedDescriptorModule> tmpModules = new HashMap<Class<?>, CachedDescriptorModule>(
					availableModules);

			// we have to parse our items for other ops/features
			for (final ModuleItem<?> item : required.inputs()) {
				final Class<?> itemType = item.getType();

				// Ignore if it is a service
				if (Service.class.isAssignableFrom(itemType)) {
					continue;
				}

				// Handle operation
				if (Op.class.isAssignableFrom(itemType)) {
					@SuppressWarnings("unchecked")
					final Class<? extends Op> typeAsOp = (Class<? extends Op>) itemType;

					if (tmpModules.containsKey(typeAsOp)) {
						parameters.add(tmpModules.get(typeAsOp)
								.getDelegateObject());
						dependencies.add(tmpModules.get(typeAsOp));
					} else {
						final CachedDescriptorModule res = resolveModule(typeAsOp,
								inputType, tmpModules);

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

				if (DescriptorParameters.class.isAssignableFrom(itemType)
						|| itemType.isAssignableFrom(inputType)) {
					parameters.add(itemType);
					continue;
				}

				// its not an operation, it must be something else
				// Place-holder for non-required attributes and attributes which
				// can be provided by some existing operation

				// TODO: Later we can try to find these operations automatically
				// and optimize etc... but for now its fine
				final CachedDescriptorModule matchingModule = findMatchingOutputOp(
						itemType, inputType, tmpModules);
				if (matchingModule != null) {
					parameters.add(itemType);
					dependencies.add(matchingModule);
					tmpModules.put(itemType, matchingModule);
					continue;
				}

				continue loop;
			}

			final CachedDescriptorModule module = new CachedDescriptorModule(
					ops.module(moduleType, parameters.toArray()));

			// set-up graph...
			for (final CachedDescriptorModule dependency : dependencies) {
				dependency.addSuccessor(module);
				module.addPredecessor(dependency);
			}

			// we build our "tree"
			tmpModules.put(moduleType, module);

			// we know that only additional modules are in local map
			for (final Entry<Class<?>, CachedDescriptorModule> entry : tmpModules
					.entrySet()) {
				availableModules.put(entry.getKey(), entry.getValue());
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
			final Class<?> outputType, final Class<?> inputType,
			final Map<Class<?>, CachedDescriptorModule> availableModules)
			throws ModuleException {

		if (availableModules.containsKey(outputType)) {
			return availableModules.get(outputType);
		}

		final ArrayList<ModuleInfo> candidates = new ArrayList<ModuleInfo>();

		for (final ModuleInfo info : matcher.getOps()) {
			final Iterable<ModuleItem<?>> outputs = info.outputs();

			for (final ModuleItem<?> output : outputs) {
				if (outputType.isAssignableFrom(output.getType())) {
					candidates.add(info);
					break;
				}
			}
		}

		// Sort by priority
		Collections.sort(candidates);

		// we can be smarter here of course
		for (final ModuleInfo candidate : candidates) {
			Class<?> delegate = candidate.createModule().getDelegateObject()
					.getClass();
			if (Op.class.isAssignableFrom(delegate)) {
				@SuppressWarnings("unchecked")
				CachedDescriptorModule module = resolveModule(
						(Class<? extends Op>) delegate, inputType,
						availableModules);

				if (module != null) {
					return module;
				}
			}
		}

		// we didn't find a matching module
		return null;
	}

	/*
	 * Set InputUpdaters. These classes listen, if some input is updated from
	 * outside, i.e. the input or some parameters.
	 */
	private List<InputUpdateListener> postProcess(
			final HashMap<Class<?>, CachedDescriptorModule> availableModules) {
		final List<InputUpdateListener> listeners = new ArrayList<InputUpdateListener>();

		for (final CachedDescriptorModule module : availableModules.values()) {
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
				listeners.add(listener);

				// now we check if the update is performed by an internal
				// operation or from outside
				final CachedDescriptorModule internalModule = availableModules
						.get(type);

				// from inside, then its an update listener
				if (internalModule != null) {
					internalModule.registerOutputReceiver(item, listener);
				}

				continue;
			}

		}

		return listeners;
	}

	/* Create one update listener */
	private InputUpdateListener createUpdateListener(
			final CachedDescriptorModule module, final ModuleItem<?> item) {
		return new InputUpdateListener() {

			@Override
			public void update(final Object o) {
				module.setInput(item.getName(), o);
				module.markDirty();
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
	 * {@inheritDoc}
	 */
	@Override
	public Pair<List<Module>, List<InputUpdateListener>> compile(
			DescriptorSet set, Class<?> inputType, List<Class<? extends Op>> ops)
			throws ModuleException {

		final HashMap<Class<?>, CachedDescriptorModule> modulePool = new HashMap<Class<?>, CachedDescriptorModule>();

		for (final Class<? extends Op> op : ops) {
			final CachedDescriptorModule module = resolveModule(op, inputType,
					modulePool);
			if (module == null)
				throw new IllegalArgumentException(
						"Can't compile DescriptorSet" + set.toString()
								+ " Reason:" + op.getSimpleName()
								+ " can't be instantiated!");
		}

		// append update listeners
		final List<InputUpdateListener> listeners = postProcess(modulePool);

		// just return the visible ops
		final List<Module> compiledOps = new ArrayList<Module>();

		for (final Class<? extends Op> opClazz : ops) {
			compiledOps.add(modulePool.get(opClazz));
		}

		return new ValuePair<List<Module>, List<InputUpdateListener>>(
				compiledOps, listeners);
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
}
