package net.imagej.ops.geom.geom3d;

import java.util.concurrent.TimeUnit;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@State( Scope.Thread )
@Fork( 1 )
public class DefaultMarchingCubesBenchmark
{
	public Img< BitType > img;

	public DefaultMarchingCubes< BitType > cubes;

	@Setup
	public void setup()
	{
		img = ArrayImgs.bits( 100, 100, 100 );
		RandomAccess< BitType > ra = img.randomAccess();
		for ( int x = 0; x < 100; x++ )
		{
			ra.setPosition( x, 0 );
			for ( int y = 5; y < 7; y++ )
			{
				ra.setPosition( y, 1 );
				for ( int z = 5; z < 7; z++ )
				{
					ra.setPosition( z, 2 );
					ra.get().setOne();
				}
			}
		}

		cubes = new DefaultMarchingCubes<>();
	}

	@Benchmark
	@BenchmarkMode( Mode.AverageTime )
	@OutputTimeUnit( TimeUnit.MILLISECONDS )
	public void calculate()
	{
		cubes.calculate( img );
	}

	public static void main( final String... args ) throws RunnerException
	{
		final Options opt = new OptionsBuilder()
				.include( DefaultMarchingCubesBenchmark.class.getSimpleName() )
				.warmupIterations( 4 )
				.measurementIterations( 8 )
				.warmupTime( TimeValue.milliseconds( 1000 ) )
				.measurementTime( TimeValue.milliseconds( 1000 ) )
				.build();
		new Runner( opt ).run();
	}
}
