package designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:31
 */
public class DialogHTML extends Dialog{
    @Override
    Button1 createButton() {
        return new Button1HTML();
    }
}
