package dev.hatopia.core.listeners;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.databases.CooldownDB;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String[] commandArgs = event.getMessage().substring(1).trim().split(" ");

        if (!player.hasPermission(ServerPlugin.getTemplateConfig().getPermissionListenerPrefix() + "commandcooldown") && !ServerPlugin.getTemplateConfig().getSkipCooldownCommands().contains(commandArgs[0].toLowerCase())) {
            CooldownDB cooldownDB = new CooldownDB(player);
            CooldownDB.Cooldown cooldown = CooldownDB.Cooldown.CMD;
            if (cooldownDB.isOnCooldown(cooldown)) {
                String remainingTime = cooldownDB.getRemainingCooldownTime(cooldown);
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cKolejną komendę możesz wysłać ponownie za &4" + remainingTime + "&c! (#_<-)"));
                event.setCancelled(true);
                return;
            } else {
                cooldownDB.addCooldown(cooldown);
            }
        }

        if (!player.hasPermission(ServerPlugin.getTemplateConfig().getPermissionListenerPrefix() + "commandavailable")) {
            boolean commandFound = false;

            for (String commandName : ServerPlugin.getTemplateConfig().getPlayerCommandsAvailable()) {
                Command command = ServerPlugin.getPlugin().getCommand(commandName);
                if (command.getName().equalsIgnoreCase(commandArgs[0]) || command.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(commandArgs[0]))) {
                    String permission = command.getPermission();

                    if (permission != null && !player.hasPermission(permission)) {
                        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie posiadasz uprawnienia do używania komendy &4/" + commandName + "&c! (˘･_･˘)"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        event.setCancelled(true);
                    }

                    commandFound = true;
                    break;
                }
            }

            if (!commandFound) {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cKomenda &4/" + commandArgs[0] + " &cnie istnieje! ::>_<::"));
                event.setCancelled(true);
            }
        }
    }
}



