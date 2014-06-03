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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.descriptors.DefaultDescriptorService.InputUpdateListener;

import org.scijava.module.MethodCallException;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;

/**
 * Cached Module for Descriptors. TODO: Clean-Up TODO: JavaDoc
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 */
class CachedDescriptorModule implements Module {

	private ArrayList<CachedDescriptorModule> successors = new ArrayList<CachedDescriptorModule>();

	private ArrayList<CachedDescriptorModule> predeccessors = new ArrayList<CachedDescriptorModule>();

	private final Map<ModuleItem<?>, Set<InputUpdateListener>> outputReceivers = new HashMap<ModuleItem<?>, Set<InputUpdateListener>>();

	private final Module module;

	public CachedDescriptorModule(final Module module) {
		this.module = module;

		System.out.println(module);
	}

	boolean dirty = true;

	@Override
	public void run() {
		if (dirty) {
			runPredeccessors();
			module.run();

			for (final Entry<ModuleItem<?>, Set<InputUpdateListener>> entry : outputReceivers
					.entrySet()) {

				// update the listeners if there are any
				for (final InputUpdateListener listener : entry.getValue()) {
					listener.update(module.getOutput(entry.getKey().getName()));
				}
			}
			dirty = false;
		}
	}

	void markDirty() {
		dirty = true;
		notifySuccessors();
	}

	private void notifySuccessors() {
		for (final CachedDescriptorModule op : successors) {
			op.markDirty();
		}
	}

	private void runPredeccessors() {
		for (final CachedDescriptorModule module : predeccessors) {
			module.run();
		}
	}

	public void addSuccessor(final CachedDescriptorModule op) {
		successors.add(op);
	}

	public void addPredecessor(final CachedDescriptorModule op) {
		predeccessors.add(op);
	}

	boolean isDirty() {
		return dirty;
	}

	@Override
	public void preview() {
		module.preview();
	}

	@Override
	public void cancel() {
		module.cancel();
	}

	@Override
	public void initialize() throws MethodCallException {
		module.initialize();
	}

	@Override
	public ModuleInfo getInfo() {
		return module.getInfo();
	}

	@Override
	public Object getDelegateObject() {
		return module.getDelegateObject();
	}

	@Override
	public Object getInput(final String name) {
		return module.getInput(name);
	}

	@Override
	public Object getOutput(final String name) {
		return module.getOutput(name);
	}

	@Override
	public Map<String, Object> getInputs() {
		return module.getInputs();
	}

	@Override
	public Map<String, Object> getOutputs() {
		return module.getOutputs();
	}

	@Override
	public void setInput(final String name, final Object value) {
		markDirty();
		module.setInput(name, value);
	}

	@Override
	public void setOutput(final String name, final Object value) {
		module.setOutput(name, value);
	}

	@Override
	public void setInputs(final Map<String, Object> inputs) {
		for (final Entry<String, Object> entry : inputs.entrySet()) {
			setInput(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void setOutputs(final Map<String, Object> outputs) {
		module.setOutputs(outputs);
	}

	@Override
	public boolean isResolved(final String name) {
		return module.isResolved(name);
	}

	@Override
	public void setResolved(final String name, final boolean resolved) {
		module.setResolved(name, resolved);
	}

	public void registerOutputReceiver(final ModuleItem<?> item,
			final InputUpdateListener listener) {
		Set<InputUpdateListener> listeners = outputReceivers.get(item);
		if (listeners == null) {
			listeners = new HashSet<InputUpdateListener>();
			outputReceivers.put(item, listeners);
		}
		listeners.add(listener);
	}

	@Override
	public String toString() {
		return module.getInfo().getName();
	}
}