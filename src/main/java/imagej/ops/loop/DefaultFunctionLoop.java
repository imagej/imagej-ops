
package imagej.ops.loop;

import imagej.ops.Function;
import imagej.ops.Op;

import org.scijava.plugin.Plugin;

/**
 * Applies an {@link Function} numIteration times to an image
 * 
 * @author Christian Dietz
 * @param <I>
 */
@Plugin(type = Op.class, name = Loop.NAME)
public class DefaultFunctionLoop<I> extends
	AbstractFunctionLoop<Function<I, I>, I>
{

	@Override
	public I compute(final I input, final I output) {
		if (n == 1) {
			return function.compute(input, output);
		}

		I tmpOutput = output;
		I tmpInput = buffer;
		I tmp;

		if (n % 2 == 0) {
			tmpOutput = buffer;
			tmpInput = output;
		}

		function.compute(input, tmpOutput);
		for (int i = 0; i < n; i++) {
			tmp = tmpInput;
			tmpInput = tmpOutput;
			tmpOutput = tmp;
			function.compute(tmpInput, tmpOutput);
		}

		return output;
	}
}
