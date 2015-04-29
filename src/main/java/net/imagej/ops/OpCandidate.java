/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

package net.imagej.ops;

import org.scijava.module.Module;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;

/**
 * Container class for a possible operation match between an {@link OpRef} and a
 * {@link ModuleInfo}, as computed by the {@link OpMatchingService}.
 * 
 * @author Curtis Rueden
 * @param <OP> The type of {@link Op}.
 * @see OpMatchingService
 */
public class OpCandidate<OP extends Op> {

	public static enum StatusCode {
		MATCH,
		INVALID_MODULE,
		TOO_MANY_ARGS,
		TOO_FEW_ARGS,
		REQUIRED_ARG_IS_NULL,
		CANNOT_CONVERT,
		DOES_NOT_CONFORM,
		OTHER
	}

	private final OpRef<OP> ref;
	private final ModuleInfo info;

	private Module module;
	private StatusCode code;
	private String message;
	private ModuleItem<?> item;

	public OpCandidate(final OpRef<OP> ref, final ModuleInfo info) {
		this.ref = ref;
		this.info = info;
	}

	/** Gets the op reference describing the desired match. */
	public OpRef<OP> getRef() {
		return ref;
	}

	/** Gets the module info describing the op to match against. */
	public ModuleInfo getInfo() {
		return info;
	}

	/** Sets the module instance associated with the attempted match. */
	public void setModule(final Module module) {
		this.module = module;
	}

	/** Gets the module instance associated with the attempted match. */
	public Module getModule() {
		return module;
	}

	/** Sets the status of the matching attempt. */
	public void setStatus(final StatusCode code) {
		setStatus(code, null, null);
	}

	/** Sets the status of the matching attempt. */
	public void setStatus(final StatusCode code, final String message) {
		setStatus(code, message, null);
	}

	/** Sets the status of the matching. */
	public void setStatus(final StatusCode code, final String message,
		final ModuleItem<?> item)
	{
		this.code = code;
		this.message = message;
		this.item = item;
	}

	/** Gets the matching status code. */
	public StatusCode getStatusCode() {
		return code;
	}

	/** Gets a message elaborating on the matching status, if any. */
	public String getStatusMessage() {
		return message;
	}

	/**
	 * Gets the status item related to the matching status, if any. Typically, if
	 * set, this is the parameter for which matching failed.
	 */
	public ModuleItem<?> getStatusItem() {
		return item;
	}

	/** Gets a descriptive status message in human readable form. */
	public String getStatus() {
		final StatusCode statusCode = getStatusCode();
		if (statusCode == null) return null;

		final StringBuilder sb = new StringBuilder();
		switch (statusCode) {
			case MATCH:
				sb.append("MATCH");
				break;
			case INVALID_MODULE:
				sb.append("Invalid module: " + info.getDelegateClassName());
				break;
			case TOO_MANY_ARGS:
				sb.append("Too many arguments");
				break;
			case TOO_FEW_ARGS:
				sb.append("Not enough arguments");
				break;
			case REQUIRED_ARG_IS_NULL:
				sb.append("Missing required argument");
				break;
			case CANNOT_CONVERT:
				sb.append("Inconvertible type");
				break;
			case DOES_NOT_CONFORM:
				sb.append("Inputs do not conform to op rules");
				break;
			default:
				return getStatusMessage();
		}
		final String msg = getStatusMessage();
		if (msg != null) sb.append(": " + msg);

		return sb.toString();
	}

	@Override
	public String toString() {
		return OpUtils.opString(getInfo());
	}
}
