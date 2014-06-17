package net.imagej.ops.descriptors.descriptorsets;

import java.awt.Polygon;
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
	addOp(FeretsDiameter.class);
	addOp(FeretsAngle.class);
	addOp(ConvexHull.class);
	addOp(Perimeter.class);
	addOp(Circularity.class);
    }

    @Override
    protected Iterator<Pair<String, DoubleType>> createIterator() {
	return new GeometricDescriptorIterator();
    }

    private class GeometricDescriptorIterator implements
    Iterator<Pair<String, DoubleType>> {

	final Map<Class<? extends Op>, Module> compiledModules = getCompiledModules();
	final List<Class<? extends Op>> ops = ops();

	private int opIndex = 0;

	private Module currentModule = null;

	private int opAtIndex = 0;
	private int opAtLimit = 0;

	@Override
	public boolean hasNext() {
	    if (opIndex < ops.size()) {
		return true;
	    }

	    if (opAtIndex < opAtLimit) {
		return true;
	    }

	    return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<String, DoubleType> next() {

	    // if current op is not null, get new element
	    if (currentModule != null && opAtIndex < opAtLimit) {
		if (currentModule.getDelegateObject() instanceof CenterOfGravity) {
		    final OutputOp<double[]> op = (OutputOp<double[]>) currentModule
			    .getDelegateObject();
		    return new ValuePair<String, DoubleType>(currentModule
			    .getInfo().getLabel()
			    + " [Dim "
			    + (opAtIndex)
			    + "] = ", new DoubleType(
				    op.getOutput()[opAtIndex++]));
		} else if (currentModule.getDelegateObject() instanceof ConvexHull) {
		    final int arrayPos = opAtIndex / 2;
		    final OutputOp<Polygon> op = (OutputOp<Polygon>) currentModule
			    .getDelegateObject();

		    ValuePair<String, DoubleType> val;
		    if (opAtIndex % 2 == 0) {
			val = new ValuePair<String, DoubleType>(currentModule
				.getInfo().getLabel()
				+ " P"
				+ arrayPos
				+ " [X] = ", new DoubleType(
					op.getOutput().xpoints[arrayPos]));
		    } else {
			val = new ValuePair<String, DoubleType>(currentModule
				.getInfo().getLabel()
				+ " P"
				+ arrayPos
				+ " [Y] = ", new DoubleType(
					op.getOutput().ypoints[arrayPos]));
		    }
		    ++opAtIndex;
		    return val;
		} else {
		    ++opAtIndex;
		    return new ValuePair<String, DoubleType>(currentModule
			    .getInfo().getLabel(),
			    ((OutputOp<DoubleType>) currentModule
				    .getDelegateObject()).getOutput());
		}
	    } else {

		Class<? extends Op> op = ops.get(opIndex++);

		if (Feret.class.isAssignableFrom(op)) {
		    op = ops.get(opIndex++);
		}

		currentModule = compiledModules.get(op);
		currentModule.run();

		opAtIndex = 0;
		if (CenterOfGravity.class.isAssignableFrom(op)) {
		    opAtLimit = ((CenterOfGravity) currentModule
			    .getDelegateObject()).getOutput().length;
		} else if (Feret.class.isAssignableFrom(op)) {
		    opAtLimit = 6;
		} else if (ConvexHull.class.isAssignableFrom(op)) {
		    opAtLimit = ((ConvexHull) currentModule.getDelegateObject())
			    .getOutput().npoints * 2;
		} else {
		    opAtLimit = 1;
		}
	    }

	    return next();
	}

	@Override
	public void remove() {
	    throw new UnsupportedOperationException("Operation not supported");
	}

    }
}
