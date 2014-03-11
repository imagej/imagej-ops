
package imagej.ops.loop;

import imagej.ops.Function;
import imagej.ops.Op;
import imagej.ops.join.JoinFunctions;

import java.util.ArrayList;

import org.scijava.plugin.Plugin;

/**
 * Applies an {@link Function} numIteration times to an image
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Loop.NAME)
public class DefaultLoopFunction<I> extends
	AbstractLoopFunction<Function<I, I>, I>
{

	@Override
	public I compute(final I input, final I output) {

		final ArrayList<Function<I, I>> functions =
			new ArrayList<Function<I, I>>(n);
		for (int i = 0; i < n; i++)
			functions.add(function);

		final JoinFunctions<I> joinFunctions = new JoinFunctions<I>();
		joinFunctions.setFunctions(functions);

		return joinFunctions.compute(input, output);
	}
}
