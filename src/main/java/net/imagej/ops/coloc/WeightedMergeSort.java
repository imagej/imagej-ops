
package net.imagej.ops.coloc;

/**
 * Helper class for WtKendallTau class. 
 *
 * @author Shulei Wang
 */
public class WeightedMergeSort {
	
	private int[] data; // formally called 'index'
	private double[] weight;
	private final IntComparator comparator;

	public WeightedMergeSort(int[] index, double[] w, IntComparator comparator) {
		this.data = index;
		this.weight = w;
		this.comparator = comparator;
	}
	
	public double sort() {
		  double swap = 0;
		  double tempswap;
		  int n = data.length;
		  int step = 1;
		  int[] index1 = new int[n];
		  int[] index2 = new int[n];
		  double[] w1 = new double[n];
		  double[] w2 = new double[n];
		  double[] cumw = new double[n];
		  int begin;
		  int begin2;
		  int end;
		  int k;
		  
		  for (int i = 0; i < n; i++)
		  {
		    index1[i] = data[i];
		    w1[i] = weight[i];
		  }
		  
		  while (step < n) {
		    begin=0;
		    k=0;
		    cumw[0]=w1[0];
		    for (int i=1;i<n;i++)
		    {
		      cumw[i]=cumw[i-1]+w1[i];
		    }
		    
		    while (true)
		    {
		      begin2 = begin + step;
		      end = begin2 + step;
		      if (end > n)
		      {
		        if (begin2 > n)
		          break;
		        end = n;
		      }
		      int i = begin;
		      int j = begin2;
		      while (i < begin2 && j < end)
		      {
		        if (comparator.compare(index1[i], index1[j])>0)
		        {
		          if (i == 0)
		          {
		            tempswap = w1[j]*cumw[begin2-1];
		          } else {
		            tempswap = w1[j]*(cumw[begin2-1]-cumw[i-1]);
		          }
		          swap = swap + tempswap;
		          index2[k] = index1[j];
		          w2[k++] = w1[j++];
		        }
		        else
		        {
		          index2[k] = index1[i];
		          w2[k++] = w1[i++];
		        }
		      }
		      if (i < begin2)
		      {
		        while (i < begin2)
		        {
		          index2[k] = index1[i];
		          w2[k++] = w1[i++];
		        }
		      } else {
		        while (j < end)
		        {
		          index2[k] = index1[j];
		          w2[k++] = w1[j++];
		        }
		      }
		      begin = end;
		    }
		    if (k < n)
		    {
		      while(k < n)
		      {
		        index2[k] = index1[k];
		        w2[k] = w1[k];
		        k++;
		      }
		    }
		    for (int i = 0; i < n; i++)
		    {
		      index1[i] = index2[i];
		      w1[i] = w2[i];
		    } 
		    
		    step *= 2;
		  }
		  return swap;
	}
}
