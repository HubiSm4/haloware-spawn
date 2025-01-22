package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        Player target = player;
        if (strings.length >= 2) {
            target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + strings[1] + " &cnie ma na serwerze!"));
                return true;
            }
        }

        if (strings.length >= 1) {
            GameMode gameMode = null;
            if (strings[0].equals("0") || strings[0].toLowerCase().startsWith("s")) {
                gameMode = GameMode.SURVIVAL;
            } else if (strings[0].equals("1") || strings[0].toLowerCase().startsWith("c")) {
                gameMode = GameMode.CREATIVE;
            } else if (strings[0].equals("2") || strings[0].toLowerCase().startsWith("a")) {
                gameMode = GameMode.ADVENTURE;
            } else if (strings[0].equals("3") || strings[0].toLowerCase().startsWith("sp")) {
                gameMode = GameMode.SPECTATOR;
            }

            if (gameMode != null) {
                target.setGameMode(gameMode);
                target.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Zmieniono Twój tryb gry na @s" + target.getGameMode().name() + "&7!"));
                if (!target.equals(player)) {
                    player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Zmieniono tryb gry gracza @s" + target.getName() + "&7 na @s" + gameMode.name() + "&7!"));
                }
            } else {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNieznany tryb gry: &4" + strings[0]));
            }
            return true;
        } else {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@s0&7/@s1&7/@s2&7/@s3&7] &8| @s/" + command.getName() + " &7[@s0&7/@s1&7/@s2&7/@s3&7] [@sgracz&7]"));
        }
        return false;
    }

}
