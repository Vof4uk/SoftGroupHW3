package task2;

import java.util.Arrays;
import java.util.Map;

public class Shopp extends WareHouse implements Runnable{
    public static final int TIME_PIECE = 300;

    private WareHouse wareHouse;
    private int sellingRate;

    public Shopp(Map<String, Integer> storage, WareHouse wareHouse, int sellingRate, Truck... trucks) {
        this.storage = storage;
        this.wareHouse = wareHouse;
        this.trucks.addAll(Arrays.asList(trucks));
        this.sellingRate = sellingRate;
    }

    @Override
    public void run() {
        while (true){
            sell();
            try {
                Thread.sleep(TIME_PIECE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sell() {

    }
}
