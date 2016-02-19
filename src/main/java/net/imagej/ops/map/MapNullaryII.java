
package net.imagej.ops.map;

import net.imagej.ops.Ops;
import net.imagej.ops.thread.chunker.ChunkerOp;
import net.imagej.ops.thread.chunker.CursorBasedChunk;
import net.imglib2.IterableInterval;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * A paralleled version of {@link MapNullaryComputer} over an
 * {@link IterableInterval}.
 * 
 * @author Leon Yang
 * @param <O> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY + 1)
public class MapNullaryII<O> extends
	AbstractMapNullaryComputer<O, IterableInterval<O>>
{

	@Override
	public void compute0(IterableInterval<O> output) {
		ops().run(ChunkerOp.class, new CursorBasedChunk() {

			@Override
			public void execute(final int startIndex, final int stepSize,
				final int numSteps)
			{
				Maps.map(output, getOp(), startIndex, stepSize, numSteps);
			}
		}, output.size());
	}

}
