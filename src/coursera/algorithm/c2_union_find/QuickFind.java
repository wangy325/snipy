package coursera.algorithm.c2_union_find;

import java.util.Arrays;

/**
 * @author wangy
 * @version 1.0
 * @date 2023/5/22 / 08:42
 */

public class QuickFind {

    public static void main(String[] args) {
        QuickFindUF uf = new QuickFindUF(10);
        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.print();
        System.out.println(uf.find(4,8));
        System.out.println(uf.find(3,9));
        uf.union(9,4);
        uf.union(2,1);
        uf.union(5,0);
        uf.print();
        uf.union(7,2);
        uf.union(6,1);
        uf.union(7,3);
        uf.print();
    }

}

class QuickFindUF {

    private int[] id;

    // set id of each object to itself
    // N array access
    public QuickFindUF(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    // check p and q are in the same component
    // 2 array access
    public boolean find(int p, int q) {
        return id[p] == id[q];
    }

    // change all entries of id[p] to id[q]
    // at most 2N +2 array access
    public void union(int p, int q) {
        int pid = id[p];
        int qid = id[q];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    public void print() {
        System.out.println(Arrays.toString(id));
    }

}
