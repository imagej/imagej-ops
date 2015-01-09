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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputFunction;

import org.scijava.convert.ConvertService;
import org.scijava.module.MethodCallException;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

/**
 * The {@link DefaultFeatureService} implements a {@link FeatureService} in a
 * way, that each {@link Feature} is only calculated once per object. This
 * means, if for example a {@link Feature} or {@link Op} is required by two
 * independent {@link Feature}s, it will only be calculated once.
 * 
 * TODO: JavaDoc & Clean-Up
 * 
 * @author Christian Dietz
 */
@Plugin(type = Service.class)
public class DefaultFeatureService<I> extends AbstractService implements
		FeatureService<I> {

	@Parameter
	private OpService ops;

	@Parameter
	private ConvertService cs;

	@Parameter
	private OpMatchingService matcher;

	@Override
	public OutputFunction<I, List<FeatureResult>> compile(
			final Set<FeatureInfo> visible, final Set<OpInfo> invisible,
			Class<? extends I> input) {
		try {

			@SuppressWarnings("unchecked")
			final Source<I> inputSource = new Source<I>((Class<I>) input);

			final Map<Integer, CachedModule> modulePool = new HashMap<Integer, CachedModule>();

			final Set<OpInfo> allOps = new HashSet<OpInfo>();
			allOps.addAll(visible);
			allOps.addAll(invisible);

			for (final FeatureInfo op : visible) {
				CachedModule module = resolveModule(op, allOps, inputSource,
						modulePool);

				if (module == null) {
					throw new IllegalArgumentException(
							"Can't compile set of FeatureInfos!" + " Reason:"
									+ op.getDelegateClass().getSimpleName()
									+ " can't be instantiated!");
				}
			}

			postProcess(modulePool, inputSource);

			// Provide compiled ops
			final Map<Integer, Module> compiledOps = new HashMap<Integer, Module>();
			for (final OpInfo op : visible) {
				compiledOps.put(op.hashCode(), modulePool.get(op.hashCode()));
			}

			return new CompiledFeatureSet(compiledOps, inputSource);
		} catch (ModuleException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OutputFunction<I, List<FeatureResult>> compile(
			FeatureInfo featureInfo, Class<? extends I> inputType) {
		return compile(featureInfo, new HashSet<OpInfo>(), inputType);
	}

	@Override
	public OutputFunction<I, List<FeatureResult>> compile(
			FeatureInfo featureInfo, Set<OpInfo> invisible,
			Class<? extends I> inputType) {
		final HashSet<FeatureInfo> visible = new HashSet<FeatureInfo>();
		visible.add(featureInfo);
		return compile(visible, invisible, inputType);
	}

	@Override
	public OutputFunction<I, List<FeatureResult>> compile(
			Set<FeatureInfo> visible, Class<? extends I> inputType) {
		return compile(visible, new HashSet<OpInfo>(), inputType);
	}

	@Override
	public OutputFunction<I, FeatureResult> compile(
			Class<? extends Feature> feature, Class<? extends I> inputType) {

		return compile(feature, new HashSet<OpInfo>(), inputType);
	}

	@Override
	public OutputFunction<I, FeatureResult> compile(
			Class<? extends Feature> feature, Set<OpInfo> invisible,
			Class<? extends I> inputType) {
		final OutputFunction<I, List<FeatureResult>> op = compile(
				new FeatureInfo(feature), invisible, inputType);

		return new AbstractOutputFunction<I, FeatureResult>() {

			@Override
			public FeatureResult createOutput(I input) {
				return new DefaultFeatureResult();
			}

			@Override
			protected FeatureResult safeCompute(I input, FeatureResult output) {
				FeatureResult res = op.compute(input).get(0);
				output.setValue(res.getValue());
				output.setName(res.getName());
				return output;
			}
		};
	}

	@Override
	public OutputFunction<I, FeatureResult> compile(
			Class<? extends Feature> feature, OpInfo invisible,
			Class<? extends I> inputType) {
		HashSet<OpInfo> set = new HashSet<OpInfo>();
		set.add(invisible);
		return compile(feature, set, inputType);

	}

	// INTERNAL
	private CachedModule resolveModule(final OpInfo op,
			final Set<OpInfo> helpers, final Source<I> inputSource,
			final Map<Integer, CachedModule> modulePool) throws ModuleException {

		if (modulePool.containsKey(op.hashCode())) {
			return modulePool.get(op.hashCode());
		}

		// see if there are any candidates in the set of helpers or features
		// which may also provide parameters
		for (final OpInfo info : helpers) {
			if (op.getDelegateClass().isAssignableFrom(info.getDelegateClass())) {
				try {
					Module tmp = matcher.findModule(null,
							info.getDelegateClass(), info.getParameters());
					if (tmp != null) {

						// here I need to instantiate the module differently as
						// some of the parameters are already resolved.
						//
						CachedModule module = checkIfAvailable(info,
								tmp.getInfo(), modulePool, inputSource,
								helpers, info.getParameters());
						if (module != null)
							return module;
					}
				} catch (IllegalArgumentException iae) {
					// Nothing to do
				}
			}
		}

		// get all candidate ops for this module type
		final List<ModuleInfo> candidates = matcher.findCandidates(null,
				op.getDelegateClass());

		// if there are no canidates, we can't resolve this module (and we fail)
		if (candidates.size() == 0) {
			return null;
		}

		// now: we check for the candidates. A candidate can be used, if all
		// fields can be resolved, given the available set of operations.
		// the only exceptions are special fields which are neither of
		// inputType nor a DescriptorParameterSet.
		for (final ModuleInfo candidate : candidates) {
			final CachedModule m = checkIfAvailable(op, candidate, modulePool,
					inputSource, helpers, null);
			if (m != null)
				return m;
		}

		return null;
	}

	private CachedModule checkIfAvailable(final OpInfo parent,
			final ModuleInfo candidate,
			final Map<Integer, CachedModule> existingModules,
			final Source<I> inputSource, final Set<OpInfo> helpers,
			final Object[] param) {

		final List<Object> resolvedParams = new ArrayList<Object>();

		final List<CachedModule> dependencies = new ArrayList<CachedModule>();

		final HashMap<Integer, CachedModule> tmpCompiledModules = new HashMap<Integer, CachedModule>(
				existingModules);

		int i = -1;
		// we have to parse our items for other ops/features
		for (final ModuleItem<?> item : candidate.inputs()) {
			i++;

			final Class<?> itemType = item.getType();

			if (!item.isRequired() || Service.class.isAssignableFrom(itemType)) {
				continue;
			}

			// It's an input parameter, we can ignore it
			if (itemType.isAssignableFrom(inputSource.getType())) {
				resolvedParams.add(itemType);
				continue;
			}

			// Handle operation
			if (Op.class.isAssignableFrom(itemType)) {
				@SuppressWarnings("unchecked")
				final Class<? extends Op> opToResolve = (Class<? extends Op>) itemType;

				if (tmpCompiledModules.containsKey(opToResolve)) {
					resolvedParams.add(tmpCompiledModules.get(opToResolve)
							.getDelegateObject());
					dependencies.add(tmpCompiledModules.get(opToResolve));
				} else {
					try {

						CachedModule res = resolveModule(
								new OpInfo(opToResolve), helpers, inputSource,
								tmpCompiledModules);

						if (res == null) {
							return null;
						}

						dependencies.add(res);
						resolvedParams.add(res.getDelegateObject());
					} catch (ModuleException e) {
						throw new RuntimeException(e);
					}
				}
				continue;
			}

			if (param != null && param.length > i && param[i] != null
					&& cs.supports(param[i], itemType)) {
				resolvedParams.add(param[i]);
				continue;
			}

			return null;
		}

		final CachedModule module = new CachedModule(ops.module(
				parent.getDelegateClass(), resolvedParams.toArray()));

		// set-up graph...
		for (final CachedModule dependency : dependencies) {
			dependency.addSuccessor(module);
			module.addPredecessor(dependency);
		}

		// we build our "tree"
		tmpCompiledModules.put(
				new OpInfo(parent.getDelegateClass()).hashCode(), module);

		// we know that only additional modules are in local map
		for (final Entry<Integer, CachedModule> entry : tmpCompiledModules
				.entrySet()) {
			existingModules.put(entry.getKey(), entry.getValue());
		}

		return module;
	}

	private void postProcess(final Map<Integer, CachedModule> modulePool,
			final Source<?> inputSource) {

		for (final Entry<Integer, CachedModule> entry : modulePool.entrySet()) {
			final CachedModule module = entry.getValue();

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

				continue;
			}

		}
	}

	/* Create one update listener */
	private InputUpdateListener createUpdateListener(final CachedModule module,
			final ModuleItem<?> item) {
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

	/* <!-- Internal Classes --> */

	/*
	 * Simple Interface to mark Descriptors which listen for updates of external
	 * inputs (i.e. inputs which are not generated by an {@link Op}).
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	interface InputUpdateListener {

		void update(Object o);

		boolean listensTo(Class<?> clazz);
	}

	/*
	 * A {@link Source} can consume input values and will notify all connected
	 * {@link CachedModule}s that their output should be recalculated. A source
	 * is wrapped by a {@link CompiledFeatureSet}.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	class Source<II> {

		private ArrayList<InputUpdateListener> listeners = new ArrayList<InputUpdateListener>();

		private Class<II> type;

		public Source(final Class<II> type) {
			this.type = type;
		}

		public void update(final II input) {
			for (final InputUpdateListener listener : listeners) {
				listener.update(input);
			}
		}

		public void registerListener(final InputUpdateListener listener) {
			listeners.add(listener);
		}

		public Class<II> getType() {
			return type;
		}
	}

	/*
	 * A CompiledFeatureSet is a ready to go OutputFunction<I,
	 * Pair<String,Double>>. Whenever I is updated, the underyling modules get
	 * notified and will recalculate there outputs. (@see CachedModule}.
	 * 
	 * @author Christian Dietz (University of Konstanz)
	 */
	class CompiledFeatureSet extends
			AbstractOutputFunction<I, List<FeatureResult>> {

		private Source<I> source;
		private Map<Integer, Module> compiledModules;

		public CompiledFeatureSet(final Map<Integer, Module> compiledModules,
				final Source<I> source) {
			this.source = source;
			this.compiledModules = compiledModules;
		}

		@Override
		public List<FeatureResult> createOutput(I input) {
			return new ArrayList<FeatureResult>();
		}

		@Override
		protected List<FeatureResult> safeCompute(I input,
				List<FeatureResult> output) {
			source.update(input);

			for (final Module module : compiledModules.values()) {

				module.run();

				final DefaultFeatureResult result = new DefaultFeatureResult(
						module.getInfo().getName(),
						((Feature) module.getDelegateObject())
								.getFeatureValue());

				output.add(result);
			}

			return output;
		}
	}

	/*
	 * CachedModule is a Module which has to be marked as dirty in order to
	 * recalculte the output
	 */
	class CachedModule implements Module {

		private ArrayList<CachedModule> successors = new ArrayList<CachedModule>();

		private ArrayList<CachedModule> predeccessors = new ArrayList<CachedModule>();

		private final Map<ModuleItem<?>, Set<InputUpdateListener>> outputReceivers = new HashMap<ModuleItem<?>, Set<InputUpdateListener>>();

		private final Module module;

		public CachedModule(final Module module) {
			this.module = module;
		}

		boolean dirty = true;

		@Override
		public void run() {
			if (dirty) {
				runPredeccessors();
				module.run();

				for (final Entry<ModuleItem<?>, Set<InputUpdateListener>> entry : outputReceivers
						.entrySet()) {

					// update the listeners if there are any
					for (final InputUpdateListener listener : entry.getValue()) {
						listener.update(module.getOutput(entry.getKey()
								.getName()));
					}
				}
				dirty = false;
			}
		}

		void markDirty() {
			dirty = true;
			notifySuccessors();
		}

		private void notifySuccessors() {
			for (final CachedModule op : successors) {
				op.markDirty();
			}
		}

		private void runPredeccessors() {
			for (final CachedModule module : predeccessors) {
				module.run();
			}
		}

		public void addSuccessor(final CachedModule op) {
			successors.add(op);
		}

		public void addPredecessor(final CachedModule op) {
			predeccessors.add(op);
		}

		boolean isDirty() {
			return dirty;
		}

		@Override
		public void preview() {
			module.preview();
		}

		@Override
		public void cancel() {
			module.cancel();
		}

		@Override
		public void initialize() throws MethodCallException {
			module.initialize();
		}

		@Override
		public ModuleInfo getInfo() {
			return module.getInfo();
		}

		@Override
		public Object getDelegateObject() {
			return module.getDelegateObject();
		}

		@Override
		public Object getInput(final String name) {
			return module.getInput(name);
		}

		@Override
		public Object getOutput(final String name) {
			return module.getOutput(name);
		}

		@Override
		public Map<String, Object> getInputs() {
			return module.getInputs();
		}

		@Override
		public Map<String, Object> getOutputs() {
			return module.getOutputs();
		}

		@Override
		public void setInput(final String name, final Object value) {
			markDirty();
			module.setInput(name, value);
		}

		@Override
		public void setOutput(final String name, final Object value) {
			module.setOutput(name, value);
		}

		@Override
		public void setInputs(final Map<String, Object> inputs) {
			for (final Entry<String, Object> entry : inputs.entrySet()) {
				setInput(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public void setOutputs(final Map<String, Object> outputs) {
			module.setOutputs(outputs);
		}

		@Override
		public boolean isResolved(final String name) {
			return module.isResolved(name);
		}

		@Override
		public void setResolved(final String name, final boolean resolved) {
			module.setResolved(name, resolved);
		}

		public void registerOutputReceiver(final ModuleItem<?> item,
				final InputUpdateListener listener) {
			Set<InputUpdateListener> listeners = outputReceivers.get(item);
			if (listeners == null) {
				listeners = new HashSet<InputUpdateListener>();
				outputReceivers.put(item, listeners);
			}
			listeners.add(listener);
		}

		@Override
		public String toString() {
			return module.getInfo().getName();
		}
	}
}
