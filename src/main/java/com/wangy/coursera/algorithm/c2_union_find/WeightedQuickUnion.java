package com.wangy.coursera.algorithm.c2_union_find;

import java.util.Arrays;

/**
 * @author wangy
 * @version 1.0
 * @date 2023/5/22 / 09:04
 */
public class WeightedQuickUnion {

    public static void main(String[] args) {
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(10);
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
    }

}

 class WeightedQuickUnionUF {
    private int[]  id;
    private int[]  sz;

    public WeightedQuickUnionUF(int N) {
        id = new int[N];
        sz = new int[N];
        for(int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }
    // chase parent pointers until reach root
    // depth of i array access
    public int root(int p){
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
        int i = root(p);
        int j = root(q);
        if (i == j) return; // already union
        if (sz[i] > sz[j]) {
            id[j] = i;
            sz[i] += sz[j];
        } else {
            id[i] = j;
            sz[j] += sz[i];
        }
    }

     public void print() {
         System.out.println(Arrays.toString(id));
     }

}