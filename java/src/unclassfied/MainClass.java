package unclassfied;

/**
 * Author: wangy325
 * Date: 2024-07-21 01:27:52
 * Description: 子类不能调用并未实现的构造器
 * */

public class MainClass {

    public static void main(String[] args) {
        Extended ext = new Extended();
        ext.pubMethod();

        // 父类有带参构造器，子类并没有实现
        // Extended ext2 = new Extended("xxxx");
    }
}

/***
 * Base no-arg constructor invoked~
    class attr: Base attr
    private attr: Base private attr
 */

class Base {

    static String attr = "Base attr";
    private String private_attr = "Base private attr";

    public Base() {
        super();
        System.out.println("Base no-arg constructor invoked~");
    }

    public Base(String args) {
        this.private_attr = args;
    }

    private void bMethod() {
        System.out.printf("class attr: %s\nprivate attr: %s\n", attr, private_attr);
    }

    public void pubMethod() {
        bMethod();
    }
}

class Extended extends Base {
    // do nothing
    // but still has a implicit constructor 
}


