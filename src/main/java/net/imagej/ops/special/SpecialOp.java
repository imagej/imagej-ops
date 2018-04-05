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

package net.imagej.ops.special;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.imagej.ops.Initializable;
import net.imagej.ops.Op;
import net.imagej.ops.OpCandidate;
import net.imagej.ops.OpEnvironment;
import net.imagej.ops.OpRef;
import net.imagej.ops.Threadable;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.NullaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.BinaryFunctionOp;
import net.imagej.ops.special.function.NullaryFunctionOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.BinaryHybridCF;
import net.imagej.ops.special.hybrid.BinaryHybridCFI;
import net.imagej.ops.special.hybrid.BinaryHybridCFI1;
import net.imagej.ops.special.hybrid.BinaryHybridCI;
import net.imagej.ops.special.hybrid.BinaryHybridCI1;
import net.imagej.ops.special.hybrid.NullaryHybridCF;
import net.imagej.ops.special.hybrid.UnaryHybridCF;
import net.imagej.ops.special.hybrid.UnaryHybridCFI;
import net.imagej.ops.special.hybrid.UnaryHybridCI;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.UnaryInplaceOp;

import org.scijava.InstantiableException;

/**
 * A <em>special</em> operation is one intended to be used repeatedly from other
 * ops. Such reuse provides additional type safety and performance gains over
 * calling the ops matching engine (i.e., the {@link OpEnvironment#run}
 * methods).
 * <p>
 * Special ops come in three major flavors: <em>computer</em>, <em>function</em>
 * and <em>inplace</em>. In addition, <em>hybrid</em> ops union together
 * <em>computer</em>, <em>function</em> and/or <em>inplace</em> in various
 * combinations.
 * </p>
 * <p>
 * There are three arities currently implemented: {@link NullaryOp},
 * {@link UnaryOp} and {@link BinaryOp}. These arities correspond to the number
 * of <em>typed</em> input parameters. Additional input parameters are allowed,
 * but not strongly typed at the interface level.
 * </p>
 * <p>
 * The following table summarizes the available kinds of special ops:
 * </p>
 * <table style="border-collapse: collapse" border=1 summary="">
 * <tr>
 * <th>Name</th>
 * <th>Summary</th>
 * <th>Stipulations</th>
 * <th style="white-space: nowrap">Output type</th>
 * <th>Arity</th>
 * <th>Class</th>
 * <th>Methods</th>
 * </tr>
 * <tr>
 * <th rowspan=3>computer</th>
 * <td style="vertical-align: top" rowspan=3>An op which computes a result from
 * the given input I, storing the result into the specified preallocated output
 * reference O.</td>
 * <td style="vertical-align: top" rowspan=3>
 * <ul>
 * <li>Mutating the input contents is not allowed.</li>
 * <li>The output and input references must be different (i.e., computers do not
 * work in-place; see <em>inplace</em> below)</li>
 * <li>The output's initial contents must not affect the value of the result.
 * </li>
 * </ul>
 * </td>
 * <td rowspan=3>BOTH</td>
 * <td>0</td>
 * <td>{@link NullaryComputerOp}</td>
 * <td>{@code void compute(O)}</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>{@link UnaryComputerOp}</td>
 * <td>{@code void compute(O, I)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryComputerOp}</td>
 * <td>{@code void compute(O, I1, I2)}</td>
 * </tr>
 * <tr>
 * <th rowspan=3>function</th>
 * <td style="vertical-align: top" rowspan=3>An op which computes a result from
 * the given input I, returning the result as a newly allocated output O.</td>
 * <td style="vertical-align: top" rowspan=3>
 * <ul>
 * <li>Mutating the input contents is not allowed.</li>
 * </ul>
 * </td>
 * <td rowspan=3>OUTPUT</td>
 * <td>0</td>
 * <td>{@link NullaryFunctionOp}</td>
 * <td>{@code O calculate()}</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>{@link UnaryFunctionOp}</td>
 * <td>{@code O calculate(I)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryFunctionOp}</td>
 * <td>{@code O calculate(I1, I2)}</td>
 * </tr>
 * <tr>
 * <th rowspan=3>inplace</th>
 * <td rowspan=3 style="vertical-align: top">An op which mutates the contents of
 * its argument(s) in-place.</td>
 * <td rowspan=3 style="vertical-align: top">-</td>
 * <td rowspan=3>BOTH</td>
 * <td>1</td>
 * <td>{@link UnaryInplaceOp}</td>
 * <td>{@code void mutate(O)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryInplace1Op}</td>
 * <td>{@code void mutate1(O, I2)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryInplaceOp}</td>
 * <td>{@code void mutate1(O, I2)}
 * <br>{@code void mutate2(I1, O)}</td>
 * </tr>
 * <tr>
 * <th rowspan=3>hybrid CF</th>
 * <td style="vertical-align: top" rowspan=3>An op which is capable of behaving
 * as either a <em>computer</em> or as a <em>function</em>, providing the API
 * for both.</td>
 * <td style="vertical-align: top" rowspan=3>Same as <em>computer</em> and
 * <em>function</em> respectively.</td>
 * <td rowspan=3>BOTH (optional)</td>
 * <td>0</td>
 * <td>{@link NullaryHybridCF}</td>
 * <td style="white-space: nowrap">{@code void compute(O)}
 * <br>{@code O calculate()}</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>{@link UnaryHybridCF}</td>
 * <td style="white-space: nowrap">{@code void compute(O, I)}
 * <br>{@code O calculate(I)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryHybridCF}</td>
 * <td style="white-space: nowrap">{@code O calculate(I1, I2)}
 * <br>{@code void compute(O, I1, I2)}</td>
 * </tr>
 * <tr>
 * <th rowspan=3>hybrid CI</th>
 * <td style="vertical-align: top" rowspan=3>An op which is capable of behaving
 * as either a <em>computer</em> or an <em>inplace</em>, providing the API for
 * both.</td>
 * <td style="vertical-align: top" rowspan=3>Same as <em>computer</em> and
 * <em>inplace</em> respectively.</td>
 * <td rowspan=3>BOTH (optional)</td>
 * <td>1</td>
 * <td>{@link UnaryHybridCI}</td>
 * <td style="white-space: nowrap">{@code void compute(I, O)}
 * <br>{@code void mutate(O)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryHybridCI1}</td>
 * <td style="white-space: nowrap">{@code void compute(I1, I2, O)}
 * <br>{@code void mutate1(O, I2)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryHybridCI}</td>
 * <td style="white-space: nowrap">{@code void compute(I1, I2, O)}
 * <br>{@code void mutate1(O, I2)}
 * <br>{@code void mutate2(I1, O)}</td>
 * </tr>
 * <tr>
 * <th rowspan=3>hybrid CFI</th>
 * <td style="vertical-align: top" rowspan=3>An op which is capable of behaving
 * as either a <em>computer</em>, a <em>function</em> or an <em>inplace</em>,
 * providing the API for all three.</td>
 * <td style="vertical-align: top" rowspan=3>Same as <em>computer</em>,
 * <em>function</em> and <em>inplace</em> respectively.</td>
 * <td rowspan=3>BOTH (optional)</td>
 * <td>1</td>
 * <td>{@link UnaryHybridCFI}</td>
 * <td style="white-space: nowrap">{@code void compute(I, O)}
 * <br>{@code O calculate(I)}
 * <br>{@code void mutate(O)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryHybridCFI1}</td>
 * <td style="white-space: nowrap">{@code void compute(I1, I2, O)}
 * <br>{@code O calculate(I1, I2)}
 * <br>{@code void mutate1(O, I2)}</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>{@link BinaryHybridCFI}</td>
 * <td style="white-space: nowrap">{@code void compute(I1, I2, O)}
 * <br>{@code O calculate(I1, I2)}
 * <br>{@code void mutate1(O, I2)}
 * <br>{@code void mutate2(I1, O)}</td>
 * </tr>
 * </table>
 * <p>
 * It is allowed for ops to implement multiple special op types. For example, an
 * op may implement {@link UnaryComputerOp} as well as {@link UnaryInplaceOp},
 * providing the option to compute the result in-place (saving memory) or into a
 * preallocated output reference (preserving the contents of the original input,
 * at the expense of memory).
 * </p>
 * 
 * @author Curtis Rueden
 */
public interface SpecialOp extends Op, Initializable, Threadable {

	/**
	 * Gets the op's number of special input parameters.
	 * <p>
	 * Note that this value may be larger than intuition might dictate in certain
	 * scenarios, because {@link UnaryOp} extends {@link NullaryOp}, and
	 * {@link BinaryOp} extends {@link UnaryOp}. This allows higher-order ops to
	 * be treated as lower-order by holding the extra input parameters constant.
	 * But it also means that e.g. a {@link BinaryComputerOp} which is locally
	 * typed as {@link UnaryComputerOp} will report its arity as 2 rather than 1
	 * as one might expect.
	 * </p>
	 */
	int getArity();

	// -- Threadable methods --

	@Override
	default SpecialOp getIndependentInstance() {
		// NB: We assume the op instance is thread-safe by default.
		// Individual implementations can override this assumption if they
		// have state (such as buffers) that cannot be shared across threads.
		return this;
	}

	// -- Utility methods --

	/**
	 * Gets the best {@link SpecialOp} implementation for the given types and
	 * arguments, populating its inputs.
	 *
	 * @param ops The {@link OpEnvironment} to search for a matching op.
	 * @param opType The {@link Class} of the operation. If multiple
	 *          {@link SpecialOp}s share this type (e.g., the type is an interface
	 *          which multiple {@link SpecialOp}s implement), then the best
	 *          {@link SpecialOp} implementation to use will be selected
	 *          automatically from the type and arguments.
	 * @param specialType The {@link SpecialOp} type to which matches should be
	 *          restricted.
	 * @param outType the type of the op's primary output, or null for any type.
	 * @param args The operation's arguments.
	 * @return A typed {@link SpecialOp} with populated inputs, ready to use.
	 */
	static <S extends SpecialOp, O> S op(final OpEnvironment ops,
		final Class<? extends Op> opType, final Class<S> specialType,
		final Class<O> outType, final Object... args)
	{
		final OpRef ref = OpRef.createTypes(opType, specialType, outType, args);
		@SuppressWarnings("unchecked")
		final S op = (S) ops.op(ref);
		return op;
	}

	static List<OpCandidate> candidates(final OpEnvironment ops,
		final String name, final Class<? extends Op> opType, final int arity,
		final Flavor flavor)
	{
		// look up matching candidates
		final List<Class<?>> types = new ArrayList<>();
		if (opType != null) types.add(opType);
		if (flavor == Flavor.COMPUTER) types.add(NullaryComputerOp.class);
		else if (flavor == Flavor.FUNCTION) types.add(NullaryFunctionOp.class);
		else if (flavor == Flavor.INPLACE) types.add(UnaryInplaceOp.class);
		final OpRef ref = new OpRef(name, types, null);
		return filterArity(ops.matcher().findCandidates(ops, ref), arity);
	}

	/** Extracts a sublist of op candidates with a particular arity. */
	static List<OpCandidate> filterArity(final List<OpCandidate> candidates,
		final int arity)
	{
		if (arity < 0) return candidates;
		return candidates.stream().filter(candidate -> {
			try {
				final Class<?> opClass = candidate.cInfo().loadClass();
				if (!SpecialOp.class.isAssignableFrom(opClass)) return false;
				final Object o = opClass.newInstance();
				final SpecialOp op = (SpecialOp) o;
				return arity == op.getArity();
			}
			catch (final InstantiableException | InstantiationException
					| IllegalAccessException exc)
			{
				// NB: Ignore this problematic op.
				return false;
			}
		}).collect(Collectors.toList());
	}

	// -- Enums --

	/** An enumeration of the primary kinds of special ops. */
	enum Flavor {
			COMPUTER, FUNCTION, INPLACE
	}

}
