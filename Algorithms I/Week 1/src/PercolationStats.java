/*---------------------------------------------------------
 *  Author:        Stanislav Mironovich
 *  Written:       8/6/2017
 *  Last updated:  8/6/2017
 *
 *  Compilation:   javac PercolationStat.java
 *  Execution:     java PercolationStat
 *  
 *  PercolationStat
 *
 *  % java PercolationStat 200 100
 *  Results
 * 
 * ---------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats { 
    private final double mean;
    private final double stddev;
    private final double confLo;
    private final double confHi;
    
    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0) {
            throw new IllegalArgumentException("n");
        }
       
        if (trials <= 0) {
            throw new IllegalArgumentException("trials");
        }
       
        double[] thresholds = new double[trials];
       
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
           
            thresholds[i] = (double) p.numberOfOpenSites() / n / n;
        }
       
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        double sqrtT = Math.sqrt(trials);
        confLo = mean - 1.96 * stddev / sqrtT;
        confHi = mean + 1.96 * stddev / sqrtT;
    }
   
   
    public double mean() {                         // sample mean of percolation threshold
        return mean;
    }  
   
    public double stddev() {                       // sample standard deviation of percolation threshold
        return  stddev;  
    }
   
    public double confidenceLo() {
        return confLo;
    }                // low  endpoint of 95% confidence interval
   
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return confHi;
    }
   
    public static void main(String[] args) {       // test client (described below)
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
       
        PercolationStats p = new PercolationStats(n, t);
       
        StdOut.printf("mean                    = %f\n", p.mean());
        StdOut.printf("stddev                  = %f\n", p.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", p.confidenceLo(), p.confidenceHi());
    }
}