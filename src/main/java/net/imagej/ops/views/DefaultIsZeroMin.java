package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.Interval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.IsZeroMin.class, name = Ops.View.IsZeroMin.NAME)
public class DefaultIsZeroMin extends AbstractFunctionOp<Interval, Boolean>implements Ops.View.IsZeroMin {

	@Override
	public Boolean compute(Interval input) {
		return Views.isZeroMin(input);
	}

}
