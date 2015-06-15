package net.imagej.ops.viewOp;

import net.imagej.ops.Computer;
import net.imagej.ops.ViewOps.View;

public interface DefaultView<I, O> extends View, Computer<I, O> {
	// NB: Marker interface
}
