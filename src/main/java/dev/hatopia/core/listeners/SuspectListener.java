package dev.hatopia.core.listeners;

import dev.hatopia.core.managers.SuspectManager;
import dev.hatopia.core.tasks.SuspectTask;
import dev.hatopia.global.utils.BossBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import static org.bukkit.Bukkit.getServer;

public class SuspectListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            player.sendTitle("", "");
            suspectManager.removeFromSuspicion();
            BossBarUtil.removeAllBossBars(player);
            SuspectTask suspectTask = new SuspectTask(player);
            suspectTask.cancelTask();

            getServer().dispatchCommand(Bukkit.getConsoleSender(), "tempban " + player.getName() + " 30d Wyjście z serwera podczas bycia podejrzanym.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player entity) {
            SuspectManager suspectManager = new SuspectManager(entity);
            if (suspectManager.inSuspect()) {
                event.setCancelled(true);
            }
        }
        if (event.getDamager() instanceof Player damager) {
            SuspectManager suspectManager = new SuspectManager(damager);
            if (suspectManager.inSuspect()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSwapHandItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            SuspectManager suspectManager = new SuspectManager(player);
            if (suspectManager.inSuspect()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        SuspectManager suspectManager = new SuspectManager(player);
        if (suspectManager.inSuspect()) {
            event.setCancelled(true);
        }
    }
}

