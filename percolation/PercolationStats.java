/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double[] results;
    private double mean = 0;
    private double stddev = 0;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("side length must be a positive integer");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("trials number must be a positive integer");
        }

        results = new double[trials];
        for (int trial = 0; trial < trials; ++trial) {
            Percolation percolation = new Percolation(n);
            int opens = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    ++opens;
                }
            }

            results[trial] = (double) opens / (double) (n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.mean == 0) {
            this.mean = StdStats.mean(results);
        }
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.stddev == 0) {
            this.stddev = StdStats.stddev(results);
        }
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(results.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(results.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        // Stopwatch stopwatch = new Stopwatch();
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        // StdOut.println(stopwatch.elapsedTime());
        StdOut.printf("mean                     = %f\n", stats.mean());
        StdOut.printf("stddev                   = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", stats.confidenceLo(), stats.confidenceHi());
    }
}