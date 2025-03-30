package designpattern.behavioral.strategy;


/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 15:57
 */
public class DuckTest {

    public static void main(String[] args) {
        // 这种🦆的飞/叫行为已经在策略里定义了
        MallardDuck mock = new MallardDuck();
        mock.performFly();
        mock.performQuark();
        // 改变行为试试
        mock.setFlyBehavior(new FlyWithRocket());
        mock.performFly();
    }
}

///：~
//Yes! I can fly with wings!
//Quark!
//Oh! I can fly with a rocket booster!
//
