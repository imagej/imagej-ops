/*
 * #%L
 * A framework for reusable algorithms.
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
package imagej.ops.onthefly;

import imagej.ops.Contingent;
import imagej.ops.Op;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.ByteArray;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Uses Javassist to generate optimized versions of the AddOp on-the-fly.
 * 
 * @author Johannes Schindelin
 */
@Plugin(type = Op.class, name = "add", priority = Priority.HIGH_PRIORITY)
public class AddOp implements Op, Contingent {
	@Parameter
	private Object a;

	@Parameter
	private Object b;

	@Parameter
	private Object result;

	@Override
	public void run() {
		final byte[] a = ((ByteArray) ((ArrayImg<?, ?>) this.a).update(null)).getCurrentStorageArray();
		final byte[] b = ((ByteArray) ((ArrayImg<?, ?>) this.b).update(null)).getCurrentStorageArray();
		final byte[] result = ((ByteArray) ((ArrayImg<?, ?>) this.result).update(null)).getCurrentStorageArray();
		for (int i = 0; i < a.length; i++) {
			result[i] = (byte) (a[i] + b[i]);
		}
	}

	@Override
	public boolean conforms() {
		if (result == b && a != b) return false;
		if (a instanceof ArrayImg && b instanceof ArrayImg && result instanceof ArrayImg) {
			final ArrayImg<?, ?> aImg = (ArrayImg<?, ?>) a;
			final ArrayImg<?, ?> bImg = (ArrayImg<?, ?>) b;
			if (!dimensionsMatch(aImg, bImg)) return false;
			final ArrayImg<?, ?> resultImg = (ArrayImg<?, ?>) result;
			if (!dimensionsMatch(aImg, resultImg)) return false;
			final Object aData = aImg.update(null);
			if (!(aData instanceof ByteArray)) return false;
			final Object bData = bImg.update(null);
			if (aData.getClass() != bData.getClass()) return false;
			final Object resultData = resultImg.update(null);
			if (aData.getClass() != resultData.getClass()) return false;
			return true;
		}
		return false;
	}

	private boolean dimensionsMatch(Img<?> aImg, Img<?> bImg) {
		final int numDimensions = aImg.numDimensions();
		if (numDimensions != bImg.numDimensions()) return false;
		for (int i = 0; i < numDimensions; i++) {
			if (aImg.dimension(i) != bImg.dimension(i)) return false;
		}
		return true;
	}
}