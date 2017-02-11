package task2;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static task2.Shopp.TIME_PIECE;

public class Truck implements ObjectOnMap, Runnable{
    private final int speed;
    private int xCoordinate;
    private int yCoordinate;
    private Map<String, Integer> cargo;
    private final int capacity;
    private int cargoWeight;

    public Truck(int speed, int capacity, Shopp shopp) {
        this.xCoordinate = shopp.getXCoordinate();
        this.yCoordinate = shopp.getYCoordinate();
        this.speed = speed;
        this.capacity = capacity;
    }

    public int getCargoWeight() {
        return cargoWeight;
    }

    public void moveTo(int x, int y){
        int dx = 1;
        int dy = 1;
        while (dx != 0 && dy != 0) {
            dx = Math.abs(x - this.xCoordinate);
            dy = Math.abs(y - this.yCoordinate);
            int xDirection = (x - this.xCoordinate) / dx;
            int yDirection = (y - this.yCoordinate) / dy;

            if(dx >= dy){
                this.xCoordinate -= Math.min(dx, speed) * xDirection;
            }else {
                this.yCoordinate -= Math.min(dy, speed) * yDirection;
            }

            try {
                Thread.sleep(TIME_PIECE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void deliver(BuildingWithStorage from, BuildingWithStorage to, Map<String, Integer> orderList){
        moveTo(from.getXCoordinate(), from.getYCoordinate());
        from.loadTruck(this, orderList);
        moveTo(to.getXCoordinate(), to.getYCoordinate());
        to.unloadTruck(this);
    }

    public Map<String, Integer> unLoad(){
        Map<String, Integer> result = this.cargo;
        cargo = Collections.emptyMap();
        cargoWeight = 0;
        return result;
    }

    public boolean load(Map<String, Integer> cargo){
        int weight = calculateWeight(cargo);
        if(weight <= capacity) {
            this.cargo = cargo;
            cargoWeight = weight;
            return true;
        }
        else {
            return false;
        }
    }

    private int calculateWeight(Map<String, Integer> cargo) {
        int weight = 0;
        for (Map.Entry<String, Integer> pair: cargo.entrySet()) {
            weight += pair.getValue();
        }
        return weight;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
}
