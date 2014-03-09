
package imagej.ops.threading;

import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

public abstract class AbstractChunkExecutor implements ChunkExecutor {

	@Parameter
	protected ThreadService threadService;

	@Parameter
	protected ChunkExecutable chunkable;

	@Parameter
	protected long totalSize;

	@Override
	public void setChunkExecutable(final ChunkExecutable definition) {
		this.chunkable = definition;
	}

	@Override
	public void setTotalSize(final int totalSize) {
		this.totalSize = totalSize;
	}
}
