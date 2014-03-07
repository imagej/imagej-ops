package imagej.ops;

/**
 * Helper class for multi threading of unary functions
 * 
 * @author Christian Dietz
 * 
 * @param <A>
 * @param <B>
 */
public class UnaryFunctionTask<A, B> implements Runnable {

	private final UnaryFunction<A, B> m_op;

	private final A m_in;

	private final B m_out;

	public UnaryFunctionTask(final UnaryFunction<A, B> op, final A in,
			final B out) {
		m_in = in;
		m_out = out;
		m_op = op.copy();
	}

	@Override
	public void run() {
		m_op.compute(m_in, m_out);
	}
}