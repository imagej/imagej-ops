package net.imagej.ops.geom;

import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RealLocalizable;

public class IIVoidToIRealLocalizableWrapper implements Iterable<RealLocalizable> {

	final Cursor<Void> cursor;
	
	public IIVoidToIRealLocalizableWrapper(final IterableInterval<Void> input) {
		this.cursor = input.localizingCursor();
	}
	
	@Override
	public Iterator<RealLocalizable> iterator() {
		return new Iterator<RealLocalizable>() {

			@Override
			public boolean hasNext() {
				return cursor.hasNext();
			}

			@Override
			public RealLocalizable next() {
				cursor.next();
				return new RealLocalizable() {
					
					@Override
					public int numDimensions() {
						return cursor.numDimensions();
					}
					
					@Override
					public void localize(double[] position) {
						cursor.localize(position);
					}
					
					@Override
					public void localize(float[] position) {
						cursor.localize(position);
					}
					
					@Override
					public float getFloatPosition(int d) {
						return cursor.getFloatPosition(d);
					}
					
					@Override
					public double getDoublePosition(int d) {
						return cursor.getDoublePosition(d);
					}
				};
			}
		};
	}

}
