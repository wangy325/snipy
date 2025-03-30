package designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:40
 */
public class GUIFactoryMac implements GUIFactory{
    @Override
    public Button2 createButton() {
        return new Button2Mac();
    }

    @Override
    public CheckBox createCheckBox() {
        return new CheckBoxMac();
    }
}
