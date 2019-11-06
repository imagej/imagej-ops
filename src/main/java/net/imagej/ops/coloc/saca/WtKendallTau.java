package net.imagej.ops.coloc.saca;

import net.imagej.ops.coloc.WeightedMergeSort;

/**
 * Helper class for the AdaptiveKTau op.
 * 
 * @author Shulei Wang
 */
public class WtKendallTau {
	
	public double[] X;
	public double[] Y;
	public double[] W;
	
	public WtKendallTau(double[]InputX, double[]InputY, double[]InputW) {
		X = InputX;
		Y = InputY;
		W = InputW;
	}
	
	public double calculate() {
		
		double[][] rankedData = rank(X, Y, W);
		int[] rankedindex = new int[X.length];
		double[] rankedw = new double[X.length];
		
		for(int i = 0; i < X.length; i++)
		{
			rankedindex[i] = (int)rankedData[i][0];
			rankedw[i] = rankedData[i][2];
		}
		
		final WeightedMergeSort mergeSort = new WeightedMergeSort(rankedindex, rankedw, (a, b) -> Integer.compare(a, b));
		
		double swap = mergeSort.sort();
		double tw = totw(W)/2;
		
		double tau = (tw - 2 * swap) / tw;
		
		return tau;	
	}
	
    public double brutalcalculate() {
		
		double[][] rankedData = rank(X, Y, W);
//		int[] rankedindex = new int[X.length];
//		double[] rankedw = new double[X.length];
		double sumw = 0;
		double sumnum = 0;
		double tempw;
		double temp;
		
		for(int i = 0; i < X.length; i++)
		{
			for (int j = i+1; j < X.length; j++)
			{
				tempw = rankedData[i][2] * rankedData[j][2];
				temp = (rankedData[i][0] - rankedData[j][0]) * (rankedData[i][1] - rankedData[j][1]);
				if (temp > 0)
				{
					sumnum = sumnum + tempw;
				}
				else
				{
					sumnum = sumnum - tempw;
				}
				sumw = sumw + tempw;
			}
		}
		
		
		double tau = sumnum / sumw;
		
		return tau;	
	}
	
	private double totw(double[] w) {
		double sumw = 0;
		double sumsquarew = 0;
		
		for (int i = 0; i < w.length; i++)
		{
			sumw += w[i];
			sumsquarew += w[i]*w[i];
		}
		
		double result = sumw * sumw - sumsquarew;
		
		return result;
	}
	
	private double[][] rank(double[] IX, double[] IY, double[] IW) {
		double[][] combinedData = new double[IX.length][3];
		
		for(int i = 0; i < IX.length; i++)
		{
			combinedData[i][0] = IX[i];
			combinedData[i][1] = IY[i];
			combinedData[i][2] = IW[i];
		}
		
		//sort X
		java.util.Arrays.sort(combinedData, new java.util.Comparator<double[]>() {
			@Override
			public int compare(double[] row1, double[] row2) {
				return Double.compare(row1[0], row2[0]);
			}
		});
		
		int start = 0;
		int end = 0;
		int rank=0;
		while (end < IX.length-1)
		{
			while (Double.compare(combinedData[start][0],combinedData[end][0]) == 0)
			{
				end++;
				if(end >= IX.length)
					break;
			}
			for (int i = start; i < end; i++){
				combinedData[i][0]=rank+Math.random();
			}
			rank++;
			start=end;
		}
		
		java.util.Arrays.sort(combinedData, new java.util.Comparator<double[]>() {
			@Override
			public int compare(double[] row1, double[] row2) {
				return Double.compare(row1[0], row2[0]);
			}
		});
		
		for (int i = 0; i < IX.length; i++) {
			combinedData[i][0] = i + 1;
		}
		
		//sort Y
		java.util.Arrays.sort(combinedData, new java.util.Comparator<double[]>() {
			@Override
			public int compare(double[] row1, double[] row2) {
				return Double.compare(row1[1], row2[1]);
			}
		});
		
		start = 0;
		end = 0;
		rank=0;
		while (end < IX.length-1)
		{
			while (Double.compare(combinedData[start][1],combinedData[end][1]) == 0)
			{
				end++;
				if(end >= IX.length)
					break;
			}
				
			for (int i = start; i < end; i++){
				combinedData[i][1]=rank+Math.random();
			}
			rank++;
			start=end;
		}
		
		java.util.Arrays.sort(combinedData, new java.util.Comparator<double[]>() {
			@Override
			public int compare(double[] row1, double[] row2) {
				return Double.compare(row1[1], row2[1]);
			}
		});
		
		for (int i = 0; i < IX.length; i++) {
			combinedData[i][1] = i + 1;
		}
		
		return combinedData;
	}
}
