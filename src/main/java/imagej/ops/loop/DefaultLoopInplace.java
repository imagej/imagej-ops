
package imagej.ops.loop;

import imagej.ops.Op;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of a {@link AbstractLoopInplace}
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Loop.NAME)
public class DefaultLoopInplace<I> extends AbstractLoopInplace<I> {

	@Override
	public I compute(final I arg) {
		for (int i = 0; i < n; i++) {
			function.compute(arg, arg);
		}
		return arg;
	}
}
