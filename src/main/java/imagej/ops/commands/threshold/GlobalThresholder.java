
package imagej.ops.commands.threshold;

import imagej.command.Command;
import imagej.ops.slicer.SlicingService;
import imagej.ops.threshold.ThresholdMethod;
import net.imglib2.Axis;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * TODO: should actually live in a different package!! OR: can this be
 * auto-generated?? (e.g. based on other plugin annotations)
 */
@Plugin(type = Command.class, menuPath = "Image > Threshold > Apply Threshold")
public class GlobalThresholder<T extends RealType<T>> implements Command {

	@Parameter
	private ThresholdMethod<T> method;

	// should not be Dataset, DisplayService, ...
	@Parameter
	private ImgPlus<T> src;

	// needs to be created by the pre-processor!
	@Parameter(type = ItemIO.BOTH)
	private ImgPlus<BitType> res;

	// we need another widget for this!!
	@Parameter
	private Axis[] axes;

	@Parameter
	private SlicingService sliceService;

	@Override
	public void run() {

		final imagej.ops.threshold.GlobalThresholder<T> thresholder =
			new imagej.ops.threshold.GlobalThresholder<T>();
		thresholder.setMethod(method);

		sliceService.process(src, res, new int[] { 1, 2 }, thresholder);

	}
}
