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

package net.imagej.ops.descriptors.descriptorsets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.descriptors.DescriptorService;
import net.imagej.ops.descriptors.Source;
import net.imglib2.Pair;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Context;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;

/**
 * Abstract {@link DescriptorSet} to wrap arbitrary {@link Op}s
 * 
 * TODO: Activate / Deactivate individual descriptors of the descriptor set.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
public abstract class ADescriptorSet<I> implements DescriptorSet {

	private List<Class<? extends Op>> ops = new ArrayList<Class<? extends Op>>();

	private Source<I> inputSource;

	private DescriptorService service;

	private Map<Class<? extends Op>, Module> compiledModules;

	public ADescriptorSet(final Context context, final Class<I> type) {
		service = context.getService(DescriptorService.class);
		inputSource = new Source<I>(type);
	}

	public void update(final I obj) {
		inputSource.update(obj);
	}

	@Override
	public Map<Class<? extends Op>, Module> compile()
			throws IllegalArgumentException, ModuleException {
		return compiledModules = service.compile(ops, inputSource);
	}

	/**
	 * Add Op and (if required) some additional sources to it.
	 * 
	 * @param opClass
	 * @param sources
	 */
	protected void addOp(final Class<? extends Op> opClass) {
		ops.add(opClass);
	}

	@Override
	public Iterator<Pair<String, DoubleType>> iterator() {
		return createIterator();
	}

	private void tryCompile() {
		try {
			if (compiledModules == null) {
				compiledModules = compile();
			}
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (final ModuleException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<Class<? extends Op>, Module> getCompiledModules() {
		tryCompile();
		return compiledModules;
	}

	protected List<Class<? extends Op>> ops() {
		return ops;
	}

	protected abstract Iterator<Pair<String, DoubleType>> createIterator();

}
