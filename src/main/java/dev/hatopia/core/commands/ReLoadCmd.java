package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReLoadCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return true;

        ServerPlugin.getTemplateConfig().load();
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&aPomyślnie przeładowano konfiguracje!"));
        return false;
    }

}