package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.guis.chest.SuspectConfessGui;
import dev.hatopia.core.managers.SuspectManager;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SuspectConfessCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        SuspectManager suspectManager = new SuspectManager(player);
        if (!suspectManager.inSuspect()) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cMożesz tego użyć tylko wtedy gdy jesteś podejrzany!"));
            return true;
        }

        if (!suspectManager.isAbilityToConfess()) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cAdministrator &4" + suspectManager.getPlayerAdministrator() + " &cwyłączył możliwość przyznania się!"));
            return true;
        }

        SuspectConfessGui.openGui(player);
        return false;
    }
}
