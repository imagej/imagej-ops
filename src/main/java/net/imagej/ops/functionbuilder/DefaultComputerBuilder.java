package net.imagej.ops.functionbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.Op;
import net.imagej.ops.OpMatchingService;
import net.imagej.ops.OpRef;
import net.imagej.ops.OpService;

import org.scijava.convert.ConvertService;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

@Plugin(type = Service.class)
public class DefaultComputerBuilder extends AbstractService implements
		ComputerBuilder {

	@Parameter
	private OpService ops;

	@Parameter
	private ConvertService cs;

	@Parameter
	private OpMatchingService matcher;

	@Override
	public <I, O> Computer<I, O> build(final OpRef opType,
			final Class<O> outputType, final Class<I> inputType,
			OpRef... opPool) {
		final HashSet<OpRef> types = new HashSet<OpRef>(1);
		types.add(opType);

		return new BuiltComputer<I, O>(build(types, outputType, inputType,
				opPool));
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

	private void postProcess(final Map<Integer, CachedModule> modulePool,
			final SourceOp<?> inputSource) {

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

	// INTERNAL
	private <I> CachedModule resolveModule(final OpRef op,
			final Set<OpRef> helpers, final SourceOp<I> inputSource,
			final Map<Integer, CachedModule> modulePool) throws ModuleException {

		if (modulePool.containsKey(op.hashCode())) {
			return modulePool.get(op.hashCode());
		}

		// see if there are any candidates in the set of helpers or features
		// which may also provide parameters
		for (final OpRef info : helpers) {
			if (op.getType().isAssignableFrom(info.getType())) {
				try {
					Module tmp = matcher.findModule(null, info.getType(),
							info.getParameters());
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
				op.getType());

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

	private <I> CachedModule checkIfAvailable(final OpRef parent,
			final ModuleInfo candidate,
			final Map<Integer, CachedModule> existingModules,
			final SourceOp<I> inputSource, final Set<OpRef> helpers,
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
								new OpRef(opToResolve), helpers, inputSource,
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
				parent.getType(), resolvedParams.toArray()));

		// set-up graph...
		for (final CachedModule dependency : dependencies) {
			dependency.addSuccessor(module);
			module.addPredecessor(dependency);
		}

		// we build our "tree"
		tmpCompiledModules.put(new OpRef(parent.getType()).hashCode(), module);

		// we know that only additional modules are in local map
		for (final Entry<Integer, CachedModule> entry : tmpCompiledModules
				.entrySet()) {
			existingModules.put(entry.getKey(), entry.getValue());
		}

		return module;
	}

	@Override
	public <I, O> Computer<I, List<O>> build(final Set<OpRef> opTypes,
			final Class<O> outType, final Class<I> inputType,
			final OpRef... opPool) {
		final SourceOp<I> inputSource = new SourceOp<I>(inputType);

		final OpRef[] opTypesAsRef = new OpRef[opTypes.size()];

		int i = 0;
		for (final OpRef op : opTypes) {
			opTypesAsRef[i++] = op;
		}

		final Map<Integer, CachedModule> modulePool = new HashMap<Integer, CachedModule>();

		final Set<OpRef> allOps = new HashSet<OpRef>();
		allOps.addAll(Arrays.asList(opTypesAsRef));
		allOps.addAll(Arrays.asList(opPool));

		for (final OpRef ref : opTypesAsRef) {
			try {
				if (null == resolveModule(ref, allOps, inputSource, modulePool)) {
					throw new IllegalArgumentException(
							"Can't compile set of FeatureInfos!" + " Reason:"
									+ ref.getType().getSimpleName()
									+ " can't be instantiated!");
				}

			} catch (ModuleException e) {
				e.printStackTrace();
			}
		}

		postProcess(modulePool, inputSource);

		final CachedModule[] outputOps = new CachedModule[opTypesAsRef.length];
		i = 0;
		for (final OpRef ref : opTypesAsRef) {
			outputOps[i++] = (CachedModule) modulePool.get(ref.hashCode());
		}

		return new OutputComputer<I, List<O>>(inputSource, new ListOutputOp<O>(
				outputOps));
	}
}
