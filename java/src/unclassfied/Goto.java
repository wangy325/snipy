package unclassfied;

public class Goto {

    /**
     * 判定i是否是质数（仅能被1和自己整除）
     */
    public static void isPrime() {
        label:for (int i  = 2; i< 10; i++) {
            for (int j = 2; j<i; j++) {
                if (i % j == 0) {
                    // 不是质数了
                    System.out.printf("%s 被 %s 整除%n", i, j);
                    continue label; // 可以试试break和continue的区别
                }
            }
            System.out.printf("%s 是质数%n", i);
        }
    }

    public static void main(String[] args) {
        isPrime();
    }
}

///*~
/// break 结束本层循环 （内层自然结束）
/// break [flag] 结束标记层的循环 （标记可能在外层，内层循环也自会结束）
/// continue 结束本层的本次循环
/// continue [flag] 结束标记层的本次循环（标记可能在外层，内层的本次循环也结束） 
///