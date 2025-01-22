package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StpCmd implements CommandExecutor { ;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length >= 1) {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == player) {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz teleportować się do siebie!"));
                return true;
            }
            if (target == null) {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + strings[0] + " &cnie ma na serwerze. Sprawdź poprawność znaków!"));
                return true;
            }
            target.teleport(player.getLocation());
            target.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Przeteleportowano Cię do @s" + player.getName() + "&7."));
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Przeteleportowałeś @s" + target.getName() + " &7do siebie!"));
        } else {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7]"));
        }
        return false;
    }
}
