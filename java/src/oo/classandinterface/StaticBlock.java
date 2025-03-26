package oo.classandinterface;

/** 静态块只在类加载时调用一次 */
public class StaticBlock {
    static int x; // x=0
    static {
        x += 1;
        System.out.println("静态代码块1运行, x= " + x);	// x=1
    }
    static {
        x += 1;
        System.out.println("静态代码块2运行, x= " + x); // x=2
    }

    /**
     * 构造器
     */
    StaticBlock() {
        x++;
        System.out.println("构造器运行, x= " + x);
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        StaticBlock t = new StaticBlock(); // 调用一次构造器 x=4
        System.out.println("---------");
        t = new StaticBlock(); // 调用第二次构造器 , 静态代码块再执行

        System.out.printf("final result of variable x is %s\n", x);
    }

}
/**
静态代码块1运行, x= 1
静态代码块2运行, x= 2
构造器运行, x= 3
---------
构造器运行, x= 4
final result of variable x is 4
 */
