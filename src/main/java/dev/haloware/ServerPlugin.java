package dev.haloware;

import dev.haloware.metrics.Metrics;
import dev.haloware.spawn.HookSpawn;
import dev.haloware.spawn.configs.SpawnConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugin extends JavaPlugin {

    @Getter
    private static ServerPlugin plugin;

    @Getter
    private static SpawnConfig spawnConfig;

    @Override
    public void onLoad() {
        plugin = this;

        HookSpawn hookSpawn = new HookSpawn();
        hookSpawn.config();

        hookConfigs();
    }

    @Override
    public void onEnable() {
        System.out.println("Enabling plugin...");

        HookSpawn hookSpawn = new HookSpawn();
        hookSpawn.commands();
        hookSpawn.listeners();

        int pluginId = 24593;
        new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        System.out.println("Disabling plugin...");
    }

    private void hookConfigs() {
        spawnConfig = HookSpawn.spawnConfig;
    }

}


