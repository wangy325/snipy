public class Gfunction {

    static <R,T> R apply_func(MFunction<R, T> func, T t) {
        return func.apply(t);
    }

    public static void main(String[] args){
        int a  = apply_func(new Square(), 5);
        System.out.println(a);
    }

}

interface MFunction <R, T> {
    R apply(T t);
}


class Square implements MFunction<Integer, Integer> {
    @Override
    public Integer apply(Integer t) {
        return t * t;
    }
}