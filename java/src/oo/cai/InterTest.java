package oo.cai;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/16 / 12:42
 */
public class InterTest {
    public static void main(String[] args) {
        System.out.println(InFace.get());
        InFace i = new InFaceImp();
        i.absFunc();
        i.defFunc();
    }
}

interface InFace {
    int CONSTANT = 1;

    // static method
    static int get() {
        return CONSTANT;
    }

    void absFunc();

    // default method
    default void defFunc() {
        System.out.println("InFace.defFunc()");
    }
}

class InFaceImp implements InFace {

    @Override
    public void absFunc() {
        System.out.println("InFaceImp.absFunc()");
    }
}

/* output
 * 1
 * InFaceImp.absFunc()
 * InFace.defFunc()
 *///:~