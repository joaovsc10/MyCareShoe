package com.example.mycareshoe.helpers;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;

import java.io.Serializable;

public class ChronometerHelper implements Serializable {
    private long timeWhenStopped = 0;
    private boolean isRunning = false;
    private Chronometer chrono;

    public long getTimeWhenStopped() {
        return timeWhenStopped;
    }

    public void setTimeWhenStopped(long timeWhenStopped) {
        this.timeWhenStopped = timeWhenStopped;
        getChrono().setBase(SystemClock.elapsedRealtime() - getTimeWhenStopped());
        //      long a= SystemClock.elapsedRealtime();
    }

    public ChronometerHelper() {
    }

    public ChronometerHelper(Chronometer chrono) {
        this.chrono = chrono;
    }

    public Chronometer getChrono() {
        return chrono;
    }

    public void setChrono(Chronometer chrono) {
        this.chrono = chrono;
    }

    private final String getTimeKey() {
        return "KEY_TIMER_TIME" + getChrono().getId();
    }

    private final String getIsRunningKey() {
        return "KEY_TIMER_RUNNING" + getChrono().getId();
    }

    public void start() {
        getChrono().setBase(SystemClock.elapsedRealtime() - getTimeWhenStopped());
        isRunning = true;
        getChrono().start();
    }

    public long stop() {
        isRunning = false;
        setTimeWhenStopped(SystemClock.elapsedRealtime() - getChrono().getBase());
        getChrono().stop();

        return getTimeWhenStopped();
    }

    public long reset() {
        getChrono().stop();
        isRunning = false;
        getChrono().setBase(SystemClock.elapsedRealtime());
        setTimeWhenStopped(0);

        return getTimeWhenStopped();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public long getCurrentTime() {
        return timeWhenStopped;
    }

    public void setCurrentTime(long time) {
        setTimeWhenStopped(time);
        long a = SystemClock.elapsedRealtime();
        long b = getTimeWhenStopped();
        getChrono().setBase(SystemClock.elapsedRealtime() - getTimeWhenStopped());

    }

    public void saveInstanceState(Bundle outState) {
        if (getChrono() != null) {
            if (isRunning) {
                setTimeWhenStopped(SystemClock.elapsedRealtime() - getChrono().getBase());
            }

            outState.putLong(getTimeKey(), getCurrentTime());
            outState.putBoolean(getIsRunningKey(), isRunning());
        }
    }

    public void restoreInstanceState(Bundle inState) {
        if (getChrono() != null) {
            isRunning = inState.getBoolean(getIsRunningKey());
            setCurrentTime(inState.getLong(getTimeKey()));
            setTimeWhenStopped(SystemClock.elapsedRealtime() - getChrono().getBase());
            if (isRunning) {
                getChrono().start();
            }
        }
    }
}
