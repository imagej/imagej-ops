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
package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Op;
import net.imagej.ops.Ops.Zernike.Magnitude;
import net.imagej.ops.Ops.Zernike.Phase;
import net.imagej.ops.features.sets.processors.ComputerSetProcessorUtils;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link ZernikeComputerSet}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = ComputerSet.class, label = "Zernike Computerset")
@SuppressWarnings("rawtypes")
public class DefaultZernikeComputerSet extends AbstractConfigurableComputerSet<Iterable, DoubleType>
		implements ZernikeComputerSet<Iterable, DoubleType> {

	@Parameter(type = ItemIO.INPUT, label = "Minimum Order of Zernike Moment", description = "The minimum order of the zernike moment to be calculated.", min = "1", max = "2147483647", stepSize = "1")
	private int orderMin = 2;

	@Parameter(type = ItemIO.INPUT, label = "Maximum Order of Zernike Moment", description = "The maximum order of the zernike moment to be calculated.", min = "1", max = "2147483647", stepSize = "1")
	private int orderMax = 4;
	
	public DefaultZernikeComputerSet() {
		super(new DoubleType(), Iterable.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Op>[] getComputers() {
		return new Class[] { Phase.class, Magnitude.class };
	}

	@Override
	protected Iterable getFakeInput() {
		return ComputerSetProcessorUtils.get2DImg();
	}

	@Override
	protected void addComputer(final Class<? extends Op> clazz) {
		for (int order = orderMin; order <= orderMax; order++) {
			for (int repetition = 0; repetition <= order; repetition++) {
				if (Math.abs(order - repetition) % 2 == 0) {
					@SuppressWarnings("unchecked")
					final UnaryComputerOp<Iterable, DoubleType> computer = Computers.unary(ops(), clazz,
							(Class<DoubleType>) outputTypeInstance.getClass(), in(), order, repetition);
					final DoubleType output = outputTypeInstance.createVariable();
					namedOutputs.put(getKey(clazz, order, repetition), output);
					computer.setOutput(output);
					computers.put(clazz, computer);
				}
			}
		}
	}

	private String getKey(final Class<? extends Op> clazz, final int order, final int repetition) {
		return clazz.getSimpleName() + " for Order " + order + " and Repetition " + repetition;
	}

	@Override
	public String[] getComputerNames() {
		final List<String> names = new ArrayList<>();
		for (final Class<? extends Op> clazz : getActiveComputers()) {
			for (int order = orderMin; order <= orderMax; order++) {
				for (int repetition = 0; repetition <= order; repetition++) {
					if (Math.abs(order - repetition) % 2 == 0) {
						names.add(getKey(clazz, order, repetition));
					}
				}
			}
		}
		return names.toArray(new String[names.size()]);
	}

}
