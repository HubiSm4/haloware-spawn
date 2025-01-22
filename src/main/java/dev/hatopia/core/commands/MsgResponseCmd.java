package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.databases.MsgDB;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class MsgResponseCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length < 1) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@swiadomość&7]"));
            return false;
        }

        MsgDB msgDBPlayer = new MsgDB(player);
        String lastUserName = (String) msgDBPlayer.getValueFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.LAST_WRITTEN_USER);

        if (lastUserName == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Aby tego użyć, musisz wcześniej pisać z jakimś graczem!"));
            return true;
        }

        Player target = Bukkit.getPlayer(lastUserName);
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + lastUserName + " &cnie ma na tym serwerze!"));
            return true;
        }

        String message = buildMessage(strings);

        getServer().dispatchCommand(player, "msg " + target.getName() + " " + message);
        return true;
    }

    private String buildMessage(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            stringBuilder.append(str).append(" ");
        }
        return MessageUtil.removeLastCharacter(stringBuilder.toString());
    }
}


