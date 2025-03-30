package designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:30
 */
public class Button1HTML implements Button1 {
    @Override
    public void render() {
        System.out.println("=====> render HTML Button.");
    }

    @Override
    public void onClick() {
        System.out.println("====> click HTML button.");
    }
}
