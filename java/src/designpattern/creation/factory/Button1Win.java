package designpattern.creation.factory;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/12 / 17:30
 */
public class Button1Win implements Button1 {
    @Override
    public void render() {
        System.out.println("<===== render windows button.");
    }

    @Override
    public void onClick() {
        System.out.println("<===== click windows button.");
    }
}
