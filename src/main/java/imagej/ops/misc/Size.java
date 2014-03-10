
package imagej.ops.misc;

import imagej.ops.Function;
import net.imglib2.type.numeric.integer.LongType;

public interface Size<I extends Iterable<?>> extends Function<I, LongType> {

	public static final String NAME = "size";
}
