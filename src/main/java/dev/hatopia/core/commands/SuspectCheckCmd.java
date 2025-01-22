package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.managers.SuspectManager;
import dev.hatopia.core.tasks.SuspectTask;
import dev.hatopia.global.utils.BossBarUtil;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SuspectCheckCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return true;

        if (strings.length == 0) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Użycie komendy: @s/" + command.getName() + " &7[@sgracz&7] &8| @s/" + command.getName() + " &7[@sgracz&7] [@spowód&7]"));
            return false;
        }

        Player target = Bukkit.getPlayer(strings[0]);
        if (target == null) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cGracza &4" + strings[0] + " &cnie ma na serwerze. Sprawdź poprawność znaków!"));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie możesz wykonać tego na sobie!"));
            return true;
        }

        String reason = strings.length >= 2 ? buildMessage(strings, 1) : "Rutynowa kontrola";
        SuspectManager suspectManager = new SuspectManager(target, player, reason, true);
        SuspectTask suspectTask = new SuspectTask(target);

        if (suspectManager.inSuspect()) {
            handleRemovalFromSuspicion(target, player, suspectManager, suspectTask);
        } else {
            handleAddToSuspicion(target, player, suspectManager, suspectTask);
        }
        return true;
    }

    private String buildMessage(String[] strings, int start) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < strings.length; i++) {
            stringBuilder.append(strings[i]).append(" ");
        }
        return MessageUtil.removeLastCharacter(stringBuilder.toString());
    }

    private void handleRemovalFromSuspicion(Player target, Player player, SuspectManager suspectManager, SuspectTask suspectTask) {
        suspectManager.removeFromSuspicion();
        BossBarUtil.removeAllBossBars(target);
        target.sendTitle(
                MessageUtil.fixColors("&2" + MessageUtil.formatToFancyFont("PODEJRZENIE")),
                MessageUtil.fixColors("&7Administrator &4" + player.getName() + " &7Cię uwolnił!")
        );
        player.sendTitle(
                MessageUtil.fixColors("&2" + MessageUtil.formatToFancyFont("PODEJRZENIE")),
                MessageUtil.fixColors("&7Uwolniłeś gracza &4" + player.getName() + ".")
        );
        target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);
        suspectTask.cancelTask();
    }

    private void handleAddToSuspicion(Player target, Player player, SuspectManager suspectManager, SuspectTask suspectTask) {
        suspectManager.addToSuspect();
        target.sendTitle(
                MessageUtil.fixColors("&2" + MessageUtil.formatToFancyFont("PODEJRZENIE")),
                MessageUtil.fixColors("&7Powód: &c" + suspectManager.getReason() + "&7. Administrator: &4" + player.getName())
        );
        player.sendTitle(
                MessageUtil.fixColors("&2" + MessageUtil.formatToFancyFont("PODEJRZENIE")),
                MessageUtil.fixColors("&7Sprawdzasz gracza &4" + player.getName() + ". &7Powód: &c" + suspectManager.getReason() + "&7.")
        );
        BossBar bossBar = BossBarUtil.createBossBar(
                MessageUtil.fixColors("&cJesteś podejrzany. Udaj się na nasz serwer &4DISCORD &ci wejdź na kanał &4#jestem-podejrzany"),
                BarColor.RED, BarStyle.SEGMENTED_6
        );
        BossBarUtil.showBossBarToPlayer(bossBar, target);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
        if (suspectManager.isAbilityToConfess()) {
            suspectTask.scheduleTask();
        }
    }
}
