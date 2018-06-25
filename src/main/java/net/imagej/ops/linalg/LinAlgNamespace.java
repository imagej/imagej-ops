package net.imagej.ops.linalg;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import org.scijava.plugin.Plugin;

/**
 * The linear algebra namespace has ops for vectors and matrices.
 *
 * @author Richard Domander
 */
@Plugin(type = Namespace.class)
public class LinAlgNamespace extends AbstractNamespace {
	@Override
	public String getName() {
		return "linalg";
	}
}
