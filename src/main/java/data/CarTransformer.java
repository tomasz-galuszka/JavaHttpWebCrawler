package data;

import data.db.Car;
import data.db.low.CarItem;

public class CarTransformer {

    public static Car transform(CarItem carItem) {
        Car car = new Car();
        car.setId(carItem.getEi());
        car.setMark(carItem.getMk());
        car.setModel(carItem.getMd());
        car.setYear(carItem.getFr());
        car.setMileage(carItem.getMa());
        car.setPrice(carItem.getPp());
        car.setPriceCurrency(carItem.getCs());
        return car;
    }

}
