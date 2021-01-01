package io.github.nothtaro.maptest.core;

import io.github.nothtaro.maptest.core.MapTask;

public interface IMapThread {
    void start(int delay, int expire, MapTask mapTask);

    void start(int delay, MapTask mapTask);

    void stop();

    boolean hasExpire();

    boolean isRunning();

    int getID();
}
