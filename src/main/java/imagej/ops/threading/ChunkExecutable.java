
package imagej.ops.threading;

public interface ChunkExecutable {

	void execute(int min, int stepSize, int numSteps);

}
