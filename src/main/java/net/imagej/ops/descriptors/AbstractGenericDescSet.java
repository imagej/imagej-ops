/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
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

package net.imagej.ops.descriptors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.descriptors.DefaultDescriptorService.InputUpdateListener;
import net.imglib2.Pair;

import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.plugin.Parameter;

/**
 * Abstract {@link DescriptorSet} to wrap arbitrary {@link Op}s
 * 
 * TODO: Activate / Deactivate individual descriptors of the descriptor set.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public abstract class AbstractGenericDescSet implements DescriptorSet {

	@Parameter
	private DescriptorService service;

	private Pair<List<Module>, List<InputUpdateListener>> compilationInfo;

	private ArrayList<Class<? extends Op>> allOps = new ArrayList<Class<? extends Op>>();

	public AbstractGenericDescSet(final Context context) {
		service = context.getService(DescriptorService.class);
	}

	protected Pair<List<Module>, List<InputUpdateListener>> getCompilationInfo() {
		return compilationInfo;
	}

	@Override
	public void compileFor(final Class<?> inputType) {
		try {
			compilationInfo = service.compile(this, inputType, allOps);
		} catch (ModuleException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(final Object obj) {
		checkStatus();

		for (final InputUpdateListener listener : compilationInfo.getB()) {
			if (listener.listensTo(obj.getClass()))
				listener.update(obj);
		}
	}

	private void checkStatus() {
		if (compilationInfo == null) {
			throw new IllegalStateException(
					"DescriptorSet has not been compiled!");
		}
	}

	@Override
	public Iterator<Pair<String, Double>> iterator() {
		checkStatus();
		return createIterator();
	}

	protected void addOp(Class<? extends Op> op) {
		allOps.add(op);
	}

	protected abstract Iterator<Pair<String, Double>> createIterator();
}
