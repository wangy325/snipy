package coursera.algorithm.c3_analysis_algorithms;


/**
 * 2分查找
 *
 * @author wangy
 * @version 1.0
 * @date 2020/6/5 / 14:23
 */
public class BinarySearch {

    public static int find(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] > key) {
                high = mid - 1;
            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
       int[] ints = {6,12,14,25,33,43,51,53,64,72,84,93,95,96,99};
        int i = BinarySearch.find(ints, 33);
        System.out.println(i);
    }
}
