package net.imagej.ops.functionbuilder;

import net.imagej.ops.Op;
import net.imagej.ops.OpRef;

public class OutputOpRef<O> extends OpRef {

	public OutputOpRef(Class<? extends Op> op, Class<O> type,
			final Object... parameters) {
		super(op, parameters);
	}
}
