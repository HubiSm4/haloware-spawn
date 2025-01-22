package dev.hatopia.core.listeners;

import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class KickListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            event.setKickMessage(MessageUtil.fixColors("&cSerwer jest przepełniony! Nie posiadasz wystarczającej rangi, aby móc wejść na pełny serwer. ¬_¬"));
        }
        if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
            event.setKickMessage(MessageUtil.fixColors("&cSerwer jest dostępny tylko dla administracji! (●'◡'●)"));
        }
    }
}
