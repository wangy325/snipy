package designpattern.behavioral.template_method;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/4/2 / 12:12
 */
public abstract class AbsCaffeineBeverageWithHook {
    //    模板方法声明为final，防止子类继承-修改算法
    public final void prepareRecipe() {
        boilWater();
        raw();
        condiment();
        pullInCup();
    }

    protected final void boilWater() {
        System.out.println("Starting boiling water.");
    }
    protected final void pullInCup() {
        System.out.println("Done! pull beverage into cup.");
    }

    public abstract void raw();
    public abstract void condiment();

    // 这是一个钩子
    public boolean addCondiment() {
        return true;
    }
}
