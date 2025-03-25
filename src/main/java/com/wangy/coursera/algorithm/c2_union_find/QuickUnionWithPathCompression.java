package com.wangy.coursera.algorithm.c2_union_find;

import java.util.Arrays;

/**
 * @author wangy
 * @version 1.0
 * @date 2023/5/25 / 08:50
 */
public class QuickUnionWithPathCompression {
    public static void main(String[] args) {
        QUWithPathCompression uf = new QUWithPathCompression(10);
        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.print();
        System.out.println(uf.find(4, 8));
        System.out.println(uf.find(3, 9));
        uf.union(9, 4);
        uf.union(2, 1);
        uf.union(5, 0);
        uf.print();
        uf.union(7, 2);
        uf.union(6, 1);
        uf.union(7, 3);
        uf.print();
        System.out.println(uf.find(2,9));
        // additional test: tree reorganized
        System.out.println(uf.find(5, 7));
        uf.print();
    }
}

class QUWithPathCompression {
    private int[] id;

    public QUWithPathCompression(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }


    // two-pass solution:
    // point node p and its ancestors to root node
    // by adding another loop
    public int root(int p) {
        int ori_p = p;
        while (id[p] != p) {
            p = id[p];
        }
        while (id[ori_p] != p) {
            int tmp = id[ori_p];
            id[ori_p] = p;
            ori_p = tmp;
        }
        return p;
    }

    // one-pass variant:
    // make every other node in path point to its
    // grandparent, thereby halving path length.
    public int root2(int p) {
        while (id[p] != p) {
            id[p] = id[id[p]];  // one line extra code!
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
        System.out.println(Arrays.toString(id));
    }
}
