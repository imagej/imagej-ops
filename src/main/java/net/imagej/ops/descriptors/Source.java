package net.imagej.ops.descriptors;

import java.util.ArrayList;

import net.imagej.ops.descriptors.DefaultDescriptorService.InputUpdateListener;

public class Source<I> {

	private ArrayList<InputUpdateListener> listeners = new ArrayList<InputUpdateListener>();

	private Class<I> type;

	public Source(final Class<I> type) {
		this.type = type;
	}

	public void update(final I input) {
		for (final InputUpdateListener listener : listeners) {
			listener.update(input);
		}
	}

	public void registerListener(final InputUpdateListener listener) {
		listeners.add(listener);
	}
	public Class<I> getType() {
		return type;
	}
}
