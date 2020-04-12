package net.imagej.ops.geom.geom3d;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.view.Views;
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

import java.util.concurrent.TimeUnit;

@State( Scope.Thread )
@Fork( 1 )
public class LabelingMarchingCubesBenchmark
{
	private IterableRegion<BoolType> region;
	private RandomAccessibleInterval<BitType> copy;

	@Setup
	public void setup()
	{
		ArrayImg<IntType, IntArray> img = ArrayImgs.ints( 1000, 1000, 100 );
		ImgLabeling<IntType, IntType> labeling = new ImgLabeling<>(img);
		RandomAccess<LabelingType<IntType>> ra = labeling.randomAccess();
		IntType label = new IntType(1);
		ra.setPosition(0, 2);
		for (int x = 0; x < 1000; x++) {
			ra.setPosition(x, 0);
			for (int y = 5; y < 7; y++) {
				ra.setPosition(y, 1);
				ra.get().add(label);
			}
		}
		LabelRegions<IntType> regions = new LabelRegions<>(labeling);
		region = regions.getLabelRegion(label);
		copy = new ArrayImgFactory<>(new BitType()).create(region);
		copy = Views.translate(copy, region.min(0), region.min(1), region.min(2));
		Regions.sample(region, copy).forEach(BitType::setOne);

	}

	@Benchmark
	@BenchmarkMode( Mode.AverageTime )
	@OutputTimeUnit( TimeUnit.MILLISECONDS )
	public void calculateDefault()
	{
		DefaultMarchingCubes< BitType > cubes = new DefaultMarchingCubes<>();
		cubes.calculate( copy );
	}


	@Benchmark
	@BenchmarkMode( Mode.AverageTime )
	@OutputTimeUnit( TimeUnit.MILLISECONDS )
	public void calculateRegions()
	{
		RegionMarchingCubes cubes = new RegionMarchingCubes();

		cubes.calculate( region );
	}

	public static void main( final String... args ) throws RunnerException
	{
		final Options opt = new OptionsBuilder()
				.include( LabelingMarchingCubesBenchmark.class.getSimpleName() )
				.warmupIterations( 4 )
				.measurementIterations( 8 )
				.warmupTime( TimeValue.milliseconds( 1000 ) )
				.measurementTime( TimeValue.milliseconds( 1000 ) )
				.build();
		new Runner( opt ).run();
	}
}
