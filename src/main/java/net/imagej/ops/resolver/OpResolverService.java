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

package net.imagej.ops.resolver;

import java.util.Map;
import java.util.Set;

import net.imagej.ops.Computer;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.OutputOp;

import org.scijava.service.Service;

/**
 * Given a set of {@link Op}s, the {@link OpResolverService} will create a
 * directed acyclic graph (DAC) with a single source for the input. The
 * {@link OpResolverService} finds dependencies between {@link Op}s and
 * automatically injects all missing {@link Op}s, which other {@link Op}s
 * potentially need to be executed. Example: A potential 'Mean' operation
 * depends on the 'Area' and 'Sum' {@link Op}s. The 'Mean' {@link Op} is passed
 * to the {@link OpResolverService}, which in turn will try to find suitable
 * 'Area' and 'Sum' {@link Op}s which operate on the given input and inject them
 * to the 'Mean' {@link Op}. The resulting {@link Computer} can then take
 * arbitrary inputs of the same type and with equals properties as the input
 * used for resolving the {@link Op}.
 * 
 * @author Christian Dietz, University of Konstanz
 */
public interface OpResolverService extends Service {

	/**
	 * Resolves a {@link Set} of {@link OpRef}s. The resulting {@link Computer}
	 * will calculate the output of all resolved {@link Op}s. Each computation
	 * results in a {@link Map} from {@link OpRef} to the corresponding {@link Op}
	 * which can be used to access the resolved {@link Op}s and their outputs.
	 * 
	 * @param input representing all the inputs which serve as input of the
	 *          resulting {@link Computer}.
	 * @param opPool {@link OpRef}s of {@link Op}s which will be automatically
	 *          resolved.
	 * @return a {@link Computer}.
	 */
	<I> Computer<I, Map<OpRef<?>, Op>> resolve(final I input,
		final Set<OpRef<?>> opPool);

	/**
	 * Resolves an array of {@link OpRef}s. The resulting {@link Computer} will
	 * calculate the output of all resolved {@link Op}s. Each computation results
	 * in a {@link Map} from {@link OpRef} to the corresponding {@link Op} which
	 * can be used to access the resolved {@link Op}s and their outputs.
	 * 
	 * @param input
	 * @param refs
	 * @return
	 */
	<I> Computer<I, Map<OpRef<?>, Op>> resolve(final I input, OpRef<?>... refs);

	/**
	 * Resolves a single {@link OpRef}. The resulting {@link Computer} can be
	 * updated with some input. The output of the {@link Computer} is an already
	 * resolved and executed {@link Op}.
	 * 
	 * @param input representing all the inputs which serve as input of the
	 *          resulting {@link Computer}.
	 * @param ref a single {@link OpRef} which will be resolved.
	 * @return a {@link Computer}.
	 */
	<I, OP extends Op> Computer<I, OP>
		resolve(final I input, final OpRef<OP> ref);

	/**
	 * Resolves an {@link Op} given the type of the {@link Op}. The resulting
	 * {@link Computer} can be updated with some input. The output of the
	 * {@link Computer} is an already resolved and executed {@link Op}.
	 * 
	 * @param input representing all the inputs which serve as input of the
	 *          resulting {@link Computer}.
	 * @param type type of the {@link Op}
	 * @param args arguments of the {@link Op}
	 * @return a {@link Computer}.
	 */
	<I, OP extends Op> Computer<I, OP> resolve(final I input,
		final Class<OP> type, final Object... args);

	/**
	 * Resolved an {@link OutputOp}. The output of the {@link OutputOp} must be of
	 * type outType. The resulting {@link Computer} returns the output given some
	 * input.
	 * 
	 * @param outType type of the output of the {@link OutputOp}
	 * @param input type of the input
	 * @param type type of the {@link OutputOp}
	 * @param args arguments required to resolve the {@link OutputOp}
	 * @return {@link Computer} computing output given some input
	 */
	@SuppressWarnings("rawtypes")
	<I, O> Computer<I, O> resolve(final Class<O> outType, final I input,
		final Class<? extends OutputOp> type, final Object... args);

	/**
	 * Resolved an {@link OutputOp}. The output of the {@link OutputOp} must be of
	 * type outType. The resulting {@link Computer} returns the output given some
	 * input.
	 * 
	 * @param outType type of the output of the {@link OutputOp}
	 * @param input type of the input
	 * @param ref {@link OpRef} describing the {@link OutputOp}
	 * @param args arguments required to resolve the {@link OutputOp}
	 * @return {@link Computer} computing output given some input
	 */
	@SuppressWarnings("rawtypes")
	<I, O> Computer<I, O> resolve(final Class<O> outType, final I input,
		final OpRef<? extends OutputOp> ref);
}
