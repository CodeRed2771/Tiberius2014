package com.coderedrobotics.tiberius.libs;

/**
 *
 * @author Michael
 */
public class Timer {

    private int stage;
    private long startTime;

    public Timer() {

    }

    public void resetTimer(long time) {
        startTime = System.currentTimeMillis() + time;
    }

    public void advanceWhenReady() {
        if (startTime < System.currentTimeMillis()) {
            stage++;
        }
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
    
    public void nextStage() {
        this.stage++;
    }
}
