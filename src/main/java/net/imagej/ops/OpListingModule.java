/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2022 ImageJ2 developers.
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

package net.imagej.ops;

import java.util.ArrayList;
import java.util.List;

import org.scijava.module.AbstractModule;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;

/**
 * A {@link Module} implementation that matches and executes an Op matching an
 * {@link OpListing} on its inputs. Usually generated by an
 * {@link OpListingInfo}.
 *
 * @author Gabriel Selzer
 */
class OpListingModule extends AbstractModule {

	private final OpService opService;
	private final OpListingInfo info;

	public OpListingModule(final OpService opService, final OpListingInfo info) {
		super();
		this.opService = opService;
		this.info = info;
	}

	@Override
	public ModuleInfo getInfo() {
		return info;
	}

	@Override
	public void run() {
		final Object[] args = opArgs();
		final Object out = this.opService.run(info.getName(), args);
		for (final ModuleItem<?> item : info.outputs()) {
			// TODO: What if there are multiple pure outputs?
			if (item.isInput()) continue;
			setOutput(item.getName(), out);
		}
	}

	/**
	 * Combines the inputs and outputs of this {@link Module} into a single array.
	 * As {@link OpService#run(String, Object...)} takes the output first, the
	 * outputs come before the inputs in this array.
	 *
	 * @return an {@link Object} array containing the outputs and inputs.
	 */
	private Object[] opArgs() {
		final List<Object> args = new ArrayList<>();
		// Add preallocated outputs
		for (final ModuleItem<?> item : info.outputs())
			if (item.isInput()) args.add(getOutput(item.getName()));
		for (final ModuleItem<?> item : info.inputs())
			args.add(getInput(item.getName()));
		return args.toArray();
	}
}
