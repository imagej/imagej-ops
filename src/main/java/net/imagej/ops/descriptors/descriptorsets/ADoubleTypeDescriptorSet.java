package net.imagej.ops.descriptors.descriptorsets;

import java.util.Iterator;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.ADescriptorSet;
import net.imglib2.Pair;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;

import org.scijava.Context;
import org.scijava.module.Module;

public abstract class ADoubleTypeDescriptorSet<I> extends ADescriptorSet<I> {

	public ADoubleTypeDescriptorSet(final Context context, final Class<I> type) {
		super(context, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.imagej.ops.descriptors.AbstractDescriptorSet#createIterator(java.
	 * util.Map)
	 * 
	 * TODO: Can be moved to abstract class
	 */
	@Override
	protected Iterator<Pair<String, DoubleType>> createIterator() {
		return new Iterator<Pair<String, DoubleType>>() {

			final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
			final Class<? extends OutputOp<DoubleType>>[] ops = descriptors();

			int idx = 0;

			@Override
			public boolean hasNext() {
				return idx < ops.length;
			}

			@SuppressWarnings("unchecked")
			@Override
			public Pair<String, DoubleType> next() {

				final Module module = compiledModules.get(ops[idx++]);
				final String name = module.getInfo().getLabel();

				module.run();

				return new ValuePair<String, DoubleType>(name,
						((OutputOp<DoubleType>) module.getDelegateObject())
								.getOutput());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"Operation not supported");
			}
		};
	}

	protected abstract Class<? extends OutputOp<DoubleType>>[] descriptors();
}
