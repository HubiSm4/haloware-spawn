package dev.hatopia.core.commands;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.managers.VanishManager;
import dev.hatopia.global.utils.BossBarUtil;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player player)) return true;

        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "list":
                    listHiddenPlayers(player);
                    return true;
                default:
                    toggleVanish(player);
                    return true;
            }
        } else {
            toggleVanish(player);
            return true;
        }
    }

    private void listHiddenPlayers(Player player) {
        var hiddenUsernames = VanishManager.getHiddenUsernames();
        if (hiddenUsernames.isEmpty()) {
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&cNie ma aktualnie żadnych ukrytych administratorów!"));
        } else {
            String hiddenPlayers = String.join(", ", hiddenUsernames);
            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "&7Ukryci: @s" + hiddenPlayers));
        }
    }

    private void toggleVanish(Player player) {
        if (VanishManager.isVanished(player)) {
            VanishManager.showPlayer(player);
            BossBarUtil.removeAllBossBars(player);
            player.sendTitle(MessageUtil.fixColors(MessageUtil.formatToFancyFont("&2VANISH")), MessageUtil.fixColors("&7Jesteś widoczny dla innych graczy"));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        } else {
            VanishManager.vanishPlayer(player);
            BossBar bossBar = BossBarUtil.createBossBar(
                    MessageUtil.fixColors("&7Jesteś na &cVANISHU&7. Inni gracze nie moga Cię zauważyć!"),
                    BarColor.RED, BarStyle.SEGMENTED_6
            );
            BossBarUtil.showBossBarToPlayer(bossBar, player);
            player.sendTitle(MessageUtil.fixColors(MessageUtil.formatToFancyFont("&cVANISH")), MessageUtil.fixColors("&7Jesteś niewidoczny dla innych graczy"));
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }
    }
}