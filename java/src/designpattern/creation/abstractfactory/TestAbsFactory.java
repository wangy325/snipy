package designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:48
 */
public class TestAbsFactory {
    /**
     * 依赖倒置：客户依赖抽象，而不是依赖具体类。
     */
    static GUIFactory factory;
    static Button2 button2;
    static CheckBox checkBox;

    public static void main(String[] args) {
        String os = "mac";
        if (args.length != 0) os = args[0];
        init(os);
        button2 = factory.createButton();
        checkBox = factory.createCheckBox();
        button2.paint();
        checkBox.paint();
    }

    public static void init(String os) {
        System.setProperty("current.os", os);
        if (System.getProperty("current.os").equals("windows"))
            factory = new GUIFactoryWin();
        else factory = new GUIFactoryMac();
    }
}
///:~
//mac: button click
//mac: box check
//
