package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length < 1) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7] &8| &7[@sx&7] [@sy&7] [@sz&7] &8| &7[@sx&7] [@sy&7] [@sz&7] [@syaw&7] [@spitch&7] &8| &7[@sx&7] [@sy&7] [@sz&7] [@syaw&7] [@spitch&7] [@sbodyYaw&7]"));
            return true;
        }

        if (strings.length == 1) {
            teleportToPlayer(player, strings[0]);
            return true;
        }

        if (strings.length >= 3 && strings.length <= 6) {
            teleportToCoordinates(player, strings);
            return true;
        }

        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7] &8| @s/" + command.getName() + " &7[@sx&7] [@sy&7] [@sz&7] &8| @s/" + command.getName() + " &7[@sx&7] [@sy&7] [@sz&7] [@syaw&7] [@spitch&7] &8| @s/" + command.getName() + " &7[@sx&7] [@sy&7] [@sz&7] [@syaw&7] [@spitch&7] [@sbodyYaw&7]"));
        return true;
    }

    private void teleportToPlayer(Player player, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + targetName + " &cnie ma na serwerze. Sprawdź poprawność znaków!"));
            return;
        }
        player.teleport(target);
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Przeteleportowano do gracza @s" + target.getName() + "&7."));
    }

    private void teleportToCoordinates(Player player, String[] strings) {
        double x = parseCoordinate(player, strings[0], "X");
        double y = parseCoordinate(player, strings[1], "Y");
        double z = parseCoordinate(player, strings[2], "Z");
        if (x == Double.MIN_VALUE || y == Double.MIN_VALUE || z == Double.MIN_VALUE) return;

        float yaw = 0, pitch = 0, bodyYaw = 0;

        if (strings.length >= 5) {
            yaw = parseAngle(player, strings[3], "Yaw");
            pitch = parseAngle(player, strings[4], "Pitch");
            if (yaw == Float.MIN_VALUE || pitch == Float.MIN_VALUE) return;
        }

        if (strings.length == 6) {
            bodyYaw = parseAngle(player, strings[5], "BodyYaw");
            if (bodyYaw == Float.MIN_VALUE) return;
            player.setBodyYaw(bodyYaw);
        }

        player.teleport(new Location(player.getWorld(), x, y, z, yaw, pitch));
        String message = String.format("Przeteleportowano na koordynaty X: @s%s&7, Y: @s%s&7, Z: @s%s", x, y, z);
        if (strings.length >= 5) {
            message += String.format("&7, Yaw: @s%s&7, Pitch: @s%s", yaw, pitch);
        }
        if (strings.length == 6) {
            message += String.format("&7, BodyYaw: @s%s", bodyYaw);
        }
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + message));
    }

    private double parseCoordinate(Player player, String value, String name) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + name + " musi być liczbą!"));
            return Double.MIN_VALUE;
        }
    }

    private float parseAngle(Player player, String value, String name) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + name + " musi być liczbą!"));
            return Float.MIN_VALUE;
        }
    }
}