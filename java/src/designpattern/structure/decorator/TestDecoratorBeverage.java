package designpattern.structure.decorator;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/12 / 14:14
 */
public class TestDecoratorBeverage {

    public static void main(String[] args) {
        // 双份摩卡加奶混合口味咖啡(超大杯)
        Beverage hb = new Mocha(new Mocha(new Milk(new HouseBlend(Size.EXTRA_LARGE))));
        // 中杯豆浆奶泡口味深烘焙（应该很难喝吧~）
        Beverage dr = new Whip(new Soy(new DarkRost(Size.MEDIUM)));

        System.out.printf("%s: %2.2f\n", hb.getDescription(), hb.cost());
        System.out.printf("%s: %2.2f", dr.getDescription(), dr.cost());
    }
}
///:~
//EXTRA_LARGE House-Blend Milk Mocha Mocha: 13.56
//MEDIUM DarkRoast Soy Whip: 5.97
//