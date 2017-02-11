package task5;

import static task5.Football.*;

class FootballPlayer implements Football.RunnableStatus {
    private Time time;
    private String name;
    private Football.State state = Football.State.FREE;
    private int reliability;
    private FootballArena stad;

    public FootballPlayer(String name, int reliability, FootballArena arena) {
        this.time = arena.getTime();
        this.name = name;
        this.reliability = reliability;
        this.stad = arena;
    }

    @Override
    public void run() {
        time.register(this);

        for (int i = 0; i < TOTAL_DAYS; i++) {
            checkStatus();
            if (time.getDayOfWeek() == Football.Week.SUNDAY && state == Football.State.FREE) {
                stad.playFootball();
            }
            time.nextDay();
        }
    }

    private void checkStatus() {
        int dice = (int) (Math.random() * MAX_CHANCE);
        if (dice < reliability) {
            state = Football.State.FREE;
        } else {
            state = Football.State.BUSY;
        }
    }

    @Override
    public String getStatus() {
        return toString();
    }

    @Override
    public String toString() {
        if(state == State.BUSY){
            return name + "(-)";
        }else if(state == State.FREE){
            return name + "(+)";
        }else {
            return name + "(+/-)";
        }
    }

    public String getName() {
        return name;
    }
}