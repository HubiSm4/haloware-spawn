package dev.haloware.spawn.listeners;

import dev.haloware.ServerPlugin;
import dev.haloware.spawn.configs.SpawnConfig;
import dev.haloware.spawn.utils.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    SpawnConfig spawnConfig = ServerPlugin.getSpawnConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (spawnConfig.isTeleportOnJoin()) {
            Player player = event.getPlayer();
            player.teleport(ConfigUtil.parseLocation(spawnConfig.getLocationWorld() + ";" + spawnConfig.getLocationX() + ";" + spawnConfig.getLocationY() + ";" + spawnConfig.getLocationZ() + ";" + spawnConfig.getLocationYaw() + ";" + spawnConfig.getLocationPitch()));
        }
    }
}
