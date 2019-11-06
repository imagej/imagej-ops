package net.imagej.ops.coloc.saca;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

/**
 * Adapted from Shulei's original Java code for AdaptiveSmoothedKendallTau from his RKColocal R package.
 * (https://github.com/lakerwsl/RKColocal/blob/master/RKColocal_0.0.1.0000.tar.gz)
 * 
 * @author Shulei Wang
 * @author Curtis Rueden
 * @author Ellen T Arena
 */
public class AdaptiveSmoothedKendallTau<I extends RealType<I>, T extends RealType<T>, O extends RealType<O>> {

	public RandomAccessibleInterval<I> image1;
	public RandomAccessibleInterval<I> image2;
	public I thres1;
	public I thres2;
	private double Dn;
	private int TL;
	private int TU;
	private double Lambda;
	private List<RandomAccessibleInterval<T>> stop;
	private Function<RandomAccessibleInterval<I>, RandomAccessibleInterval<T>> factory;
	
	public AdaptiveSmoothedKendallTau(RandomAccessibleInterval<I> image1, RandomAccessibleInterval<I> image2, I thres1, I thres2) {
		this.image1 = image1;
		this.image2 = image2;
		this.thres1 = thres1;
		this.thres2 = thres2;
		factory = img -> Util.getSuitableImgFactory(img, (T) new DoubleType()).create(img);
	}

	public RandomAccessibleInterval<DoubleType> execute() {
		long nr = image1.dimension(1);
		long nc = image1.dimension(0);
		RandomAccessibleInterval<DoubleType> result = Util.getSuitableImgFactory(image1, new DoubleType()).create(image1);
		RandomAccessibleInterval<T> oldtau = factory.apply(image1);
		RandomAccessibleInterval<T> newtau = factory.apply(image1);
		RandomAccessibleInterval<T> oldsqrtN = factory.apply(image1);
		RandomAccessibleInterval<T> newsqrtN = factory.apply(image1);
		stop = new ArrayList<>();
		for (int s=0; s<3; s++) stop.add(factory.apply(image1));
		Dn=Math.sqrt(Math.log(nr*nc))*2;
		TU=15;
		TL=8;
		Lambda=Dn;

		LoopBuilder.setImages(oldsqrtN).forEachPixel(t -> t.setOne());
		
		double size = 1;
		double stepsize = 1.15; // empirically the best, but could have users set
		int intSize;
		boolean IsCheck = false;
		
		for (int s = 0; s < TU; s++)
		{
			intSize = (int)Math.floor(size);
			singleiteration(oldtau,oldsqrtN,newtau,newsqrtN,result,intSize,IsCheck);
			size *= stepsize;
			if (s == TL)
			{
				IsCheck=true;
				LoopBuilder.setImages(stop.get(1), stop.get(2), newtau, newsqrtN).forEachPixel((ts1, ts2, tTau, tSqrtN) -> {
					ts1.set(tTau);
					ts2.set(tSqrtN);
				});
			}
		}
		
		return result; // z score, used to produce heat map
	}
	
	private void singleiteration(RandomAccessibleInterval<T> oldtau, RandomAccessibleInterval<T> oldsqrtN, RandomAccessibleInterval<T> newtau, RandomAccessibleInterval<T> newsqrtN, RandomAccessibleInterval<DoubleType> result, int Bsize, boolean isCheck) {
		double[][] kernel = kernelGenerate(Bsize);
		
		long[] rowrange = new long[4];
		long[] colrange = new long[4];
		int totnum=(2*Bsize+1)*(2*Bsize+1);
		double[] LocX = new double[totnum];
		double[] LocY = new double[totnum];
		double[] LocW = new double[totnum];
		double tau;
		double taudiff;

		final Cursor<DoubleType> cursor = Views.iterable(result).localizingCursor();
		final RandomAccess<T> raOldTau = oldtau.randomAccess();
		final RandomAccess<T> raOldSqrtN = oldsqrtN.randomAccess();
		final RandomAccess<T> raNewTau = newtau.randomAccess();
		final RandomAccess<T> raNewSqrtN = newsqrtN.randomAccess();
		final RandomAccess<T> rastop0 = stop.get(0).randomAccess();
		final RandomAccess<T> rastop1 = stop.get(1).randomAccess();
		final RandomAccess<T> rastop2 = stop.get(2).randomAccess();
		
		final RandomAccess<I> gdImage1 = image1.randomAccess();
		final RandomAccess<I> gdImage2 = image2.randomAccess();
		final RandomAccess<T> gdTau = oldtau.randomAccess();
		final RandomAccess<T> gdSqrtN = oldsqrtN.randomAccess();

		long nr = result.dimension(1);
		long nc = result.dimension(0);
		while (cursor.hasNext()) {
			cursor.next();
			raOldTau.setPosition(cursor);
			raOldSqrtN.setPosition(cursor);
			raNewTau.setPosition(cursor);
			raNewSqrtN.setPosition(cursor);
			rastop0.setPosition(cursor);
			rastop1.setPosition(cursor);
			rastop2.setPosition(cursor);

			final long row = cursor.getLongPosition(1);
			updateRange(row, Bsize, nr, rowrange);
			if (isCheck)
			{
				if (rastop0.get().getRealDouble() != 0)
				{
					continue;
				}
			}
			final long col = cursor.getLongPosition(0);
			updateRange(col, Bsize, nc, colrange);
			getData(kernel, gdImage1, gdImage2, gdTau, gdSqrtN, LocX, LocY, LocW, rowrange, colrange, totnum);
			raNewSqrtN.get().setReal(Math.sqrt(NTau(LocW, LocX, LocY)));
			if (raNewSqrtN.get().getRealDouble() <= 0)
			{
				raNewTau.get().setZero();
				cursor.get().setZero();
			}
			else
			{
				WtKendallTau kendalltau = new WtKendallTau(LocX, LocY, LocW);
				tau = kendalltau.calculate();
				raNewTau.get().setReal(tau);
				cursor.get().setReal(tau * raNewSqrtN.get().getRealDouble() * 1.5);
			}

			if (isCheck)
			{
				taudiff = Math.abs(rastop1.get().getRealDouble() - raNewTau.get().getRealDouble()) * rastop2.get().getRealDouble();
				if (taudiff > Lambda)
				{
					rastop0.get().setOne();
					raNewTau.get().set(raOldTau.get());
					raNewSqrtN.get().set(raOldSqrtN.get());
				}
			}
		}

		// TODO: instead of copying pixels here, swap oldTau and newTau every time. :-)
		LoopBuilder.setImages(oldtau, newtau, oldsqrtN, newsqrtN).forEachPixel((tOldTau, tNewTau, tOldSqrtN, tNewSqrtN) -> {
			tOldTau.set(tNewTau);
			tOldSqrtN.set(tNewSqrtN);
		});

	}
	
	private void getData(double[][] w, RandomAccess<I> i1RA, RandomAccess<I> i2RA, RandomAccess<T> tau, RandomAccess<T> sqrtN,
			double[] sx, double[] sy, double[] sw, long[] rowrange, long[] colrange, int totnum)
	{
		// TODO: Decide if this cast is OK.
		int kernelk = (int) (rowrange[0] - rowrange[2] + rowrange[3]);
		int kernell;
		int index = 0;
		double taudiffabs;
		
		sqrtN.setPosition(colrange[2], 0);
		sqrtN.setPosition(rowrange[2], 1);
		double sqrtNValue = sqrtN.get().getRealDouble();

		for (long k = rowrange[0]; k <= rowrange[1]; k++)
	    {
			i1RA.setPosition(k, 1);
			i2RA.setPosition(k, 1);
			sqrtN.setPosition(k, 1);
			// TODO: Double check cast.
			kernell = (int) (colrange[0] - colrange[2] + colrange[3]);
	        for (long l = colrange[0]; l <= colrange[1]; l++)
	        {
	        	i1RA.setPosition(l, 0);
	        	i2RA.setPosition(l, 0);
	        	sqrtN.setPosition(l, 0);
	        	sx[index] = i1RA.get().getRealDouble();
	            sy[index] = i2RA.get().getRealDouble();
	            sw[index] = w[kernelk][kernell];

	        	tau.setPosition(l, 0);
				tau.setPosition(k, 1);
	            double tau1 = tau.get().getRealDouble();

	        	tau.setPosition(colrange[2], 0);
				tau.setPosition(rowrange[2], 1);
	            double tau2 = tau.get().getRealDouble();


	            taudiffabs = Math.abs(tau1 - tau2) * sqrtNValue;
	            taudiffabs = taudiffabs / this.Dn;
	            if (taudiffabs < 1)
	            	sw[index] = sw[index] * (1-taudiffabs) * (1-taudiffabs);
	            else
	            	sw[index] = sw[index] * 0;
	            kernell++;
	            index++;
	        }
	        kernelk++;
	      }
		while(index < totnum)
	      {
	        sx[index] = 0;
	        sy[index] = 0;
	        sw[index] = 0;
	        index++;
	      }
	}
	
	private void updateRange(long location, int radius, long boundary, long[] range) {
		range[0] = location - radius;
	    if (range[0] < 0)
	    	range[0] = 0;
	    range[1] = location + radius;
	    if (range[1] >= boundary)
	    	range[1] = boundary - 1;
	    range[2] = location;
	    range[3] = radius;
	}
	
	private double NTau(double[] w, double[] x, double[] y) {
		double sumW=0;
	    double sumsqrtW=0;
	    double tempW;
	    
	    for (int index = 0; index < w.length; index++)
		    {
		        if (x[index]<this.thres1.getRealDouble() || y[index]<this.thres2.getRealDouble() )
		        	w[index]=0;
		        tempW = w[index];
		        sumW += tempW;
		        tempW = tempW * w[index];
		        sumsqrtW += tempW;
		    }
	    double  NW;
	    double Denomi = sumW * sumW;
	    if (Denomi <= 0)
	    {
	    	NW = 0;
	    }
	    else
	    {
	    	NW = Denomi / sumsqrtW;
    	}
	    return NW;
	}
	
	private double[][] kernelGenerate(int size) {
		int L = size * 2 + 1;
		double[][] kernel = new double[L][L];
		int center = size;
		double temp;
		double Rsize = size * Math.sqrt(2.5);
		
		for (int i = 0;i <= size; i++)
		{
		    for (int j = 0;j <= size; j++)
		    {
		      temp = Math.sqrt(i*i+j*j)/Rsize;
		      if (temp>=1)
		        temp=0;
		      else
		        temp=1-temp;
		      kernel[center+i][center+j] = temp;
		      kernel[center-i][center+j] = temp;
		      kernel[center+i][center-j] = temp;
		      kernel[center-i][center-j] = temp;
		    }
		}
		return kernel;
	}
}
