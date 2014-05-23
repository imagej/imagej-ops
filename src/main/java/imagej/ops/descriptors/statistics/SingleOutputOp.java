
package imagej.ops.descriptors.statistics;

import imagej.ops.Op;

public interface SingleOutputOp<O> extends Op {

	O getOutput();

}
