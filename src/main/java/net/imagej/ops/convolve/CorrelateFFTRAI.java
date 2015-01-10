package net.imagej.ops.convolve;

import org.scijava.plugin.Plugin;

import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import net.imagej.ops.Op;
import net.imagej.ops.fft.filter.LinearFFTFilterRAI;

/**
 * Correlate op for (@link RandomAccessibleInterval)
 * 
 * @author bnorthan
 * 
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class)
public class CorrelateFFTRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		extends LinearFFTFilterRAI<I, O, K, C> {

	/**
	 * Perform correlation by conjugate multiplying the FFTs in the frequency
	 * domain
	 * 
	 * TODO use an op here??
	 */
	protected void frequencyOperation(Img<C> a, Img<C> b) {
		final Cursor<C> cursorA = a.cursor();
		final Cursor<C> cursorB = b.cursor();

		while (cursorA.hasNext()) {
			cursorA.fwd();
			cursorB.fwd();

			C temp = a.firstElement().createVariable();
			temp.set(cursorB.get());
			temp.complexConjugate();

			cursorA.get().mul(cursorB.get());
		}
	}
}
