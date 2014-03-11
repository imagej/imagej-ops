
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
public class DefaultLoopFunction<A> extends
	AbstractLoopFunction<Function<A, A>, A>
{

	@Override
	public A compute(final A input, final A output) {

		final ArrayList<Function<A, A>> functions =
			new ArrayList<Function<A, A>>(n);
		for (int i = 0; i < n; i++)
			functions.add(function);

		final JoinFunctions<A> joinFunctions = new JoinFunctions<A>();
		joinFunctions.setFunctions(functions);
		joinFunctions.setBuffer(buffer);

		return joinFunctions.compute(input, output);
	}
}
