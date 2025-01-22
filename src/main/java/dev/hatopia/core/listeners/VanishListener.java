package dev.hatopia.core.listeners;

import dev.hatopia.core.managers.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (VanishManager.isVanished(onlinePlayer)) {
                player.hidePlayer(onlinePlayer);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (VanishManager.isVanished(player)) {
            VanishManager.showPlayer(player);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player && VanishManager.isVanished((Player)event.getTarget())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player && VanishManager.isVanished((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && VanishManager.isVanished((Player)event.getEntity())) {
            event.setCancelled(true);
        } else if (event.getEntity() instanceof Player && VanishManager.isVanished((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (VanishManager.isVanished(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
