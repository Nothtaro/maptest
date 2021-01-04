package io.github.nothtaro.maptest.core;

import io.github.nothtaro.maptest.MapTestPlugin;
import org.bukkit.Bukkit;

public class MapThreader implements MapThread, Runnable{
    private int schedulerId = -1;
    private int expire = -1;
    private int counter = 0;

    private MapTask mapTask = null;

    private boolean paused = false;

    public void start(int delay, int period, int expire, MapTask mapTask) {
        this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MapTestPlugin.getInstance().getPlugin(), this, delay, period);
        this.mapTask = mapTask;
        this.expire = expire;
        Bukkit.broadcastMessage("リミットありのスレッドを開始します");
        //return this;
    }

    public void start(int delay, int period, MapTask mapTask) {
        this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MapTestPlugin.getInstance().getPlugin(), this, delay, period);
        this.mapTask = mapTask;
        Bukkit.broadcastMessage("リミットなしのスレッドを開始します");
        //return this;
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(schedulerId);
        Bukkit.broadcastMessage("終了");
        mapTask = null;
        schedulerId = -1;
        counter = 0;
    }

    @Override
    public void pause(boolean setPause) {
        this.paused = setPause;
    }

    public boolean isPaused() {
        return paused;
    }

    @Override
    public boolean hasExpire() {
        return this.expire != -1;
    }

    @Override
    public boolean isRunning() {
        return this.schedulerId != -1;
    }

    @Override
    public int getID() {
        return schedulerId;
    }

    @Override
    public void run() {
        //Bukkit.broadcastMessage("RUNNING: " + schedulerId);
        if(mapTask != null && !paused) {
            this.mapTask.run((expire - counter));
        }

        if(paused) {
            Bukkit.broadcastMessage("PAUSING");
        }

        if(hasExpire()) {
            Bukkit.broadcastMessage(expire - counter + "s remaining.");
            if(counter >= expire) {
                stop();
            }
            counter++;
        }
    }
}


