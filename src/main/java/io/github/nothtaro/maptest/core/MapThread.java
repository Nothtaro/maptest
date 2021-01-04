package io.github.nothtaro.maptest.core;

import io.github.nothtaro.maptest.core.MapTask;

public interface MapThread {
    void start(int delay, int period, int expire, MapTask mapTask);

    void start(int delay, int period, MapTask mapTask);

    void stop();

    void pause(boolean setPause);

    boolean isPaused();

    boolean hasExpire();

    boolean isRunning();

    int getID();
}
