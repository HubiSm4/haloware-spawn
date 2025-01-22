package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;;
import dev.hatopia.core.databases.MsgDB;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MsgIgnoreCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 0) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7/@swszyscy&7/@slista&7]"));
            return false;
        }

        MsgDB msgDB = new MsgDB(player);
        ArrayList<String> ignoredUsers = (ArrayList<String>) msgDB.getValueFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORED_USERS);

        switch (strings[0].toLowerCase()) {
            case "lista", "list" -> showIgnoredList(player, ignoredUsers);
            case "wszyscy", "all" -> toggleIgnoreAll(player, msgDB);
            default -> toggleIgnoreUser(player, strings[0], ignoredUsers);
        }
        return true;
    }

    private void showIgnoredList(Player player, ArrayList<String> ignoredUsers) {
        if (ignoredUsers.isEmpty()) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cBrak osób, od których ignorujesz wiadomości prywatne!"));
        } else {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Lista ignorowanych graczy: &c" + String.join(", ", ignoredUsers)));
        }
    }

    private void toggleIgnoreAll(Player player, MsgDB msgDB) {
        boolean ignoreAll = msgDB.getBoolFromCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORE_ALL);
        msgDB.setValueInCache(MsgDB.DocumentKey.DATA, MsgDB.MsgDocumentKey.IGNORE_ALL, !ignoreAll);
        String message = ignoreAll
                ? "&cPomyślnie wyłączono ignorowanie wiadomości prywatnych od wszystkich graczy!"
                : "&aPomyślnie włączono ignorowanie wiadomości prywatnych od wszystkich graczy!";
        player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + message));
    }

    private void toggleIgnoreUser(Player player, String target, ArrayList<String> ignoredUsers) {
        if (target.equalsIgnoreCase(player.getName())) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz dodać samego siebie do listy ignorowanych graczy!"));
            return;
        }

        if (ignoredUsers.contains(target)) {
            ignoredUsers.remove(target);
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cPomyślnie usunięto &2" + target + " &az listy ignorowanych graczy!"));
        } else {
            ignoredUsers.add(target);
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&aPomyślnie dodałeś &2" + target + " &ado listy ignorowanych graczy!"));
        }
    }
}
