package io.github.nothtaro.maptest;

import io.github.nothtaro.maptest.core.listener.CommandListener;
import io.github.nothtaro.maptest.core.listener.EventListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapTest extends JavaPlugin {
    @Override
    public void onEnable() {
        MapTestPlugin.getInstance().setPlugin(this.getServer().getPluginManager().getPlugin("MapTest"));
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getCommand("maptest").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() { }
}
