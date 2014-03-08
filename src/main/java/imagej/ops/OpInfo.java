
package imagej.ops;

public class OpInfo {

	private final String name;
	private final Class<? extends Op> type;
	private final Object[] args;

	public OpInfo(final String name, final Class<? extends Op> type,
		final Object... args)
	{
		this.name = name;
		this.type = type;
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public Class<? extends Op> getType() {
		return type;
	}

	public Object[] getArgs() {
		return args;
	}

}
