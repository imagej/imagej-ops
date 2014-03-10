package imagej.ops.threading;

import net.imglib2.Cursor;

public abstract class CursorBasedChunkExecutable implements ChunkExecutable {
	
	public static void setToStart(final Cursor<?> c, int startIndex) {
		c.reset();
		c.jumpFwd(startIndex + 1);
	}

}
