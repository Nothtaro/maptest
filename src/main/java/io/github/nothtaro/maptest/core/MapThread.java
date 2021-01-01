package io.github.nothtaro.maptest.core;

import io.github.nothtaro.maptest.MapTestPlugin;
import org.bukkit.Bukkit;

public class MapThread implements IMapThread, Runnable{
    private int schedulerId = -1;
    private int expire = -1;
    private int counter = 0;

    private MapTask mapTask = null;

    public void start(int delay, int expire, MapTask mapTask) {
        this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MapTestPlugin.getInstance().getPlugin(), this, delay, 20);
        this.mapTask = mapTask;
        this.expire = expire;
        Bukkit.broadcastMessage("リミットありのスレッドを開始します");
        //return this;
    }

    public void start(int delay, MapTask mapTask) {
        this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MapTestPlugin.getInstance().getPlugin(), this, delay, 10);
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
        if(mapTask != null) {
            this.mapTask.run((expire - counter));
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


