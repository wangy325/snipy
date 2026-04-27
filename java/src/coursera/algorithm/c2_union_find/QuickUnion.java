package coursera.algorithm.c2_union_find;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

/**
 * @author wangy
 * @version 1.0
 * @date 2023/5/22 / 08:59
 */
public class QuickUnion {

    public static void main(String[] args) {
        QuickUnionUF uf = new QuickUnionUF(10);
        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.print();
        StdOut.println(uf.find(4, 8));
        StdOut.println(uf.find(3, 9));
        uf.union(9, 4);
        uf.union(2, 1);
        uf.union(5, 0);
        uf.print();
        uf.union(7, 2);
        uf.union(6, 1);
        uf.union(7, 3);
        uf.print();
        StdOut.println(uf.find(2,9));
    }
}


class QuickUnionUF {
    private int[] id;

    QuickUnionUF(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    // chase parent pointers until reach root
    // depth of i array access
    private int root(int p) {
        while (id[p] != p) {
            p = id[p];
        }
        return p;
    }

    // check p and q have the same root
    // depth of p and q array accesses
    public boolean find(int p, int q) {
        return root(p) == root(q);
    }

    // change root of p to point to root of q
    // depth of p and q array access
    public void union(int p, int q) {
        int rp = root(p);
        int rq = root(q);
        id[rp] = rq;
    }

    public void print() {
        StdOut.println(Arrays.toString(id));
    }
}


