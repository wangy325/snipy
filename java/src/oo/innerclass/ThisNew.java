package oo.innerclass;

/**
 * @author wangy
 * @version 1.0
 * @date 2020/4/18 / 17:34
 */
public class ThisNew {

    public static void main(String[] args) {
        DotThis d = new DotThis();
        DotThis.Inner ti = d.inner();
        ti.outer().f();

        DotNew n = new DotNew();
        // 使用 .new 获取内部类引用
        DotNew.Inner ni = n.new Inner();
        ni.f();
    }
}

class DotThis {
    void f() {
        System.out.println("DotThis.f()");
    }

    class Inner {
        // 在内部类中生成外部类对象引用
        public DotThis outer() {
            return DotThis.this;
        }
    }

    Inner inner() {
        return new Inner();
    }
}

class DotNew {
    class Inner {
        void f() {
            System.out.println("DotNew.Inner.f()");
        }
    }
}
