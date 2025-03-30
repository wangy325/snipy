package designpattern.creation.abstractfactory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 18:39
 */
public class Button2Win implements Button2 {
    @Override
    public void paint() {
        System.out.println("win: button click");
    }
}
