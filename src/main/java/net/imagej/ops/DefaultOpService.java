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

import java.util.Collection;
import java.util.List;

import org.scijava.Context;
import org.scijava.Prioritized;
import org.scijava.Priority;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandService;
import org.scijava.event.EventService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.plugin.PluginService;
import org.scijava.service.Service;
import org.scijava.util.ClassUtils;

/**
 * Default service for managing and executing {@link Op}s.
 *
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultOpService extends AbstractOpEnvironment implements
	OpService
{

	@Parameter
	private CommandService commandService;

	// -- OpEnvironment methods --

	@Override
	public CommandInfo info(final Op op) {
		return commandService.getCommand(op.getClass());
	}

	@Override
	public Collection<CommandInfo> infos() {
		return commandService.getCommandsOfType(Op.class);
	}

	@Override
	public OpEnvironment parent() {
		return null;
	}

	// -- SingletonService methods --

	@Override
	public Class<Op> getPluginType() {
		return Op.class;
	}

	// -- BEGIN AbstractPTService DUPLICATION --

	@Parameter
	private PluginService pluginService;

	// -- PTService methods --

	@Override
	public PluginService getPluginService() {
		return pluginService;
	}

	@Override
	public List<PluginInfo<Op>> getPlugins() {
		return pluginService.getPluginsOfType(getPluginType());
	}

	@Override
	public <P extends Op> P create(final Class<P> pluginClass) {
		final PluginInfo<Op> opInfo =
			pluginService.getPlugin(pluginClass, getPluginType());
		@SuppressWarnings("unchecked")
		final P plugin = (P) pluginService.createInstance(opInfo);
		return plugin;
	}

	// -- END AbstractPTService DUPLICATION --

	// -- BEGIN AbstractService DUPLICATION --

	/**
	 * A pointer to the service's {@link Context}. Note that for two reasons, the
	 * context is not set in the superclass:
	 * <ol>
	 * <li>As services are initialized, their dependencies are recursively created
	 * and initialized too, which is something that normal context injection does
	 * not handle. I.e., the {@link Context#inject(Object)} method assumes the
	 * context and its associated services have all been initialized already.</li>
	 * <li>Event handler methods must not be registered until after service
	 * initialization is complete (i.e., during {@link #registerEventHandlers()},
	 * after {@link #initialize()}).</li>
	 * </ol>
	 */
	private Context context;

	// -- Service methods --

	@Override
	public void initialize() {
		// NB: Do nothing by default.
	}

	@Override
	public void registerEventHandlers() {
		// TODO: Consider removing this method in scijava-common 3.0.0.
		// Instead, the ServiceHelper could just invoke the lines below directly,
		// and there would be one less boilerplate Service method to implement.
		final EventService eventService = context().getService(EventService.class);
		if (eventService != null) eventService.subscribe(this);
	}

	// -- Contextual methods --

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public void setContext(final Context context) {
		// NB: Do not call super.setContext(Context)!
		// The ServiceHelper populates service parameters.
		// We do this because we need to recursively create and initialize
		// service dependencies, rather than merely injecting existing ones.
		this.context = context;
	}

	// -- Disposable methods --

	@Override
	public void dispose() {
		// NB: Do nothing by default.
	}

	// -- Object methods --

	@Override
	public String toString() {
		return getClass().getName() + " [priority = " + getPriority() + "]";
	}

	// -- END AbstractService DUPLICATION --

	// -- BEGIN AbstractRichPlugin DUPLICATION --

	/** The priority of the plugin. */
	private double priority = Priority.NORMAL_PRIORITY;

	/** The metadata associated with the plugin. */
	private PluginInfo<?> info;

	// -- Prioritized methods --

	@Override
	public double getPriority() {
		return priority;
	}

	@Override
	public void setPriority(final double priority) {
		this.priority = priority;
	}

	// -- HasPluginInfo methods --

	@Override
	public PluginInfo<?> getInfo() {
		return info;
	}

	@Override
	public void setInfo(final PluginInfo<?> info) {
		this.info = info;
	}
	

	// -- Comparable methods --

	@Override
	public int compareTo(final Prioritized that) {
		if (that == null) return 1;

		// compare priorities
		final int priorityCompare = Priority.compare(this, that);
		if (priorityCompare != 0) return priorityCompare;

		// compare classes
		return ClassUtils.compare(getClass(), that.getClass());
	}

	// -- END AbstractRichPlugin DUPLICATION --

}
