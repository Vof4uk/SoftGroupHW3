package task4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Shop implements Runnable{
    private static final int CLIENTS_TO_OPEN = 5;
    private static final int SHOP_CAPACITY = 5;
    private static final int BREAK_DURATION = 10000;
    private static final int[] CUSTOMERS_SCHEDULE = {0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 5, 6, 5, 8, 10, 3, 2, 5, 4};

    private volatile boolean isOpen = false;
    private CyclicBarrier minimumCustomersCondition = new CyclicBarrier(CLIENTS_TO_OPEN, new Runnable() {
        @Override
        public void run() {
            isOpen = true;
        }
    });
    private Semaphore guardian = new Semaphore(SHOP_CAPACITY);
    private AtomicInteger customersIn = new AtomicInteger(0);

    public static void main(String[] args) {
        Shop shop = new Shop();
        for (int i = 0; i < CUSTOMERS_SCHEDULE.length; i++) {
            try {
                Thread.sleep(CUSTOMERS_SCHEDULE[i]*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Customer(shop).init();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void customerLeaves(Customer customer){
        customersIn.decrementAndGet();
        guardian.release();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(isOpen) {
            pauseIfNeeded();
        }
    }

    public void customerWantsInside(Customer customer){
        try {
            if(!isOpen) {
                minimumCustomersCondition.await();
            }
            guardian.acquire();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            try {
                Thread.sleep(100);
                minimumCustomersCondition.await();
            } catch (InterruptedException | BrokenBarrierException e1) {
                throw new RuntimeException(e);
            }
        }
        customersIn.getAndIncrement();
    }

    private void pauseIfNeeded() {
        if(customersIn.get() < 4){
            isOpen = false;
            System.out.println("shop closes for coffee break");
            try {
                while (minimumCustomersCondition.getNumberWaiting() < CLIENTS_TO_OPEN) {
                    Thread.sleep(BREAK_DURATION);
                }
                System.out.println("coffee break finished");
                minimumCustomersCondition.reset();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isOpen = true;
        }
    }

    public String getStatus() {
        return String.format("(%d INSIDE and %d OUTSIDE)",customersIn.get(), guardian.getQueueLength() + minimumCustomersCondition.getNumberWaiting());
    }
}
class Customer implements Runnable{
    private static int count = 0;
    private Shop shop;
    private Thread life;

    private String name;
    public Customer(Shop shop) {
        this.shop = shop;
        this.name = "customer" + count++;
    }

    public void init(){
        life = new Thread(this);
        life.start();
    }

    @Override
    public void run() {
        System.out.printf("%s wants to enter, %s%n", name, shop.getStatus());
        shop.customerWantsInside(this);
        System.out.printf("%s enters, %s%n", name, shop.getStatus());
        try {
            Thread.sleep((int)(1 + Math.random()*7)*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shop.customerLeaves(this);
        System.out.printf("%s bought something and leaves, %s%n", name, shop.getStatus());
    }

    public Thread getLife(){
        return life;
    }
 }
