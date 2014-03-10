
package imagej.ops.commands.project;

import imagej.command.Command;
import imagej.ops.Function;
import imagej.ops.OpService;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath = "Image > Threshold > Project")
public class Projector<T extends RealType<T>> implements Command {

	@Parameter(type = ItemIO.BOTH)
	private ImgPlus<BitType> res;

	@Parameter
	private ImgPlus<T> src;

	@Parameter(choices = { "min", "max", "sum", "average", "median", "stdev",
		"variance" })
	private Function<Iterable<T>, T> method;

	@Parameter
	private OpService opService;

	@Override
	public void run() {

	}
}
