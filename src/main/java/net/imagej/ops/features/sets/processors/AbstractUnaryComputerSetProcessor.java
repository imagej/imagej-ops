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
package net.imagej.ops.features.sets.processors;

import java.util.concurrent.ExecutorService;

import net.imagej.ops.features.sets.ComputerSet;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.table.GenericTable;
import net.imglib2.type.Type;

import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.thread.ThreadService;

/**
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <I>
 *            Type of the input object.
 * @param <F>
 *            Inputtype of the {@link ComputerSet}.
 * @param <O>
 *            Outputtype of the {@link ComputerSet}.
 */
public abstract class AbstractUnaryComputerSetProcessor<I, F, O extends Type<O>>
		extends AbstractUnaryHybridCF<I, GenericTable> implements ComputerSetProcessor<I> {

	@Parameter
	protected ComputerSet<F, O>[] computerSets;

	@Parameter
	protected ThreadService ts;

	@Parameter
	protected ConvertService cs;

	protected ExecutorService es;

	@Override
	public void initialize() {
		es = ts.getExecutorService();
	}

}
