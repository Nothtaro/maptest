package io.github.nothtaro.maptest;

import org.bukkit.plugin.Plugin;

public class MapTestPlugin {
    private static final MapTestPlugin instance = new MapTestPlugin();
    private Plugin plugin = null;

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public static MapTestPlugin getInstance() {
        return instance;
    }
}
