package unclassfied;
/**
 * 一个抽象方法的栗子.
 * <p>
 * 现在可以使用函数式接口, 加上Lambda表达式更加优雅的编码.
 * <p>
 * 其实是为了和python中的函数作为形参来对应
 */
public class Gfunction {

    static <R,T> R apply_func(MFunction<R, T> func, T t) {
        return func.apply(t);
    }

    public static void main(String[] args){
        int a  = apply_func(new Square(), 5);
        System.out.println(a);
    }

}

// 函数式接口 function interface
interface MFunction <R, T> {
    R apply(T t);
}


class Square implements MFunction<Integer, Integer> {
    @Override
    public Integer apply(Integer t) {
        return t * t;
    }
}