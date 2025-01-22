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

import java.util.ArrayList;

public class MsgMessageCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length < 2) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7] [@swiadomość&7]"));
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);
        if (!isValidTarget(player, target)) return true;

        MsgDB msgDBPlayer = new MsgDB(player);
        MsgDB msgDBTarget = new MsgDB(target);

        if (!canSendMessage(player, target, msgDBPlayer, msgDBTarget)) return true;

        String msg = buildMessage(strings);
        sendMessage(player, target, msg);

        updateLastMessageUser(msgDBPlayer, msgDBTarget, target.getName(), player.getName());
        return true;
    }

    private boolean isValidTarget(Player player, Player target) {
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + target.getName() + " &cnie ma na serwerze!"));
            return false;
        }
        if (player.equals(target)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz wysłać wiadomości do samego siebie!"));
            return false;
        }
        return true;
    }

    private boolean canSendMessage(Player player, Player target, MsgDB msgDBPlayer, MsgDB msgDBTarget) {
        if (isIgnored(player, target, msgDBPlayer, msgDBTarget)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cTen gracz dodał cię do listy ignorowanych lub masz zablokowane wysyłanie wiadomości!"));
            return false;
        }
        if (msgDBPlayer.getBoolFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORE_ALL)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz pisać wiadomości prywatnych do innych graczy, gdy masz je zablokowane!"));
            return false;
        }
        if (msgDBTarget.getBoolFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORE_ALL)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cTen gracz ma zablokowane wysyłanie wiadomości prywatnych!"));
            return false;
        }
        return true;
    }

    private boolean isIgnored(Player player, Player target, MsgDB msgDBPlayer, MsgDB msgDBTarget) {
        ArrayList<String> ignoredUsersPlayer = (ArrayList<String>) msgDBPlayer.getValueFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORED_USERS);
        ArrayList<String> ignoredUsersTarget = (ArrayList<String>) msgDBTarget.getValueFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORED_USERS);
        return ignoredUsersPlayer.contains(target.getName()) || ignoredUsersTarget.contains(player.getName());
    }

    private String buildMessage(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append(strings[i]).append(" ");
        }
        return MessageUtil.removeLastCharacter(stringBuilder.toString());
    }

    private void sendMessage(Player player, Player target, String msg) {
        player.sendMessage(MessageUtil.fixColors("&a&lPRIV &8| @sJa &8→ &f" + target.getName() + "&8: &7" + msg));
        target.sendMessage(MessageUtil.fixColors("&a&lPRIV &8| &f" + player.getName() + " &8→ @sJa&8: &7" + msg));
    }

    private void updateLastMessageUser(MsgDB msgDBPlayer, MsgDB msgDBTarget, String targetName, String playerName) {
        msgDBPlayer.setValueInCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.LAST_WRITTEN_USER, targetName);
        msgDBTarget.setValueInCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.LAST_WRITTEN_USER, playerName);
    }
}
    






