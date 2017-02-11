package task5;

import static task5.Football.*;

class Weather implements Football.RunnableStatus {
    private volatile boolean isRaining;
    private int rainChance;
    private Time time;

    public Weather(int rainChance, Time time) {
        this.rainChance = rainChance % MAX_CHANCE;
        this.time = time;
    }

    public boolean isRaining() {
        return isRaining;
    }

    @Override
    public synchronized String getStatus() {
        if (isRaining) {
            return "____RAIN____";
        }
        return "FINE_WEATHER";
    }

    @Override
    public void run() {
        time.register(this);
        for (int i = 0; i < TOTAL_DAYS; i++) {
            generateWeather();
            time.nextDay();
        }
    }

    private synchronized void generateWeather() {
        int dice = (int) (Math.random() * MAX_CHANCE);
        isRaining = dice < rainChance;
    }
}