package dev.hatopia.core.listeners;

import dev.hatopia.core.databases.CooldownDB;
import dev.hatopia.core.databases.MsgDB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HookPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MsgDB msgDB = new MsgDB(player);
        msgDB.loadCacheAsync();
        CooldownDB cooldownDB = new CooldownDB(player);
        cooldownDB.loadCacheAsync();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        MsgDB msgDB = new MsgDB(player);
        msgDB.putCacheToDatabaseAndUnloadAsync();
        CooldownDB cooldownDB = new CooldownDB(player);
        cooldownDB.putCacheToDatabaseAndUnloadAsync();
    }
}

