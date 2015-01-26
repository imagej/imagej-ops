package net.imagej.ops.functionbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.imagej.ops.Op;

import org.scijava.module.MethodCallException;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;

class CachedModule implements Module {
	private ArrayList<CachedModule> successors = new ArrayList<CachedModule>();
	private ArrayList<CachedModule> predeccessors = new ArrayList<CachedModule>();
	private final Map<ModuleItem<?>, Set<InputUpdateListener>> outputReceivers = new HashMap<ModuleItem<?>, Set<InputUpdateListener>>();
	private final Module module;

	public CachedModule(final Module module) {
		this.module = module;
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
		for (final CachedModule op : successors) {
			op.markDirty();
		}
	}

	private void runPredeccessors() {
		for (final CachedModule module : predeccessors) {
			module.run();
		}
	}

	public void addSuccessor(final CachedModule op) {
		successors.add(op);
	}

	public void addPredecessor(final CachedModule op) {
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

	public Op getOp() {
		return (Op) module.getDelegateObject();
	}
}