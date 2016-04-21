package net.imagej.ops.filter.derivative;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.AllPartialDerivatives;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.GenericComposite;
import net.imglib2.view.composite.RealComposite;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Filter.AllPartialDerivatives.class, name = Ops.Filter.AllPartialDerivatives.NAME)
public class PartialDerivativesRAI<T extends RealType<T>, C extends GenericComposite<T>>
		extends AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, CompositeIntervalView<T, RealComposite<T>>>
		implements AllPartialDerivatives {

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] derivativeComputers;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		derivativeComputers = new UnaryFunctionOp[in().numDimensions()];
		for (int i = 0; i < in().numDimensions(); i++) {
			derivativeComputers[i] = RAIs.function(ops(), Ops.Filter.PartialDerivative.class, in(), i);
		}
	}

	@Override
	public CompositeIntervalView<T, RealComposite<T>> compute1(RandomAccessibleInterval<T> input) {
		List<RandomAccessibleInterval<T>> derivativeList = new ArrayList<>();
		for (int i = 0; i < derivativeComputers.length; i++) {
			RandomAccessibleInterval<T> derivative = derivativeComputers[i].compute1(input);
			derivativeList.add(derivative);
		}
		
		RandomAccessibleInterval<T> stacked = Views.stack(derivativeList);
		return Views.collapseReal(stacked);
	}
}
