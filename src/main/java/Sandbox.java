import net.imagej.ops.OpService;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.numeric.integer.ShortType;

import org.scijava.Context;

public class Sandbox {

	public static void main(String[] args) {
		short[] shorts = {1,2,3,4,5};
		ImgPlus<ShortType> img = new ImgPlus<ShortType>(ArrayImgs.shorts(shorts, new long[]{5}));
		Context c = new Context();
		OpService op = c.getService(OpService.class);

		// this works
//		ShortType copy = img.firstElement().copy();
//		copy.setReal(25);
//		op.add(img, copy);

		// This works
//		op.add(img, (short)25);

		// This does not work
		op.add(img, 25);

		for (int i=0; i<shorts.length; i++) System.out.println(shorts[i]);

	}
}
