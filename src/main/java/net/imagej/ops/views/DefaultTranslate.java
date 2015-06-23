package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Translate;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.Translate.class, name = ViewOps.Translate.NAME)
public class DefaultTranslate<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		Translate<RandomAccessible<T>, MixedTransformView<T>>{

	@Parameter(type = ItemIO.INPUT)
	private long[] translation;
	
	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.translate(input, translation);
	}

}
