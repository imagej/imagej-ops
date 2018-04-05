/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

/**
 * Interface for objects intended to be reused across multiple threads
 * simultaneously.
 * <p>
 * In contrast to a {@link Parallel} op, which marks an op that executes across
 * multiple threads, a {@code Threadable} object knows how to provide multiple
 * independent versions of itself, each of which will be used from a separate
 * concurrent thread. Note that these versions may be deep copies, shallow
 * copies, or even the same instance every time, depending on the nature of the
 * object.
 * </p>
 * <p>
 * In a nutshell: {@link Parallel} ops make use of {@code Threadable} objects
 * for doing their work in a multithreaded way.
 * </p>
 * <p>
 * The requirement is merely that two threads using their respective object
 * references as intended will not confuse one another's processing. As a rule
 * of thumb, this means they might share read-only state, but not read-writable
 * state (apart from perhaps lazily initialized state).
 * </p>
 * 
 * @author Curtis Rueden
 * @see Parallel
 */
public interface Threadable {

	/**
	 * Gets a reference to an instance of this object which can be used
	 * simultaneously from a second thread while this instance is being used from
	 * "its" thread. This "independent instance" may be a deep copy, a shallow
	 * copy, or even the same instance every time, depending on the nature of the
	 * object.
	 * <p>
	 * It is expected that subclasses which override this method will narrow the
	 * return type appropriately. We do not enforce this at compile time via
	 * recursive generics due to their complexity: they introduce a host of
	 * typing difficulties.
	 * </p>
	 */
	Threadable getIndependentInstance();

}
