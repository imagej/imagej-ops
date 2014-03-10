
package imagej.ops.loop;

import imagej.ops.Op;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of a {@link AbstractInplaceLoop}
 * 
 * @author Christian Dietz
 * @param <I>
 * @param <O>
 */
@Plugin(type = Op.class, name = Loop.NAME)
public class DefaultInplaceLoop<I> extends AbstractInplaceLoop<I> {

	@Override
	public I compute(final I arg) {
		for (int i = 0; i < n; i++) {
			function.compute(arg, arg);
		}
		return arg;
	}
}
