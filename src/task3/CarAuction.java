package task3;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static task3.CarAuction.PAUSE;

public class CarAuction {
    static final int CUSTOMERS_TO_START = 10;
    static final int CARS_TO_START = 5;
    static final int PAUSE =500;
    static String[] CARS = {"BMW", "Mercedez", "VW", "lanos", "Audi"};
    public static void main(String[] args) {
        CarRetailer cr = new CarRetailer(CARS_TO_START, CUSTOMERS_TO_START);
        new Thread(cr).start();

        CarSupplier cs = new CarSupplier(cr);
        new Thread(cs).start();

        for (int i = 0; i < CUSTOMERS_TO_START; i++) {
            Customer c = new Customer(cr);
            new Thread(c).start();
        }
    }


}
class CarRetailer implements Runnable{
    private CountDownLatch customersLatch;
    private CountDownLatch carsLatch;
    ArrayList<String> carsList;

    public CarRetailer(int carstToStart, int customersToStart) {
        carsLatch = new CountDownLatch(carstToStart);
        customersLatch = new CountDownLatch(customersToStart);
        carsList = new ArrayList<>(carstToStart);
    }

    @Override
    public void run() {
        System.out.println("Carshop announces sale");
        try {
            carsLatch.await();
            customersLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sale starts. door is open");
    }

    public synchronized String buyCar(){
        if(carsList.isEmpty()){
            return null;
        }
        else {
           return carsList.remove(0);
        }
    }

    public void registerCustomer(Customer customer){
        customersLatch.countDown();
        System.out.println(customersLatch.getCount() + " clients left");
        try {
            carsLatch.await();
            customersLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized void registerCar(String car){
        System.out.printf("new car Lot %s. ",car);
        carsList.add(car);
        carsLatch.countDown();
        System.out.println(carsLatch.getCount() + " cars left");

    }
}

class Customer implements Runnable{
    private static int count = 0;

    private String name;
    private CarRetailer shop;

    public Customer(CarRetailer shop) {
        this.shop = shop;
        name = "customer" + count++;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(PAUSE);
            System.out.println(name + " arrived");
            shop.registerCustomer(this);
            Thread.sleep(PAUSE);
            System.out.println(name + " is looking for new car...");
            Thread.sleep(PAUSE);
            String car = this.shop.buyCar();
            if(car != null){
                System.out.println(name + " baught " + car);
            }else{
                System.out.println(name + " left with nothing");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class CarSupplier implements Runnable{
    private CarRetailer cr;

    public CarSupplier(CarRetailer cr) {
        this.cr = cr;
    }

    @Override
    public void run() {
        for (String car: CarAuction.CARS) {
            cr.registerCar(car);
        }
    }
}