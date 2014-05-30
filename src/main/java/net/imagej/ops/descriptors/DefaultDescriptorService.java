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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;

import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 * Default implementation of {@link DescriptorService}. Resolves all
 * dependencies for a {@link Op} or a set of {@link Op}s.
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

	@SuppressWarnings("unchecked")
	@Override
	public <OP extends Op, INPUTTYPE> ResolvedDescriptor<OP, INPUTTYPE> resolveDependencies(
			final Class<OP> feature, final Class<? extends INPUTTYPE> inputType) {

		// First pass: check if we can automatically create the op and remember
		// all
		// operations
		final HashMap<Class<?>, Module> allExistingOps = new HashMap<Class<?>, Module>();

		final Module module = initModule(feature, inputType, allExistingOps);

		if (module == null)
			throw new IllegalArgumentException("cannot create feature");

		// Second pass: set updaters for operations. recursive approach as the
		// list
		// of listeners must be correct, this means it must take into account
		// the
		// dependencies of the ops.
		final List<InputUpdateListeners> listeners = new ArrayList<InputUpdateListeners>();
		postProcess(feature, inputType, allExistingOps, listeners,
				new ArrayList<Class<?>>());

		return new ResolvedDescriptor<OP, INPUTTYPE>(
				(OP) module.getDelegateObject(), listeners);
	}

	/* Set input updaters */
	@SuppressWarnings("unchecked")
	private void postProcess(final Class<? extends Op> feature,
			final Class<?> inputType,
			final HashMap<Class<?>, Module> instantiatedModules,
			final List<InputUpdateListeners> listeners,
			final List<Class<?>> processed) {

		processed.add(feature);

		final List<InputUpdateListeners> localListeners = new ArrayList<InputUpdateListeners>();

		final Module module = instantiatedModules.get(feature);
		InputUpdateListeners myListener = null;
		for (final ModuleItem<?> item : module.getInfo().inputs()) {
			if (!item.isRequired()) {
				continue;
			}

			if (item.getType().isAssignableFrom(inputType)) {
				// TODO: we need to take care about generics here.
				myListener = createUpdateListener(module, item);
				continue;
			}

			// its an op
			if (Op.class.isAssignableFrom(item.getType())
					&& !processed.contains(item.getType())) {
				postProcess((Class<? extends Op>) item.getType(), inputType,
						instantiatedModules, localListeners, processed);
				continue;
			}
		}

		listeners.addAll(localListeners);

		if (myListener != null) {
			listeners.add(myListener);
		}
	}

	/**
	 * @param module
	 * @param item
	 * @return
	 */
	private InputUpdateListeners createUpdateListener(final Module module,
			final ModuleItem<?> item) {
		return new InputUpdateListeners() {

			@Override
			public void update(final Object o) {
				module.setInput(item.getName(), o);
				module.run();
			}
		};
	}

	/*
	 * Recursively checking if we can automatically instantiate a module of type
	 * opType given input of type inputType. Reusing ops.
	 */
	@SuppressWarnings("unchecked")
	private Module initModule(final Class<? extends Op> moduleType,
			final Class<?> inputType,
			final HashMap<Class<?>, Module> existingOps) {
		final List<ModuleInfo> candidates = matcher.findCandidates(null,
				moduleType);
		if (candidates.size() == 0)
			return null;

		loop: for (final ModuleInfo parent : candidates) {

			final List<Object> parameters = new ArrayList<Object>();

			final HashMap<Class<?>, Module> tmpAllCreatedOps = new HashMap<Class<?>, Module>();

			// we have to parse our items for other ops/features
			for (final ModuleItem<?> item : parent.inputs()) {
				final Class<?> type = item.getType();

				// Ignore if it is a service
				if (Service.class.isAssignableFrom(type)) {
					continue;
				}

				// Place-holder for non-required attributes
				if (!item.isRequired()) {
					parameters.add(item.getType());
					continue;
				}

				// Handle operation
				if (Op.class.isAssignableFrom(type)) {
					final Class<? extends Op> typeAsOp = (Class<? extends Op>) type;

					if (existingOps.containsKey(typeAsOp)) {
						parameters.add(existingOps.get(typeAsOp)
								.getDelegateObject());
					} else if (tmpAllCreatedOps.containsKey(typeAsOp)) {
						parameters.add(tmpAllCreatedOps.get(typeAsOp)
								.getDelegateObject());
					} else {
						final Object res = initModule(typeAsOp, inputType,
								tmpAllCreatedOps);
						if (res == null)
							return null;

						parameters.add(((Module) res).getDelegateObject());
					}

					continue;
				}

				// Set input
				// TODO: Generic check
				if (type.isAssignableFrom(inputType)) {
					parameters.add(inputType.getClass());
					continue;
				}

				continue loop;
			}

			final Module module = ops.module(moduleType, parameters.toArray());
			tmpAllCreatedOps.put(moduleType, module);

			// we know that only additional ops are in local map
			for (final Entry<Class<?>, Module> entry : tmpAllCreatedOps
					.entrySet()) {
				existingOps.put(entry.getKey(), entry.getValue());
			}

			return module;
		}

		return null;
	}

	public interface InputUpdateListeners {

		void update(Object o);
	}

}
