package imagej.ops;

import imagej.command.CommandInfo;
import imagej.command.CommandModule;
import imagej.command.CommandModuleItem;
import imagej.module.Module;
import imagej.module.ModuleItem;
import imagej.module.ModuleService;

import java.lang.reflect.Type;

import org.scijava.Context;
import org.scijava.InstantiableException;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.ConversionUtils;

/**
 * Default matching heuristic for {@link Op}s.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = OperationMatcher.class)
public class DefaultOperationMatcher extends AbstractOperationMatcher {

	@Parameter
	private Context context;

	@Parameter
	private OpService opService;

	@Parameter
	private ModuleService moduleService;

	@Parameter
	private LogService log;

	@Override
	public Module match(final CommandInfo info, final String name,
		final Class<? extends Op> type, final Object... args)
	{
		if (name != null && !name.equals(info.getName())) return null;

		// the name matches; now check the class
		final Class<?> opClass;
		try {
			opClass = info.loadClass();
		}
		catch (final InstantiableException exc) {
			log.error("Invalid op: " + info.getClassName());
			return null;
		}
		if (type != null && !type.isAssignableFrom(opClass)) return null;

		// check that each parameter is compatible with its argument
		int i = 0;
		for (final ModuleItem<?> item : info.inputs()) {
			if (i >= args.length) return null; // too few arguments
			final Object arg = args[i++];
			if (!canAssign(arg, item)) return null;
		}
		if (i != args.length) return null; // too many arguments

		// create module and assign the inputs
		final CommandModule module = (CommandModule) createModule(info, args);

		// make sure the op itself is happy with these arguments
		if (Contingent.class.isAssignableFrom(opClass)) {
			final Contingent c = (Contingent) module.getCommand();
			if (!c.conforms()) return null;
		}

		if (log.isDebug()) {
			log.debug("OpService.module(" + name + "): op=" +
				module.getDelegateObject().getClass().getName());
		}

		// found a match!
		return module;
	}

	// -- Helper methods --

	private Module createModule(final CommandInfo info, final Object... args) {
		final Module module = moduleService.createModule(info);
		context.inject(module.getDelegateObject());
		return opService.assignInputs(module, args);
	}

	private boolean canAssign(final Object arg, final ModuleItem<?> item) {
		if (item instanceof CommandModuleItem) {
			final CommandModuleItem<?> commandItem = (CommandModuleItem<?>) item;
			final Type type = commandItem.getField().getGenericType();
			return ConversionUtils.canConvert(arg, type);
		}
		return ConversionUtils.canConvert(arg, item.getType());
	}

}
