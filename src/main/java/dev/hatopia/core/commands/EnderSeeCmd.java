package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderSeeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 0) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7]"));
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + target.getName() + " &cnie ma na serwerze!"));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz otworzyć swojej skrzyni kresu!"));
            return true;
        }

        player.openInventory(target.getEnderChest());
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Otworzyłeś skrzynie kresu gracza @s" + target.getName() + "&7."));
        return true;
    }
}
