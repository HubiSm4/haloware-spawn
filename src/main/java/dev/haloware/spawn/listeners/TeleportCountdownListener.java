package dev.haloware.spawn.listeners;

import dev.haloware.ServerPlugin;
import dev.haloware.spawn.configs.SpawnConfig;
import dev.haloware.spawn.tasks.TeleportCountdownTask;
import dev.haloware.spawn.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeleportCountdownListener implements Listener {

    SpawnConfig spawnConfig = ServerPlugin.getSpawnConfig();
    String prefix = ServerPlugin.getSpawnConfig().getMessagePrefix();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (spawnConfig.isCancelOnMove()) {
            Player player = event.getPlayer();
            Location locationFrom = event.getFrom();
            Location locationTo = event.getTo();
            TeleportCountdownTask teleportCountdownTask = new TeleportCountdownTask(player);
            if (teleportCountdownTask.inCountdown()) {
                if (locationFrom.getBlockX() != locationTo.getBlockX() || locationFrom.getBlockZ() != locationTo.getBlockZ() || locationTo.getBlockY() > locationFrom.getBlockY()) {
                    teleportCountdownTask.cancelTask();
                    player.sendTitle(MessageUtil.fixColors(spawnConfig.getCancelOnMoveTitle()), MessageUtil.fixColors(spawnConfig.getCancelOnMoveSubtitle()));
                    player.sendMessage(MessageUtil.fixColors(prefix + spawnConfig.getCancelOnMoveMessage()));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (spawnConfig.isCancelOnDamage()) {
            if (event.isCancelled()) return;
            if (event.getEntity() instanceof Player player) {
                TeleportCountdownTask teleportCountdownTask = new TeleportCountdownTask(player);
                if (teleportCountdownTask.inCountdown()) {
                    teleportCountdownTask.cancelTask();
                    player.sendTitle(MessageUtil.fixColors(spawnConfig.getCancelOnDamageTitle()), MessageUtil.fixColors(spawnConfig.getCancelOnDamageSubtitle()));
                    player.sendMessage(MessageUtil.fixColors(prefix + spawnConfig.getCancelOnDamageMessage()));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TeleportCountdownTask teleportCountdownTask = new TeleportCountdownTask(player);
        if (teleportCountdownTask.inCountdown()) {
            teleportCountdownTask.cancelTask();
        }
    }
}
