package dev.haloware.spawn;

import dev.haloware.ServerPlugin;
import dev.haloware.spawn.commands.SpawnCmd;
import dev.haloware.spawn.configs.SpawnConfig;
import dev.haloware.spawn.listeners.PlayerJoinListener;
import dev.haloware.spawn.listeners.TeleportCountdownListener;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;

import java.io.File;
import java.util.stream.Stream;

public class HookSpawn {

    public static SpawnConfig spawnConfig;

    public void listeners() {
        Stream.of(
                new TeleportCountdownListener(),
                new PlayerJoinListener()
        ).forEach(listener -> ServerPlugin.getPlugin().getServer().getPluginManager().registerEvents(listener, ServerPlugin.getPlugin()));
    }

    public void commands() {
        ServerPlugin.getPlugin().getCommand("spawn").setExecutor(new SpawnCmd());
    }

    public void config() {
        spawnConfig = ConfigManager.create(SpawnConfig.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(new File(ServerPlugin.getPlugin().getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }

}
