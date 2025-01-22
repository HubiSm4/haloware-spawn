package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpeedCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 1) {
            String arg = strings[0];
            float speed = parseSpeedArgument(arg);
            if (speed >= 0) {
                player.setFlySpeed(speed);
                player.setWalkSpeed(speed);
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Ustawiono szybkość latania oraz biegania na @s" + speed + "&7!"));
            } else {
                player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cWartość musi być liczbą od &41 &cdo &410&c!"));
            }
        } else {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@swartość&7] &8(Domyślny 2)"));
        }
        return false;
    }

    private float parseSpeedArgument(String arg) {
        try {
            int intSpeed = Integer.parseInt(arg);
            float speed = intSpeed * 0.1f;
            if (speed >= 0.1f && speed <= 1.0f) {
                return speed;
            }
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }
}


