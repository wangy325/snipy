package review.generic;


/**
 * @author wangy
 * @version 1.0
 * @date 2024/7/9 / 18:00
 */
/**
// * 函数式接口和Lambda表达式<br>
 * 1. 函数式接口：接口可以用作函数 --> 函数作为方法形参
 * <p>
 * 2. Lambda表达式简化了函数式接口的使用
 * */

public class Gfunction {

    static <R, T> R applyFunc(MFunction<R, T> f, T n) {
        return f.apply(n);
    }

    static MFunction<Integer, Integer> func = new MFunction<Integer, Integer>() {
        @Override
        public Integer apply(Integer o) {
            return o * o;
        }
    };

    public static void main(String[] args) {
        System.out.println(applyFunc(new Square(), 3));

        applyFunc(func, 5);
    }
}

interface MFunction<R, T> {
    R apply(T t);
}

class Square implements MFunction<Integer, Integer> {
    @Override
    public Integer apply(Integer t) {
        return t * t;
    }
}

class Doubly implements MFunction<Integer, Integer> {
    @Override
    public Integer apply(Integer t) {
        return t * 2;
    }
}