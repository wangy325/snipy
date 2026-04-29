package coursera.algorithm.c2_union_find;


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] res;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        res = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while(!percolation.percolates()){
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                percolation.open(row, col);
            }
            res[i] = percolation.numberOfOpenSites() / (n * n * 1.0e0);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.min(res);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.max(res);
    }

    // test client (see below)
    public static void main(String[] args) {
//        int  n = Integer.parseInt(args[0]);
//        int trials = Integer.parseInt(args[1]);
         int n = StdIn.readInt();
         int trials = StdIn.readInt();

        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.printf("mean = %.12f\t\n", ps.mean());
        StdOut.printf("stddev = %.12f\t\n", ps.stddev());
        StdOut.printf("95%% confidence interval = [%.12f, %.12f]\n", ps.confidenceLo(), ps.confidenceHi());
    }
}
