package designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 22:00
 */
public class CarBuilder implements Builder {

    private Car car;
    private Product product;

    public CarBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.car = new Car();
    }

    @Override
    public void setSeats(int seats) {
        car.setSeat(seats);
    }

    @Override
    public void setEngine(Engine engine) {
        car.setEngine(engine);
    }

    @Override
    public void setTripComputer(boolean bool) {
        car.setTripComputer(bool);
    }

    @Override
    public void setGPS(boolean gps) {
        car.setGPS(gps);
    }

    // 具体构建器需要自行提供获取结果的方法。这是因为不同类型的构建器可能
    // 会创建不遵循相同接口的、完全不同的产品。所以也就无法在生成器接口中
    // 声明这些方法（至少在静态类型的编程语言中是这样的）。
    //
    // 通常在构建器实例将结果返回给客户端后，它们应该做好生成另一个产品的
    // 准备。因此生成器实例通常会在 `getProduct（获取产品）`方法主体末尾
    // 调用重置方法。但是该行为并不是必需的，你也可让生成器等待客户端明确
    // 调用重置方法后再去处理之前的结果。
    @Override
    public Car getProduct() {
        this.product = this.car;
        this.reset();
        return (Car) product;
    }
}
