package unclassfied;

/**
 * Author: wangy325
 * Date: 2024-07-17 12:37:27
 * Description: 不要在finally块里写返回语句, finally块里的值总是最终的返回值
 **/

class TryF {
    public static void main(String[] args) {
        System.out.println(how_finally_works(2, 3));
        System.out.println("---------");
        System.out.println(how_finally_works(2, 0));
        System.out.println("---------");
        System.out.println(how_finally_works("a", "b"));
        System.out.println("---------");

    }

    /**
     * 和python的表现一模一样
     * 
     * @param x
     * @param y
     * @return
     * @see try_excep.py
     */
    @SuppressWarnings("finally")
    static float how_finally_works(Object x, Object y) {
        try {
            return (int) x / (int) y;
        } catch (ArithmeticException e) {
            e.printStackTrace();
            System.out.println("can not devide by 0");
            return 0;
        } finally {
            System.out.println("finally...");
            return -1;
        }
    }
}
