package dev.hatopia.core.commands;

import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        player.kickPlayer(MessageUtil.fixColors("&7Przenoszenie do poczekalni." + "#!{VOID}"));
        return false;
    }
}
