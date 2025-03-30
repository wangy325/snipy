package designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:37
 * 抽象工厂，可以创建多种产品
 */
public interface GUIFactory {

    Button2 createButton();
    CheckBox createCheckBox();
}
