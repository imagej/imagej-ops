package net.imagej.ops.geom.pixelbased;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.scijava.plugin.Parameter;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.Boundary;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Iterator;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondNeighborhood;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.DiamondShape.NeighborhoodsAccessible;
import net.imglib2.algorithm.neighborhood.DiamondShape.NeighborhoodsIterableInterval;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.view.Views;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BoolType;

public class DefaultHullII<T extends BooleanType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements Boundary {

	@Parameter(required=false)
	private Shape shape = new DiamondShape(1);
	
	@Parameter
	private OpService ops;
	
	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createII;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		createII = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
				RandomAccessibleInterval.class, in(), in().randomAccess().get());
	}
	
	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		return createII.calculate(input);
	}

	@Override
	public void compute(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {
		final T value = input.randomAccess().get().createVariable();
		value.set(false);
		IterableInterval<Neighborhood<T>> neighborhoods = shape.neighborhoods(input);
		
		final Cursor<Neighborhood<T>> nhCursor = neighborhoods.localizingCursor();
		final RandomAccess<T> outRA = output.randomAccess();
		
		while (nhCursor.hasNext()) {
			final Neighborhood<T> nh = nhCursor.next();
			final BoolType outside = new BoolType(false);
			nh.forEach(p -> {
				if (p.get()) {
					outside.set(true);
				}
			});
			outRA.setPosition(nhCursor);
			outRA.get().set(outside.get());
		}	
	}

}
