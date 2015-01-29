package net.imagej.ops.features;

import java.util.ArrayList;

import net.imagej.ops.InputOp;

class SourceOp<I> implements InputOp<I> {

		private ArrayList<InputUpdateListener> listeners = new ArrayList<InputUpdateListener>();

		private I input;

		private Class<? extends I> type;

		public SourceOp(final Class<? extends I> type) {
			this.type = type;
		}

		public void setInput(final I input) {
			this.input = input;
		}

		public void registerListener(final InputUpdateListener listener) {
			listeners.add(listener);
		}

		public Class<? extends I> getType() {
			return type;
		}

		public I getInput() {
			return input;
		}

		@Override
		public void run() {
			for (final InputUpdateListener listener : listeners) {
				listener.update(input);
			}
		}
	}
