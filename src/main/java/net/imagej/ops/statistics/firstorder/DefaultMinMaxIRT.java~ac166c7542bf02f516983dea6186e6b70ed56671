/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.statistics.firstorder;

import java.util.Iterator;

import net.imagej.ops.statistics.FirstOrderOps.MinMax;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the minimum and maximum value of an {@link Iterable} of type
 * {@link RealType}
 * 
 * @author Martin Horn
 */
@Plugin(type = MinMax.class, name = MinMax.NAME, label = MinMax.LABEL, priority = Priority.LOW_PRIORITY)
public class DefaultMinMaxIRT<T extends RealType<T>> implements MinMax {

	@Parameter(type = ItemIO.INPUT)
	private Iterable<T> img;

	@Parameter(type = ItemIO.OUTPUT)
	private T min;

	@Parameter(type = ItemIO.OUTPUT)
	private T max;

	@Override
	public void run() {
		min = img.iterator().next().createVariable();
		max = min.copy();

		min.setReal(min.getMaxValue());
		max.setReal(max.getMinValue());

		final Iterator<T> it = img.iterator();
		while (it.hasNext()) {
			final T i = it.next();
			if (min.compareTo(i) > 0)
				min.set(i);
			if (max.compareTo(i) < 0)
				max.set(i);
		}
	}
}
