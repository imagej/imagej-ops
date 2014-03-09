
package imagej.ops.threading;

import imagej.Cancelable;
import imagej.ops.Op;

/**
 * ChunkExecutor to execute chunks of data.
 * 
 * @author Christian Dietz
 */
public interface ChunkExecutor extends Op, Cancelable {

	void setChunkExecutable(final ChunkExecutable definition);

	void setTotalSize(final int totalSize);
}
