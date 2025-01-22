package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        Player target = strings.length == 0 ? player : Bukkit.getPlayer(strings[0]);
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + strings[0] + " &cnie ma na serwerze!"));
            return true;
        }

        healPlayer(target);
        String message = target.equals(player)
                ? "&7Twój głód oraz zdrowie zostało zaspokojone!"
                : "&7Głód oraz zdrowie gracza @s" + target.getName() + " &7zostało zaspokojone!";
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + message));
        return true;
    }

    private void healPlayer(Player player) {
        player.setFoodLevel(20);
        player.setHealth(20.0D);
    }
}

