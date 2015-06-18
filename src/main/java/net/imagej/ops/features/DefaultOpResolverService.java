package net.imagej.ops.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.Op;
import net.imagej.ops.OpCandidate;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputOp;

import org.scijava.convert.ConversionRequest;
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

@Plugin(type = Service.class)
public class DefaultOpResolverService extends AbstractService implements
		OpResolverService {

	@Parameter
	private OpService ops;

	@Parameter
	private ConvertService cs;

	@Parameter
	private OpMatchingService matcher;

	@Override
	public <I> ResolvedOpSet<I> resolve(I input, OpRef<?>... refs) {
		Set<OpRef<?>> pool = new HashSet<OpRef<?>>();
		pool.addAll(Arrays.asList(refs));
		return resolve(input, pool);
	}

	@Override
	public <I, OP extends Op> ResolvedOpSet<I> resolve(final I input,
			final OpRef<OP> opRef) {
		final HashSet<OpRef<?>> set = new HashSet<OpRef<?>>();
		set.add(opRef);
		return resolve(input, set);
	}

	@Override
	public <I, OP extends Op> ResolvedOpSet<I> resolve(final I input,
			final Class<OP> type, final Object... args) {
		return resolve(input, new OpRef<OP>(type, args));
	}

	@Override
	public <I> ResolvedOpSet<I> resolve(final I inputType,
			final Set<OpRef<?>> opPool) {

		@SuppressWarnings("unchecked")
		final SourceOp<I> inputSource = new SourceOp<I>(
				(Class<? extends I>) inputType.getClass());

		final Map<OpRef<?>, CachedModule> modulePool = new HashMap<OpRef<?>, CachedModule>();

		for (final OpRef<?> ref : opPool) {
			try {
				if (null == resolveModule(ref, opPool, inputSource, modulePool)) {
					throw new IllegalArgumentException(
							"Can't compile set of OpRefs!" + " Reason:"
									+ ref.getType().getSimpleName()
									+ " can't be auto-resolved!");
				}

			} catch (ModuleException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		postProcess(modulePool, inputSource);

		return new ResolvedOpSet<I>(inputSource, modulePool, opPool);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <I, O> ResolvedOp<I, O> resolve(final Class<O> outType,
			final I input, final Class<? extends OutputOp> o, Object... args) {
		return this.<I, O> resolve(outType, input, new OpRef(o, args));
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public <I, O> ResolvedOp<I, O> resolve(final Class<O> outType,
			final I input, final OpRef<? extends OutputOp> ref) {
		return new ResolvedOp<I, O>(resolve(input, ref), ref);
	}

	/* Create one update listener */
	private InputUpdateListener createUpdateListener(final Module module,
			final ModuleItem<?> item) {
		return new InputUpdateListener() {

			@Override
			public void update(final Object o) {
				ConversionRequest cr = new ConversionRequest(o, item.getType());

				if (item.getType().isAssignableFrom(o.getClass())) {
					module.setInput(item.getName(), o);
				} else if (cs.supports(cr)) {
					module.setInput(item.getName(), cs.convert(cr));

				}
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

	private void postProcess(final Map<OpRef<?>, ? extends Module> modulePool,
			final SourceOp<?> inputSource) {

		for (final Entry<OpRef<?>, ? extends Module> entry : modulePool
				.entrySet()) {
			final Module module = entry.getValue();

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

				// its an input parameter that we can convert
				ConversionRequest cr = new ConversionRequest(
						inputSource.getType(), type);
				if (type.isAssignableFrom(inputSource.getType())
						|| cs.supports(cr)) {

					inputSource.registerListener(listener);
					// only use input !!ONCE!!
					break;
				}
			}

		}
	}

	// INTERNAL
	private <I> CachedModule resolveModule(final OpRef<?> op,
			final Set<OpRef<?>> opPool, final SourceOp<I> inputSource,
			final Map<OpRef<?>, CachedModule> modulePool)
			throws ModuleException {

		if (modulePool.containsKey(op)) {
			return modulePool.get(op);
		}

		// see if there are any candidates in the set of helpers or features
		// which may also provide parameters
		for (final OpRef<?> ref : opPool) {
			if (op.getType().isAssignableFrom(ref.getType())) {
				try {

					CachedModule module = modulePool.get(ref);
					if (module != null)
						return module;

					Module tmp = ops.module(ref.getType(), ref.getArgs());
					if (tmp != null) {

						// here I need to instantiate the module differently as
						// some of the parameters are already resolved.
						//
						module = checkIfAvailable(ref, tmp.getInfo(),
								modulePool, inputSource, opPool, ref.getArgs());
						if (module != null)
							return module;
					}
				} catch (IllegalArgumentException iae) {
					// Nothing to do
				}
			}
		}

		// get all candidate ops for this module type
		final List<OpCandidate<?>> candidates = createCandidates(op.getType());

		// if there are no canidates, we can't resolve this module (and we fail)
		if (candidates.size() == 0) {
			return null;
		}

		// now: we check for the candidates. A candidate can be used, if all
		// fields can be resolved, given the available set of operations.
		// the only exceptions are special fields which are neither of
		// inputType nor a DescriptorParameterSet.
		for (final OpCandidate<?> candidate : candidates) {
			final CachedModule m = checkIfAvailable(op, candidate.getInfo(),
					modulePool, inputSource, opPool, null);

			if (m != null)
				return m;
		}

		return null;
	}

	/**
	 * @param op
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<OpCandidate<?>> createCandidates(final Class<? extends Op> op) {
		return matcher.findCandidates(new OpRef(op));
	}

	/**
	 * @param op
	 * @return
	 */
	private <O extends Op> OpRef<O> createOpRef(final Class<O> type) {
		return new OpRef<O>(type);
	}

	@SuppressWarnings("unchecked")
	private <I> CachedModule checkIfAvailable(final OpRef<?> parent,
			final ModuleInfo candidate,
			final Map<OpRef<?>, CachedModule> existingModules,
			final SourceOp<I> inputSource, final Set<OpRef<?>> helpers,
			final Object[] param) {

		final List<Object> resolvedParams = new ArrayList<Object>();

		final List<CachedModule> dependencies = new ArrayList<CachedModule>();

		final HashMap<OpRef<?>, CachedModule> tmpCompiledModules = new HashMap<OpRef<?>, CachedModule>(
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
				final Class<? extends Op> opToResolve = (Class<? extends Op>) itemType;

				if (tmpCompiledModules.containsKey(opToResolve)) {
					resolvedParams.add(tmpCompiledModules.get(opToResolve)
							.getDelegateObject());
					dependencies.add(tmpCompiledModules.get(opToResolve));
				} else {
					try {

						CachedModule res = resolveModule(
								createOpRef(opToResolve), helpers, inputSource,
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

			// check if we can use on of the parameters
			if (param != null && param.length > i && param[i] != null
					&& cs.supports(param[i], itemType)) {
				resolvedParams.add(param[i]);
				continue;
			}

			// last option, check if we can convert the input
			if (cs.supports(new ConversionRequest(inputSource.getType(),
					itemType))) {
				resolvedParams.add(itemType);
				continue;
			}

			return null;
		}

		final CachedModule module = new CachedModule(ops.module(
				parent.getType(), resolvedParams.toArray()));

		// set-up graph...
		for (final CachedModule dependency : dependencies) {
			dependency.addSuccessor(module);
			module.addPredecessor(dependency);
		}

		// we build our "tree"
		tmpCompiledModules.put(parent, module);

		// we know that only additional modules are in local map
		for (final Entry<OpRef<?>, CachedModule> entry : tmpCompiledModules
				.entrySet()) {
			existingModules.put(entry.getKey(), entry.getValue());
		}

		return module;
	}

	private class CachedModule implements Module {
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

		@Override
		public String toString() {
			return module.getInfo().getName();
		}
	}
}
