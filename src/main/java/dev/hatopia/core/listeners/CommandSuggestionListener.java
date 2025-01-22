package dev.hatopia.core.listeners;

import dev.hatopia.ServerPlugin;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandSuggestionListener implements Listener {

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(ServerPlugin.getTemplateConfig().getPermissionCommandPrefix() + "listener.commandSuggestion")) {
            event.getCommands().clear();

            for (String commandName : ServerPlugin.getTemplateConfig().getPlayerCommandsAvailable()) {
                Command command = ServerPlugin.getPlugin().getCommand(commandName);
                if (command.getPermission() != null && player.hasPermission(command.getPermission())) {
                    event.getCommands().add(command.getName());
                    event.getCommands().addAll(command.getAliases());
                }
            }
        }
    }
}
