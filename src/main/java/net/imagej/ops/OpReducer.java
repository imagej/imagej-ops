package net.imagej.ops;

import java.util.List;

import org.scijava.plugin.SciJavaPlugin;

/** TODO */
public interface OpReducer extends SciJavaPlugin {

	/** TODO */
	List<OpRef<?>> reduce(OpRef<?> ref);

}
