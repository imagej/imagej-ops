package net.imagej.ops.view;

import net.imagej.ops.OutputOp;
import net.imagej.ops.ViewOps.View;

public interface DefaultView<O> extends View, OutputOp<O> {
	// NB: Marker interface
}
