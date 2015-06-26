package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.IsZeroMin;
import net.imglib2.Interval;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.IsZeroMin.class, name = ViewOps.IsZeroMin.NAME)
public class DefaultIsZeroMin extends AbstractView<Interval, Boolean> implements
		IsZeroMin<Interval, Boolean> {

	@Override
	public Boolean compute(Interval input) {
		return Views.isZeroMin(input);
	}

}
