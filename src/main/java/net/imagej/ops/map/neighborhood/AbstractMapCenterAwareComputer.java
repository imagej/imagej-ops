/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.map.neighborhood;

import net.imagej.ops.map.MapComputer;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of a {@link MapComputer} for {@link CenterAwareComputerOp}.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <A> mapped on {@code <B>}
 * @param <B> mapped from {@code <A>}
 * @param <C> provides {@code <A>}s
 * @param <D> provides {@code <B>}s
 */
public abstract class AbstractMapCenterAwareComputer<A, B, C, D> extends
	AbstractUnaryComputerOp<C, D> implements
	MapComputer<Pair<A, Iterable<A>>, B, CenterAwareComputerOp<A, B>>
{

	@Parameter
	private CenterAwareComputerOp<A, B> op;

	@Override
	public CenterAwareComputerOp<A, B> getOp() {
		return op;
	}

	@Override
	public void setOp(final CenterAwareComputerOp<A, B> op) {
		this.op = op;
	}
}
