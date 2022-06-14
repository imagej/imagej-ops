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

import java.util.Objects;

import org.scijava.Context;
import org.scijava.module.AbstractModuleInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;

/**
 * A {@link ModuleInfo} whose execution is the execution of an Op. The executed
 * Op is <b>not</b> identified by this {@link ModuleInfo}; instead, the executed
 * Op is the Op best tailored to the parameters given.
 *
 * @author Gabriel Selzer
 */
public class OpListingInfo extends AbstractModuleInfo {

	private final OpService opService;

	private final OpListing listing;

	/**
	 * Defines an OpListingInfo given an {@link OpListing} and an
	 * {@link OpService} that could find a satisfying Op.
	 *
	 * @param service the {@link OpService} used to match the parameters to the
	 *          Op.
	 * @param listing the {@link OpListing} defining matched Op requirements
	 */
	public OpListingInfo(final OpService service, final OpListing listing) {
		this.opService = service;
		this.listing = listing;
		setName(listing.getName());
	}

	@Override
	protected void parseParameters() {
		listing.inputsFor(this).forEach(this::registerInput);
		listing.outputsFor(this).forEach(this::registerOutput);
	}

	@Override
	public String getDelegateClassName() {
		return listing.getFunctionalType().getName();
	}

	@Override
	public Class<?> loadDelegateClass() throws ClassNotFoundException {
		final String delegateClass = listing.getFunctionalType().getName();
		return Context.getClassLoader().loadClass(delegateClass);
	}

	@Override
	public Module createModule() {
		return new OpListingModule(opService, this);
	}

	@Override
	public boolean equals(final Object that) {
		if (!(that instanceof OpListingInfo)) return false;
		final OpListingInfo thatInfo = (OpListingInfo) that;
		return opService.equals(thatInfo.opService) && listing.equals(
			thatInfo.listing);
	}

	@Override
	public int hashCode() {
		return Objects.hash(opService, listing);
	}

}
