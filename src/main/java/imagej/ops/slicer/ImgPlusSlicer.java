
package imagej.ops.slicer;

import imagej.ops.MetadataUtil;
import imagej.ops.Op;
import net.imglib2.Interval;
import net.imglib2.img.ImgView;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "hyperslicer")
public class ImgPlusSlicer<T extends Type<T>> extends AbstractHyperSlicer {

	@Parameter
	private Interval interval;

	@Parameter
	private ImgPlus<T> in;

	@Parameter(type = ItemIO.OUTPUT)
	private ImgPlus<T> out;

	@Override
	public void run() {
		ImgPlus<T> unpackedIn = in;
		while (unpackedIn.getImg() instanceof ImgPlus) {
			unpackedIn = (ImgPlus<T>) unpackedIn.getImg();
		}

		out =
			new ImgPlus<T>(new ImgView<T>(hyperSlice(unpackedIn.getImg(), interval),
				in.factory()));

		MetadataUtil.copyAndCleanImgPlusMetadata(interval, in, out);
	}

}
