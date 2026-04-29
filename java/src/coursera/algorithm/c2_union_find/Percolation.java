package coursera.algorithm.c2_union_find;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private WeightedQuickUnionUF grid ;
    private int openCount;
    private boolean[] openFlag;
    private int vTop;
    private int vBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.size = n;
        // n^2 + 2 virtual sites
        // assume the first and last site is VTop and vBottom sites
        // 0 ~ (n^2 - 1)
        grid = new WeightedQuickUnionUF(n * n + 2);
        vTop = n * n ;
        vBottom = n * n + 1;
        // init all sites closed
        openFlag = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            openFlag[i] = false;
        }
    }

    private void validate(int row, int col){
        if (row < 1 || row > size || col < 1 || col > size)
            throw new IllegalArgumentException();
    }

    // check site adjacent sites
    private boolean validate(int rl) {
        return rl >= 1 && rl <= size;
    }

    private int calIndex(int row, int col){
        validate(row, col);
        // start from 0
        return size * (row - 1) + col - 1;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        int p = calIndex(row, col);
        if (row == 1) {
            // connect to vTop
            grid.union(p, vTop);
        }
        if (row == size) {
            // connect to vBottom
            grid.union(p, vBottom);
        }
        // connect adjacent open site
        // up
        if (validate(row - 1) && isOpen(row - 1, col)) 
            grid.union(p, calIndex(row - 1, col));
        // right
        if (validate(col + 1) && isOpen(row, col + 1))
            grid.union(p, calIndex(row, col + 1 ));
        // bottom
        if (validate(row + 1) && isOpen(row + 1, col))
            grid.union(p, calIndex(row + 1, col));
        // left
        if (validate(col - 1) && isOpen(row, col - 1))
            grid.union(p, calIndex(row, col - 1));

        openFlag[p] = true;
        openCount++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int p = calIndex(row, col);
        return openFlag[p];
    }

    // is the site (row, col) full?
    // iff the site is connected to Top row open site
    public boolean isFull(int row, int col) {
        int p = calIndex(row, col);
        if (!openFlag[p]) return false;
        // is this correct?
        return grid.find(p) == grid.find(vTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    // iff any bottom row site if full
    public boolean percolates() {
        return grid.find(vTop) == grid.find(vBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        // corner cases tests
        // case0 N = 0
        // Percolation p0 = new Percolation(0);
        // case1: N = 1
        Percolation p1 = new Percolation(1);
        StdOut.println("n = 1, percolates? " + p1.percolates());
        p1.open(1, 1);
        StdOut.println("n = 1, percolates? " + p1.percolates());
        // case2: N = 5
        Percolation p2 = new Percolation(5);
        p2.open(3,4);
        p2.open(1,2);
        p2.open(2,3);
        p2.open(4,3);
        p2.open(5,3);
        p2.open(3,3);
        p2.open(1,3);
        StdOut.println("n = 5, openCount " + p2.openCount);
        StdOut.println("percolates? " + p2.percolates());
        StdOut.println("full? " + p2.isFull(4,2));
    }
}
