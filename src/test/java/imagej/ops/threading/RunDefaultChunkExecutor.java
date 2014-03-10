package imagej.ops.threading;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.Parallel;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "doNothing", priority = Priority.LOW_PRIORITY)
public class RunDefaultChunkExecutor<A extends RealType<A>> extends AbstractFunction<IterableInterval<A>, IterableInterval<A>> implements Parallel {

	@Parameter
	private OpService opService;


	@Override
	public IterableInterval<A> compute(final IterableInterval<A> input,
			final IterableInterval<A> output) {
		
			opService.run(DefaultChunkExecutor.class, new CursorBasedChunkExecutable() {

			@Override
			public void	execute(int startIndex, final int stepSize, final int numSteps)
			{
				final Cursor<A> cursor = input.localizingCursor();
				final Cursor<A> cursorOut = output.localizingCursor();
			
				setToStart(cursor, startIndex);
				setToStart(cursorOut, startIndex);

				int ctr = 0;
				while (ctr < numSteps) {
					cursorOut.get().set(cursor.get());
					
					cursorOut.jumpFwd(stepSize);
					cursor.jumpFwd(stepSize);
					ctr++;
				}
			}
		}, input.size());
	
		return output;
		
	}
}