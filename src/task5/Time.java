package task5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Phaser;

import static task5.Football.*;

class Time implements Runnable {
    private Phaser dayFlow = new Phaser(1);
    private List<Football.RunnableStatus> listeners = new CopyOnWriteArrayList<>();
    private volatile Football.Week dayOfWeek = Football.Week.MONDAY;

    public void nextDay() {
        dayFlow.awaitAdvance(dayFlow.getPhase());
    }

    public Football.Week getDayOfWeek() {
        return dayOfWeek;
    }

    private void midnight() {
        dayOfWeek = Football.Week.values()[(dayOfWeek.ordinal() + 1) % 7];
        dayFlow.arrive();
    }

    @Override
    public void run() {
        Thread logger = new Thread(new Time.Logger(LOGGED_DAYS));
        logger.setDaemon(true);
        logger.start();

        dayFlow.arrive();

        for (int i = 0; i < TOTAL_DAYS + 5; i++) {
            try {
                Thread.sleep(DAY_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            midnight();
        }
    }

    public void register(Football.RunnableStatus runnableStatus) {
        listeners.add(runnableStatus);
        dayFlow.awaitAdvance(dayFlow.getPhase());
    }

    private class Logger implements Runnable {

        private Set<Football.Week> logDays = new HashSet<>();

        public Logger(Football.Week... logDays) {
            this.logDays.addAll(Arrays.asList(logDays));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(DAY_DURATION / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (logDays.contains(dayOfWeek)) {
                    String dayLog = collectLogs(listeners);
                    System.out.printf("%s : %s%n", dayOfWeek, dayLog);
                }
                nextDay();
            }
        }

        private String collectLogs(List<Football.RunnableStatus> listeners) {
            StringBuilder sb = new StringBuilder();
            for (Football.RunnableStatus object : listeners) {
                sb.append(object.getStatus());
                sb.append(" ");
            }
            return sb.toString();
        }
    }
}