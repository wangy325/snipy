package oo.generic;

/**
 * 函数式接口和Lambda表达式<br>
 * 1. 函数式接口：接口可以用作函数 --> 函数作为方法形参
 * <p>
 * 2. Lambda表达式简化了函数式接口的使用
 *
 * @author wangy
 * @version 1.0
 * @date 2024/7/9 / 18:00
 */

interface MFunction<R, T> {
    R apply(T t);
}

public class Gfunction {

    /* 函数式接口作为形参 */
    static <R, T> R applyFunc(MFunction<R, T> f, T n) {
        return f.apply(n);
    }

    public static void main(String[] args) {
        /* 传统方式 */
        System.out.println(applyFunc(new MFunction<Integer, Integer>() {
            @Override
            public Integer apply(Integer t) {
                return t * t;
            }
        }, 3));

        /* 使用lambda表达式 */
        System.out.println((Integer) applyFunc(x -> x * x, 5));
    }
}

/// :~
// 9
// 25