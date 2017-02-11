package task2;

import java.util.Map;

public abstract class BuildingWithStorage implements ObjectOnMap{
    protected int xCoordinate;
    protected int yCoordinate;

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public abstract boolean loadTruck(Truck truck, Map<String, Integer> orderList);

    public abstract boolean unloadTruck(Truck truck);
}
