package net.imagej.ops.descriptors.descriptorsets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.geometric.Area;
import net.imagej.ops.descriptors.geometric.CenterOfGravity;
import net.imagej.ops.descriptors.geometric.Circularity;
import net.imagej.ops.descriptors.geometric.ConvexHull;
import net.imagej.ops.descriptors.geometric.Eccentricity;
import net.imagej.ops.descriptors.geometric.Feret;
import net.imagej.ops.descriptors.geometric.FeretsAngle;
import net.imagej.ops.descriptors.geometric.FeretsDiameter;
import net.imagej.ops.descriptors.geometric.Perimeter;
import net.imglib2.Pair;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.ValuePair;

import org.scijava.Context;
import org.scijava.module.Module;

public class GeometricDescriptorSet<I> extends ADescriptorSet<I> {

    public GeometricDescriptorSet(final Context context, final Class<I> type) {
	super(context, type);

	addOp(Area.class);
	addOp(CenterOfGravity.class);
	addOp(Eccentricity.class);
	addOp(Feret.class);
	addOp(FeretsAngle.class);
	addOp(FeretsDiameter.class);
	addOp(ConvexHull.class);
	addOp(Perimeter.class);
	addOp(Circularity.class);
    }

    @Override
    protected Iterator<Pair<String, DoubleType>> createIterator() {
	return new Iterator<Pair<String, DoubleType>>() {

	    final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
	    final List<Class<? extends Op>> ops = ops();

	    int idx = 0;

	    @Override
	    public boolean hasNext() {
		return idx < ops.size();
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public ValuePair<String, DoubleType> next() {

		final Module module = compiledModules.get(ops.get(idx++));
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

}
