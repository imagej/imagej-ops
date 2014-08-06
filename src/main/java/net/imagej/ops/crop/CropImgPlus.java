/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.crop;

import net.imagej.ops.MetadataUtil;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.ImgView;
import net.imglib2.meta.ImgPlus;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz
 * @author Martin Horn
 */
@Plugin(type = Op.class, name = Crop.NAME, attrs = { @Attr(name = "aliases",
	value = Crop.ALIASES) }, priority = Priority.LOW_PRIORITY + 1)
public class CropImgPlus<T extends Type<T>> extends
	AbstractCropRAI<T, ImgPlus<T>>
{

	@Parameter(type = ItemIO.BOTH, required = false)
	private ImgPlus<T> out;

	@Parameter
	private ImgPlus<T> in;

	@Override
	public void run() {
		ImgPlus<T> unpackedIn = in;
		while (unpackedIn.getImg() instanceof ImgPlus) {
			unpackedIn = (ImgPlus<T>) unpackedIn.getImg();
		}

		// if out is not provided, just return a view on the image, else write into
		// the result
		ImgView<T> view = new ImgView<T>(crop(unpackedIn.getImg()), in.factory());
		if (out == null) {
			out = new ImgPlus<T>(view);
		}
		else {
			// TODO: might be optimized here (using two cursors etc.)
			Cursor<T> outC = out.cursor();
			RandomAccess<T> viewRA = view.randomAccess();
			while (outC.hasNext()) {
				outC.fwd();
				viewRA.setPosition(outC);
				outC.get().set(viewRA.get());
			}
		}

		MetadataUtil.copyAndCleanImgPlusMetadata(interval, in, out);
	}
}
