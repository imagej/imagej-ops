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

package net.imagej.ops;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.scijava.log.LogService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Default implementation of the {@link NoParamService}.
 * 
 * @author Leon Yang
 */
@Plugin(type = Service.class)
public class DefaultNoParamService extends AbstractPTService<NoParam> implements
	NoParamService
{

	@Parameter
	private LogService log;

	private Map<Class<?>, Object> noParams;
	private ClassLoader loader;

	@Override
	public void initialize() {
		noParams = new HashMap<>();
		loader = this.getClass().getClassLoader();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNoParam(final Object param) {
		if (param == null) return null;
		if (param instanceof NoParam) return (T) param;
		if (param instanceof Class) return getNoParam((Class<T>) param);
		return getNoParam(param.getClass());
	}

	@Override
	public <T> T getNoParam(final Class<T> paramClass) {
		if (noParams.containsKey(paramClass)) {
			@SuppressWarnings("unchecked")
			final T noParam = (T) noParams.get(paramClass);
			return noParam;
		}
		final T noParam = createNoParam(paramClass);
		noParams.put(paramClass, noParam);
		return noParam;
	}

	// -- PTService methods --

	@Override
	public Class<NoParam> getPluginType() {
		return NoParam.class;
	}

	// -- Helper methos --

	private <T> T createNoParam(final Class<T> paramClass) {
		if (paramClass.isPrimitive()) return null;
		final InvocationHandler handler = new InvocationHandler() {

			@Override
			public Object invoke(final Object proxy, final Method method,
				final Object[] args) throws Throwable
			{
				throw new NullPointerException("Calling Method of a NoParam.");
			}
		};
		@SuppressWarnings("unchecked")
		T noParam = (T) Proxy.newProxyInstance(loader, proxyInterfaces(paramClass),
			handler);
		return noParam;
	}

	private static Class<?>[] proxyInterfaces(final Class<?> paramClass) {
		final Set<Class<?>> ifaces = new HashSet<>();
		if (paramClass.isInterface()) ifaces.add(paramClass);
		Class<?> curr = paramClass;
		do {
			ifaces.addAll(Arrays.asList(curr.getInterfaces()));
		}
		while ((curr = curr.getSuperclass()) != null);
		final Class<?>[] result = new Class<?>[ifaces.size() + 1];
		result[0] = NoParam.class;
		System.arraycopy(ifaces.toArray(), 0, result, 1, ifaces.size());
		return result;
	}

}
