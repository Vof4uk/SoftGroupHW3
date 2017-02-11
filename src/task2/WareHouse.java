package task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WareHouse extends BuildingWithStorage implements Runnable{

    protected Map<String, Integer> storage;
    protected int capacity;
    protected List<Truck> trucks = Collections.synchronizedList(new ArrayList<Truck>());

    public WareHouse() {
    }

    public WareHouse(int xCoordinate, int yCoordinate, Map<String, Integer> initialStorage, int capacity) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.storage = initialStorage;
        this.capacity = capacity;
    }

    @Override
    public synchronized boolean loadTruck(Truck truck, Map<String, Integer> loadList){
        if(!truckIsHere(truck) || checkAvailable(storage, loadList)) {
            throw new RuntimeException("truck not arrived yet");
        }
            if(truck.load(loadList)){
                for (String key: loadList.keySet()){
                    storage.put(key, storage.get(key) - loadList.get(key));
                }
                return true;
            }
        return false;
    }

    @Override
    public boolean unloadTruck(Truck truck) {
        int weight = truck.getCargoWeight();
        if(enoughSpaceFor(weight)){
            this.receive(truck.unLoad());
            return true;
        }
        return false;
    }

    private void receive(Map<String, Integer> cargo) {
        for (String key :
                cargo.keySet()) {
            if (storage.containsKey(key)){
                storage.put(key, storage.get(key) + cargo.get(key));
            }else{
                storage.put(key, cargo.get(key));
            }
        }
    }

    private boolean truckIsHere(Truck truck) {
        return truck.getXCoordinate() == this.xCoordinate &&
                truck.getYCoordinate() == this.yCoordinate;
    }

    private boolean checkAvailable(Map<String, Integer> storage, Map<String, Integer> loadList){
        for (Map.Entry<String, Integer> pair : loadList.entrySet()) {
            if(storage.get(pair.getKey()) == null || storage.get(pair.getKey()) < pair.getValue()){
                return false;
            }
        }
        return true;
    }

    private int totalWeightInStock(){
        int weight = 0;
        for (Map.Entry<String, Integer> pair: storage.entrySet()) {
            weight += pair.getValue();
        }
        return weight;
    }

    private boolean enoughSpaceFor(int weight){
        return (capacity - totalWeightInStock() >= weight);
    }

}
