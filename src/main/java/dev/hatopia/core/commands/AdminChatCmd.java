package dev.hatopia.core.commands;

import dev.hatopia.global.connections.RedisConnection;
import dev.hatopia.global.utils.GoxyUtil;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import dev.hatopia.ServerPlugin;

public class AdminChatCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 0) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@swiadomość&7]"));
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : strings) {
            stringBuilder.append(arg).append(" ");
        }
        String msg = MessageUtil.removeLastCharacter(stringBuilder.toString());

        RedisConnection redisConnection = new RedisConnection();
        redisConnection.publish("adminchat", MessageUtil.fixColors("&6&lADMINCHAT &8▷ &7[&a" + GoxyUtil.getServerName() + "&7] " + player.getName() + ": &f" + msg));
        return false;
    }
}


