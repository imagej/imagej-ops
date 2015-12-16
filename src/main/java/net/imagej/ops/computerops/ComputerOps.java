package net.imagej.ops.computerops;
import net.imagej.ops.ComputerOp;

public final class ComputerOps {

	private ComputerOps() {
		// NB: Prevent instantiation of utility class.
	}

	/** Built-in op interfaces of the logic namespace. */
	public static final class Logic<I,O> {

		private Logic() {
			// NB: Prevent instantiation of utility class.
		}


		/**
		 * Base interface for "computer or" operations.
		 * <p>
		 * Implementing classes should be annotated with:
		 * </p>
		 *
		 * <pre>
		 * @Plugin(type = ComputerOps.Or.class)
		 * </pre>
		 */
		public interface Or<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerOr";
		}

		public interface XOr<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerXOr";
		}

		public interface And<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerAnd";
		}

		public interface Equal<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerEqual";
		}

		public interface LessThan<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerLessThan";
		}

		public interface GreaterThan<I,O> extends ComputerOp<I,O> {
			String NAME = "ComputerGreaterThan";
		}
	}
}