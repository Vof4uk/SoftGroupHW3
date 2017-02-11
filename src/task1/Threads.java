package task1;

import java.util.Date;
import java.util.Random;

public class Threads {
    static final Random RANDOM = new Random(new Date().getTime());
    public static long random(){
        return Math.abs(RANDOM.nextInt() % 1000);
    }

    public static void main(String[] args) {
        final Thread th4 = new Thread(new RandomThread(4));
        final Thread th2 = new Thread(new RandomThread(2));
        final Thread th3 = new Thread(new RandomThread(3){
            @Override
            public void beforeShutDown() {
                super.beforeShutDown();
                if(!th2.isAlive()){
                    th4.start();
                }
            }
        });
        Thread th1 = new Thread(new RandomThread(1){
            @Override
            public void beforeShutDown() {
                super.beforeShutDown();
                th2.start();
                th3.start();
            }
        });

        th1.start();
    }
}
class RandomThread implements Runnable{
    private int count;
    public RandomThread(int num) {
        this.count = num;
    }

    public void init(){}

    public void beforeShutDown(){}

    @Override
    public final void run() {
        init();
        long timeout = 500 + Threads.random();
        System.out.printf("Thread %d started(lifetime = %dms)%n", count, timeout);
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread %d finished %n", count);
        beforeShutDown();
    }
}
