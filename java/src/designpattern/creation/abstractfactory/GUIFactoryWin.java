package designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:39
 */
public class GUIFactoryWin implements GUIFactory{
    @Override
    public Button2 createButton() {
        return new Button2Win();
    }

    @Override
    public CheckBox createCheckBox() {
        return new CheckBoxWin();
    }
}
