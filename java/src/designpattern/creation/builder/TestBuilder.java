package designpattern.creation.builder;

/**
 * @author wangy
 * @version 1.0
 * @date 2022/3/13 / 23:19
 */
public class TestBuilder {

    public static void main(String[] args) {
        CarDirector carDirector = new CarDirector();
        CarBuilder builder = new CarBuilder();
        //create sport car
        carDirector.createSportCar(builder);
        System.out.println(builder.getProduct());
        // create suv
        carDirector.createSUV(builder);
        System.out.println(builder.getProduct());
    }
}
///:~
//Car{seat=2, engine=*.SportEngine@90f6bfd, tripComputer=true, GPS=true}
//Car{seat=7, engine=*.NormalEngine@15975490, tripComputer=true, GPS=true}
