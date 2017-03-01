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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.imagej.ops.CustomOpEnvironment;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpInfo;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.type.Type;

/**
 * The abstract implementation of {@link ComputerSet}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 * @param <I>
 *            type of the common input
 * @param <O>
 *            type of the common output
 */
public abstract class AbstractComputerSet<I, O extends Type<O>> extends AbstractUnaryFunctionOp<I, Map<String, O>>
		implements ComputerSet<I, O> {

	/**
	 * The computers of this {@link ComputerSet}.
	 */
	protected final Map<Class<? extends Op>, UnaryComputerOp<I, O>> computers;

	/**
	 * The outputs by name of all computers.
	 */
	protected final Map<String, O> namedOutputs;

	/**
	 * An instance of the output type.
	 */
	protected final O outputTypeInstance;

	/**
	 * The input type.
	 */
	private final Class<I> inType;

	/**
	 * Create a new {@link AbstractComputerSet} with the default
	 * {@link OpEnvironment}.
	 *
	 * @param outputTypeInstance
	 *            object of the output type
	 * @param inType
	 *            the input type
	 */
	public AbstractComputerSet(final O outputTypeInstance, final Class<I> inType) {
		this(null, outputTypeInstance, inType);
	}

	/**
	 * Create a new {@link AbstractComputerSet} with a custom
	 * {@link OpEnvironment}.
	 *
	 * @param opEnv
	 *            the custom {@link OpEnvironment}
	 * @param outputTypeInstance
	 *            object of the output type
	 * @param inType
	 *            the input type
	 */
	public AbstractComputerSet(final OpEnvironment opEnv, final O outputTypeInstance, final Class<I> inType) {
		if (opEnv != null) {
			setEnvironment(opEnv);
		}

		this.outputTypeInstance = outputTypeInstance;
		this.inType = inType;
		this.computers = new HashMap<>();
		this.namedOutputs = new HashMap<>();
	}

	/**
	 * TEMP
	 * TODO: https://github.com/imagej/imagej-ops/issues/351
	 *
	 * A fake input is need for the matching of Ops. This can be removed if the
	 * matcher gets improved.
	 *
	 * @return an empty input object.
	 */
	protected abstract I getFakeInput();

	@Override
	public void initialize() {
		setInput(getFakeInput());
		initialize(null);
	}

	/**
	 * Set {@link CustomOpEnvironment} and create all computers of this
	 * {@link ComputerSet}.
	 *
	 * @param infos
	 *            for the {@link CustomOpEnvironment}
	 */
	protected void initialize(final Collection<OpInfo> infos) {
		if (infos != null) {
			setEnvironment(new CustomOpEnvironment(ops(), infos));
		}
		for (final Class<? extends Op> computer : getComputers()) {
			addComputer(computer);
		}
	}

	/**
	 * Create instance and output object for the {@link Computers}. Add both to the
	 * datastructures.
	 *
	 * @param clazz
	 *            the class of the {@link Computers}
	 */
	protected void addComputer(final Class<? extends Op> clazz) {
		@SuppressWarnings("unchecked")
		final UnaryComputerOp<I, O> computer = Computers.unary(ops(), clazz, (Class<O>) outputTypeInstance.getClass(),
				in());
		final O output = outputTypeInstance.createVariable();
		namedOutputs.put(clazz.getSimpleName(), output);
		computer.setOutput(output);
		computers.put(clazz, computer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, O> calculate(I input) {
		computers.values().parallelStream().forEach(c -> c.compute(input, c.out()));
		
		return namedOutputs;
	}
	
	@Override
	public List<Class<? extends Op>> getActiveComputers() {
		return Arrays.asList(getComputers());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getComputerNames() {
		return Arrays.asList(getComputers()).stream().map(a -> a.getSimpleName()).collect(Collectors.toList())
				.toArray(new String[computers.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<I> getInType() {
		return inType;
	}
}
