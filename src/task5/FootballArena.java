package task5;

import java.util.concurrent.atomic.AtomicInteger;

import static task5.Football.*
        ;

class FootballArena implements Football.RunnableStatus {
    private Time time;
    private Weather weather;
    private String status;
    private final AtomicInteger playersCount = new AtomicInteger(0);

    public FootballArena(Time time, Weather weather) {
        this.time = time;
        this.weather = weather;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void run() {
        time.register(this);
        for (int i = 0; i < TOTAL_DAYS; i++) {
            try {
                Thread.sleep(DAY_DURATION / 4);
                checkConditionsAndPlay();
                Thread.sleep(DAY_DURATION / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playersCount.set(0);
            time.nextDay();
        }
    }

    public Time getTime() {
        return time;
    }

    public synchronized void playFootball() {
        if (playersCount.get() < PLAYERS_TO_START) {
            int players = playersCount.incrementAndGet();
        }
    }

    private void checkConditionsAndPlay() {
        if (checkConditions()) {
            status = String.format("__________FOOTBALL__________");
        } else {
            status = String.format("NO FOOTBALL TODAY(%s players)", playersCount.get());
        }
//            System.out.println(playersCount.get());
    }

    private boolean checkConditions() {
        return !weather.isRaining() && playersCount.get() >= PLAYERS_TO_START;
    }
}