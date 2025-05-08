package oo.references;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/14 / 15:15
 */
@SuppressWarnings("all")
class ByValue {
    static void tripleValue(Integer x) {
        x = 3 * x;
        System.out.println("x = " + x);
    }

    public static void main(String[] args) {
        int y = 10;
        // Integer y = Integer.valueOf(10);
        y = 3 * y;
        tripleValue(y);
        System.out.println("y = " + y);
    }
}

/* output:
 * x = 30
 * y = 10
 */// :~