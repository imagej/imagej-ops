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

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Parameter;
import org.scijava.util.ClassUtils;
import org.scijava.util.ConversionUtils;
import org.scijava.util.GenericUtils;

/**
 * Base class for unit testing of namespaces. In particular, this class has
 * functionality to verify the completeness of the namespace's built-in method
 * signatures.
 * 
 * @author Curtis Rueden
 */
public abstract class AbstractNamespaceTest extends AbstractOpTest {

	@Parameter
	private CommandService commandService;

	/**
	 * Checks that the given class's list of {@link OpMethod}-annotated methods
	 * "covers" the available ops, and vice versa.
	 * <p>
	 * This method verifies that all ops with the given name have type-safe
	 * {@link OpMethod}-annotated methods. And vice versa: it verifies that all
	 * the annotated methods could theoretically invoke at least one op
	 * implementation.
	 * </p>
	 * <p>
	 * This method provides a general-purpose verification test which extensions
	 * to OPS can also use to verify their own cache of type-safe methods provided
	 * by their own service(s).
	 * </p>
	 * <p>
	 * The completeness tests are not 100% accurate:
	 * </p>
	 * <ul>
	 * <li>The comparison of method parameters to op parameters is too lenient:
	 * the matching should ideally be exact rather than accepting "compatible"
	 * (i.e., subtype) matches.</li>
	 * <li>There are some limitations to the matching of generic parameters.</li>
	 * <li>When a method is missing, the system generates a suggested code block,
	 * but that code block does not includes only raw type parameters, not
	 * generified type parameters. For details on why, see <a
	 * href="http://stackoverflow.com/q/28143029">this post on StackOverflow</a>.</li>
	 * </ul>
	 * 
	 * @param namespace The namespace of the ops to scrutinize (e.g., "math").
	 * @param namespaceClass Class with the {@link OpMethod}-annotated methods.
	 * @param qName The fully qualified (with namespace) name of the op to verify
	 *          is completely covered.
	 * @see GlobalNamespaceTest Usage examples for global namespace ops.
	 * @see net.imagej.ops.math.MathNamespaceTest Usage examples for math ops.
	 */
	public void assertComplete(final String namespace,
		final Class<?> namespaceClass, final String qName)
	{
		final String opName = stripNamespace(qName);

		// obtain the list of built-in methods
		final List<Method> allMethods =
			ClassUtils.getAnnotatedMethods(namespaceClass, OpMethod.class);

		// obtain the list of ops
		final List<CommandInfo> allOps = matcher.getOps();

		// filter methods and ops to only those with the given name
		final List<Method> methods;
		final List<CommandInfo> opList;
		if (opName == null) {
			methods = allMethods;
			opList = allOps;
		}
		else {
			// filter the methods
			methods = new ArrayList<Method>();
			for (final Method method : allMethods) {
				if (opName.equals(method.getName())) methods.add(method);
			}

			// filter the ops
			opList = new ArrayList<CommandInfo>();
			for (final CommandInfo op : allOps) {
				if (qName.equals(op.getName())) opList.add(op);
			}
		}

		// cross-check them!
		assertComplete(namespace, methods, opList);
	}

	/**
	 * Checks that the given list of methods corresponds to the specified list of
	 * available ops. The test will fail if either:
	 * <ol>
	 * <li>There is a method that does not correspond to an op; or</li>
	 * <li>There is an op that cannot be invoked by any method.</li>
	 * </ol>
	 * <p>
	 * Note that this test does not verify sanity of either priorities or
	 * {@link Contingent} ops. It assumes that if a candidate's types match, there
	 * might be some possibility that it could potentially match in the proper
	 * circumstances.
	 * </p>
	 * 
	 * @param namespace The namespace prefix of the ops in question.
	 * @param methods List of methods.
	 * @param infos List of ops.
	 */
	public void assertComplete(final String namespace,
		final List<Method> methods, final List<? extends ModuleInfo> infos)
	{
		final OpCoverSet coverSet = new OpCoverSet();

		boolean success = true; // whether the test will succeed
		for (final Method method : methods) {
			final String name = method.getName();
			final String qName = namespace == null ? name : namespace + "." + name;

			if (!checkVarArgs(method)) success = false;

			for (final Class<? extends Op> opType : opTypes(method)) {
				if (opType.isInterface()) {
					if (!checkOpIface(method, qName, opType)) success = false;
				}
				else {
					if (!checkOpImpl(method, qName, opType, coverSet)) success = false;
				}
			}
		}

		// verify that all ops have been completely covered
		final StringBuilder missingMessage = new StringBuilder("Missing methods:");
		int missingCount = 0;
		for (final ModuleInfo info : infos) {
			int requiredCount = 0, inputCount = 0;
			for (final ModuleItem<?> input : info.inputs()) {
				if (input.isRequired()) requiredCount++;
				inputCount++;
			}

			for (int argCount = requiredCount; argCount <= inputCount; argCount++) {
				if (!coverSet.contains(info, argCount)) {
					missingMessage.append("\n\n" +
						methodString(info, argCount - requiredCount));
					missingCount++;
				}
			}
		}
		if (missingCount > 0) {
			error(missingMessage.toString());
			success = false;
		}

		assertTrue("Coverage mismatch", success);
	}

	// -- Helper methods --

	/**
	 * Ensures that, if the method's last argument is an array, it was written as
	 * varargs.
	 * <p>
	 * Good: {@code foo(int a, Number... nums)}<br>
	 * Bad: {@code foo(int a, Number[] num)}
	 * </p>
	 */
	private boolean checkVarArgs(final Method method) {
		final Class<?>[] argTypes = method.getParameterTypes();
		if (argTypes.length == 0) return true;
		if (!argTypes[argTypes.length - 1].isArray()) return true;
		if (method.isVarArgs()) return true;
		error("Last argument should be varargs for method:\n\t" + method);
		return false;
	}

	/**
	 * Gets the list of {@link Op} classes associated with the given method via
	 * the {@link OpMethod} annotation.
	 */
	private Set<Class<? extends Op>> opTypes(final Method method) {
		final Set<Class<? extends Op>> opSet = new HashSet<Class<? extends Op>>();
		final OpMethod ann = method.getAnnotation(OpMethod.class);
		if (ann != null) {
			final Class<? extends Op>[] opTypes = ann.ops();
			if (opTypes.length == 0) opSet.add(ann.op());
			for (Class<? extends Op> opType : opTypes) {
				opSet.add(opType);
			}
		}
		return opSet;
	}

	/**
	 * Checks whether the given op interface matches the specified method,
	 * particularly with respect to the interface's {@code NAME} constant.
	 * @param method The method to which the {@link Op} should be compared.
	 * @param qName The fully qualified (with namespace) name of the op.
	 * @param opType The {@link Op} to which the method should be compared.
	 * @return true iff the method and {@link Op} match up.
	 */
	private boolean checkOpIface(final Method method, final String qName,
		final Class<? extends Op> opType)
	{
		try {
			final Field nameField = opType.getField("NAME");
			if (nameField.getType() != String.class) {
				error("Non-String NAME field", opType, method);
				return false;
			}
			final String nameFieldValue = (String) nameField.get(null);
			if (!qName.equals(nameFieldValue)) {
				error("NAME field mismatch", opType, method);
				return false;
			}
		}
		catch (final NoSuchFieldException exc) {
			error("No NAME field", opType, method);
			return false;
		}
		catch (final IllegalAccessException exc) {
			error("Inaccessible NAME field", opType, method);
			return false;
		}

		final Object[] argTypes = method.getParameterTypes();
		// verify that the method argument is "Object..." as expected
		if (argTypes.length != 1 || argTypes[0] != Object[].class) {
			error("Expected single Object... argument", opType, method);
			return false;
		}

		return true;
	}

	/**
	 * Checks whether the given op implementation matches the specified method,
	 * including op name, as well as input and output type parameters.
	 * 
	 * @param method The method to which the {@link Op} should be compared.
	 * @param qName The fully qualified (with namespace) name of the op.
	 * @param opType The {@link Op} to which the method should be compared.
	 * @param coverSet The set of ops which have already matched a method.
	 * @return true iff the method and {@link Op} match up.
	 */
	private boolean checkOpImpl(final Method method, final String qName,
		final Class<? extends Op> opType, final OpCoverSet coverSet)
	{
		// TODO: Type matching needs to be type<->type instead of class<->type.
		// That is, the "special class placeholder" also needs to work with Type.
		// Then we can pass Types here instead of Class instances.
		// final Object[] argTypes = method.getGenericParameterTypes();
		final Object[] argTypes = method.getParameterTypes();
		final OpRef<Op> ref = new OpRef<Op>(qName, null, argTypes);
		final CommandInfo info = commandService.getCommand(opType);
		final OpCandidate<Op> candidate = new OpCandidate<Op>(ref, info);

		// check input types
		if (!inputTypesMatch(candidate)) {
			error("Mismatched inputs", opType, method);
			return false;
		}

		// check output types
		final Type returnType = method.getGenericReturnType();
		if (!outputTypesMatch(returnType, candidate)) {
			error("Mismatched outputs", opType, method);
			return false;
		}

		// mark this op as covered (w.r.t. the given number of args)
		coverSet.add(info, argTypes.length);

		return true;
	}

	private boolean inputTypesMatch(final OpCandidate<Op> candidate) {
		// check for assignment compatibility, including generics
		if (!matcher.typesMatch(candidate)) return false;

		// also check that raw types exactly match
		final Object[] paddedArgs = matcher.padArgs(candidate);
		int i = 0;
		for (final ModuleItem<?> input : candidate.getInfo().inputs()) {
			final Object arg = paddedArgs[i++];
			if (!typeMatches(arg, input.getType())) return false;
		}

		return true;
	}

	private boolean typeMatches(final Object arg, final Class<?> type) {
		if (arg == null) return true;
		// NB: Handle special typed null placeholder.
		final Class<?> argType =
			arg instanceof Class ? ((Class<?>) arg) : arg.getClass();
		return argType == type;
	}

	private boolean outputTypesMatch(final Type returnType,
		final OpCandidate<Op> candidate)
	{
		final List<Type> outTypes = new ArrayList<Type>();
		for (final ModuleItem<?> output : candidate.getInfo().outputs()) {
			outTypes.add(output.getGenericType());
		}
		if (outTypes.size() == 0) return returnType == void.class;

		final Type baseType;
		if (outTypes.size() == 1) baseType = returnType;
		else {
			// multiple return types; so the method return type must be a list
			if (GenericUtils.getClass(returnType) != List.class) return false;
			// use the list's generic type parameter as the base type
			baseType = GenericUtils.getTypeParameter(returnType, List.class, 0);
		}

		for (final Type outType : outTypes) {
			if (!isSuperType(baseType, outType)) return false;
		}

		return true;
	}

	private boolean isSuperType(final Type baseType, final Type subType) {
		// TODO: Handle generics.
		final Class<?> baseClass = GenericUtils.getClass(baseType);
		final Class<?> subClass = GenericUtils.getClass(subType);
		return baseClass.isAssignableFrom(subClass);
	}

	private void error(final String message, final Class<? extends Op> opType,
		final Method method)
	{
		error(message + ":\n\top = " + opType.getName() + "\n\tmethod = " + method);
	}

	private void error(final String message) {
		System.err.println("[ERROR] " + message);
	}

	private String methodString(final ModuleInfo info, final int optionalsToFill)
	{
		final StringBuilder sb = new StringBuilder();

		// outputs
		int outputCount = 0;
		String returnType = "void";
		String castPrefix = "";
		for (final ModuleItem<?> output : info.outputs()) {
			if (++outputCount == 1) {
				returnType = typeString(output);
				castPrefix = "(" + castTypeString(output) + ") ";
			}
			else {
				returnType = "List";
				castPrefix = "(List) ";
				break;
			}
		}

		final String className =
			info.getDelegateClassName().replaceAll("\\$", ".") + ".class";
		sb.append("\t@OpMethod(op = " + className + ")\n");

		final String methodName = stripNamespace(info.getName());
		sb.append("\tpublic " + returnType + " " + methodName + "(");

		// inputs
		boolean first = true;
		int optionalIndex = 0;
		final StringBuilder args = new StringBuilder();
		args.append(className);
		for (final ModuleItem<?> input : info.inputs()) {
			if (!input.isRequired()) {
				// leave off unspecified optional arguments
				if (++optionalIndex > optionalsToFill) continue;
			}
			if (first) first = false;
			else sb.append(", ");
			sb.append("final " + typeString(input) + " " + input.getName());
			args.append(", " + input.getName());
		}
		sb.append(") {\n");
		sb.append("\t\tfinal " + returnType + " result =\n");
		sb.append("\t\t\t" + castPrefix + "ops().run(" + args + ");\n");
		sb.append("\t\treturn result;\n");
		sb.append("\t}");

		return sb.toString();
	}

	private String typeString(final ModuleItem<?> item) {
		return item.getType().getSimpleName();
	}

	private String castTypeString(final ModuleItem<?> item) {
		return ConversionUtils.getNonprimitiveType(item.getType()).getSimpleName();
	}

	private String stripNamespace(final String qName) {
		if (qName == null) return null;
		final int dot = qName.lastIndexOf(".");
		return dot < 0 ? qName : qName.substring(dot + 1);
	}

	/** A data structure which maps each key to a set of values. */
	private static class MultiMap<K, V> extends HashMap<K, Set<V>> {

		public void add(final K key, final V value) {
			Set<V> set = get(key);
			if (set == null) {
				set = new HashSet<V>();
				put(key, set);
			}
			set.add(value);
		}

		public boolean contains(final K key, final V value) {
			final Set<V> set = get(key);
			return set != null && set.contains(value);
		}
	}

	/**
	 * Maps an op implementation (i.e., {@link ModuleInfo}) to a list of integers.
	 * Each integer represents a different number of arguments to the op.
	 */
	public static class OpCoverSet extends MultiMap<ModuleInfo, Integer> {
		// NB: No implementation needed.
	}

}
