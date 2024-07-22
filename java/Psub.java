/**
 * Author: wangy325
 * Date: 2024-07-21 01:27:52
 * Description:
 **/


class Basef {
    static String attr = "Base attr";

    private String pattr = "Base pattr";

    public Basef() {
        super();
    }

    public Basef(String args) {
        this.pattr = args;
    }

    private void pmethod() {
        System.out.printf("class attr: %s\nprivate attr: %s\n", attr, pattr);
    }

    public void pubMethod(){
        pmethod();
    }
}

class Subc extends Basef {
    // do nothing
}

public class Psub {
    public static void main(String[] args) {
        Subc sub = new Subc();
        sub.pubMethod();
        // 不行直接调用父类的构造器
        // Subc sub2 = new Subc("xxxx");

    }
}

