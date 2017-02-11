package task5;

public class Football {
    static final int WEEK_DURATION = 2000;//ms
    static final int DAY_DURATION = WEEK_DURATION / 7;
    static final int TOTAL_DAYS = 100;//ms
    static final int RAIN_CHANCE = 35;//%
    static final int MAX_CHANCE = 100;//%
    static final int PLAYERS_TO_START = 10;//%
    static final String[] PLAYERS = {"Anton", "Max", "Vovan", "Viktor", "Alex",
            "Igor", "Serg", "Greg", "Ruslan", "Fedir"};
    static final Week[] LOGGED_DAYS = {Week.SUNDAY, };
//            Week.MONDAY, Week.THURSDAY, Week.TUESDAY, Week.WEDNESDAY, Week.FRIDAY, Week.SATURDAY};

    enum Week {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    enum State {
        FREE, BUSY
    }

    interface RunnableStatus extends Runnable {
        String getStatus();
    }

    public static void main(String[] args) {
        Football football = new Football();
        Time time = new Time();
        Weather weather = new Weather(RAIN_CHANCE, time);
        FootballArena arena = new FootballArena(time, weather);

        //to log in desired order
        try {
            new Thread(arena).start();
            Thread.sleep(100);
            new Thread(weather).start();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < PLAYERS.length; i++) {
            FootballPlayer footballPlayer = new FootballPlayer(PLAYERS[i], 99 - i, arena);
            new Thread(footballPlayer, footballPlayer.getName()).start();
        }
        new Thread(time).start();
    }
}
