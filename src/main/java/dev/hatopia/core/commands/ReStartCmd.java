package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReStartCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            try {
                int count = Integer.parseInt(strings[0]);
                if (count >= 0 && count <= 10) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (count == 0) {
                            player.sendTitle(MessageUtil.fixColors("&cᴘʀᴢᴇᴘʀᴀѕᴢᴀᴍʏ ᴢᴀ ɴɪᴇᴅᴏɢᴏᴅɴᴏѕᴄɪ"), MessageUtil.fixColors("&4Ponowne uruchomienie serwera"));
                            player.sendMessage(MessageUtil.fixColors("&4⚠ &cPonowne uruchomienie serwera!"));
                            player.kickPlayer("#!{VOID}");
                        } else {
                            player.sendTitle(MessageUtil.fixColors("&cᴘʀᴢᴇᴘʀᴀѕᴢᴀᴍʏ ᴢᴀ ɴɪᴇᴅᴏɢᴏᴅɴᴏѕᴄɪ"), MessageUtil.fixColors("&cPonowne uruchomienie serwera za &4" + count + "&cs"));
                            player.sendMessage(MessageUtil.fixColors("&4⚠ &cPonowne uruchomienie serwera za &4" + count + "&cs."));
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, 1f);
                    });
                } else {
                    commandSender.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cProszę podać liczbę z zakresu od 0 do 10."));
                }
            } catch (NumberFormatException e) {
                commandSender.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cProszę podać prawidłową liczbę całkowitą."));
            }
        } else {
            commandSender.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cPodaj argument."));
        }
        return false;
    }
}
