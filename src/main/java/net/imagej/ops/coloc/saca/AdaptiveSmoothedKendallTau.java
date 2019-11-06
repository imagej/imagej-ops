package net.imagej.ops.coloc.saca;

/**
 * Shulei's original Java code for AdaptiveSmoothedKendallTau from his RKColocal R package.
 * (https://github.com/lakerwsl/RKColocal/blob/master/RKColocal_0.0.1.0000.tar.gz)
 * 
 * @author Shulei Wang
 */

public class AdaptiveSmoothedKendallTau {

	public double[][] X;
	public double[][] Y;
	public double ThreX;
	public double ThreY;
	private double Dn;
	private int TL;
	private int TU;
	private double Lambda;
	private double[][][] Stop;
	
	public AdaptiveSmoothedKendallTau(double[][] IX, double[][] IY,double IThreX, double IThreY) {
		this.X = IX;
		this.Y = IY;
		this.ThreX = IThreX;
		this.ThreY = IThreY;
	}
	
	public double[][] execute() {
		int nr = X.length;
		int nc = X[0].length;
		double[][] result = new double[nr][nc];
		double[][] oldtau = new double[nr][nc];
		double[][] newtau = new double[nr][nc];
		double[][] oldsqrtN = new double[nr][nc];
		double[][] newsqrtN = new double[nr][nc];
		Stop = new double[nr][nc][3];
		Dn=Math.sqrt(Math.log(nr*nc))*2;
		TU=15;
		TL=8;
		Lambda=Dn;
		
		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				oldtau[i][j] = 0;
				oldsqrtN[i][j] = 1;
				Stop[i][j][0]=0;
				Stop[i][j][1]=0;
				Stop[i][j][2]=0;
			}
		}
		
		double size = 1;
		double stepsize = 1.15;
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
				for (int i = 0; i < nr; i++) {
					for (int j = 0; j < nc; j++) {
						Stop[i][j][1]=newtau[i][j];
						Stop[i][j][2]=newsqrtN[i][j];
					}
				}
			}
		}
		
		return result;
	}
	
	private void singleiteration(double[][] oldtau, double[][] oldsqrtN, double[][] newtau, double[][] newsqrtN, double[][] result, int Bsize, boolean isCheck) {
		int nr = X.length;
		int nc = X[0].length;
		double[][] kernel = kernelGenerate(Bsize);
		
		int[] rowrange = new int[4];
		int[] colrange = new int[4];
		int totnum=(2*Bsize+1)*(2*Bsize+1);
		double[] LocX = new double[totnum];
		double[] LocY = new double[totnum];
		double[] LocW = new double[totnum];
//		double VarW;
		double tau;
		double taudiff;
		
		for (int i = 0; i < nr; i++)
		{
			rowrange = getRange(i, Bsize, nr);
		    for (int j = 0; j < nc; j++)
		    {
		    	if (isCheck)
		    	{
		    		if (Stop[i][j][0] != 0)
		    		{
		    			continue;
		    		}
		    	}
		    	colrange = getRange(j, Bsize, nc);
		    	getData(X, Y, kernel, oldtau, oldsqrtN, LocX, LocY, LocW, rowrange, colrange, totnum);
//		    	VarW = varTau(LocW, LocX, LocY);
		    	newsqrtN[i][j]=Math.sqrt(NTau(LocW, LocX, LocY));
//		    	if(newsqrtN[i][j]>1)
//		    		System.out.println(newsqrtN[i][j]);
		    	if (newsqrtN[i][j] <= 0)
		    	{
		    		newtau[i][j] = 0;
		    		result[i][j] = 0;
		    	}
		    	else
		    	{
		    		WtKendallTau kendalltau = new WtKendallTau(LocX, LocY, LocW);
		    		tau = kendalltau.calculate();
		    		newtau[i][j] = tau;
//			    	result[i][j] = tau / Math.sqrt(VarW);
		    		result[i][j] = tau * newsqrtN[i][j] * 1.5;
		    	}
		    	
		    	if (isCheck)
		    	{
		    		taudiff = Math.abs(Stop[i][j][1] - newtau[i][j]) * Stop[i][j][2];
		    		if (taudiff > Lambda)
		    		{
		    			Stop[i][j][0] = 1;
		    			newtau[i][j] = oldtau[i][j];
		    			newsqrtN[i][j] = oldsqrtN[i][j];
		    		}
		    	}
		    	
		    }
		}
		
		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				oldtau[i][j] = newtau[i][j];
				oldsqrtN[i][j] = newsqrtN[i][j];
			}
		}

	}
	
	private void getData(double[][] x, double[][] y, double[][] w, double[][] oldtau, double[][] oldsqrtN, double[] sx, double[] sy, double[] sw, int[] rowrange, int[] colrange, int totnum) {
		int kernelk = rowrange[0] - rowrange[2] + rowrange[3];
		int kernell;
		int index = 0;
		double taudiff;
		double taudiffabs;
		
		for (int k = rowrange[0]; k <= rowrange[1]; k++)
	    {
			kernell = colrange[0] - colrange[2] + colrange[3];
	        for (int l = colrange[0]; l <= colrange[1]; l++)
	        {
	        	sx[index] = x[k][l];
	            sy[index] = y[k][l];
	            sw[index] = w[kernelk][kernell];
	            taudiff = oldtau[k][l] - oldtau[rowrange[2]][colrange[2]];
	            taudiffabs = Math.abs(taudiff) * oldsqrtN[rowrange[2]][colrange[2]];
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
	
	private int[] getRange(int location, int radius, int boundary) {
		int[] range = new int[4];
		range[0] = location - radius;
	    if (range[0] < 0)
	    	range[0] = 0;
	    range[1] = location + radius;
	    if (range[1] >= boundary)
	    	range[1] = boundary - 1;
	    range[2] = location;
	    range[3] = radius;
		
	    return range;
	}
	
//	private double varTau(double[] w, double[] x, double[] y) {
//		double sumW=0;
//	    double sumsqrtW=0;
//	    double sumcubicW=0;
//	    double sumquardW=0;
////	    double Amplifer=10000;
//	    double tempW;
//	    for (int index = 0; index < w.length; index++)
//	    {
//	        if (x[index]<this.ThreX || y[index]<this.ThreY )
//	        	w[index]=0;
////	    	w[index] = w[index] * x[index] * x[index] * y[index] * y[index];
////	    	w[index] = w[index] / (1+Math.exp(-(x[index]-ThreX)*Amplifer)) / (1+Math.exp(-(y[index]-ThreY)*Amplifer));
//	        tempW = w[index];
//	        sumW += tempW;
//	        tempW = tempW * w[index];
//	        sumsqrtW += tempW;
////	        tempW = tempW * w[index];
////	        sumcubicW += tempW;
////	        tempW = tempW * w[index];
////	        sumquardW += tempW;
//	    }
//	    double  VarW;
////	    double Denomi = sumW * sumW - sumsqrtW;
//	    double Denomi = sumW * sumW;
//	    if (Denomi == 0)
//	    	VarW = 0;
//	    else
//	    {
//	    	VarW = Denomi / sumsqrtW;
////	    	VarW = 4 / (Denomi * Denomi) / 9;
////	    	VarW = VarW * (sumW * sumW * sumsqrtW - 2 * sumW * sumcubicW - 5 * sumquardW / 2 + 7 * sumsqrtW * sumsqrtW / 2);
//	    }
//	    return VarW;
//	}
	
	private double NTau(double[] w, double[] x, double[] y) {
		double sumW=0;
	    double sumsqrtW=0;
	    double tempW;
	    
	    for (int index = 0; index < w.length; index++)
		    {
		        if (x[index]<this.ThreX || y[index]<this.ThreY )
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
