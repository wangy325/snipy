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