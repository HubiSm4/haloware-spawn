package dev.hatopia.global.utils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBarUtil {

    private static Map<Player, BossBar> playerBossBars = new HashMap<>();

    public static BossBar createBossBar(String text, BarColor color, BarStyle style) {
        return Bukkit.createBossBar(text, color, style);
    }

    public static void setBossBarValue(BossBar bossBar, double value) {
        bossBar.setProgress(value);
    }

    private static void addPlayerToBossBar(BossBar bossBar, Player player) {
        bossBar.addPlayer(player);
    }

    public static void removePlayerFromBossBar(BossBar bossBar, Player player) {
        bossBar.removePlayer(player);
    }

    public static void showBossBarToPlayer(BossBar bossBar, Player player) {
        addPlayerToBossBar(bossBar, player);
        bossBar.setVisible(true);
        playerBossBars.put(player, bossBar);
    }

    public static void hideBossBarFromPlayer(Player player) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar != null) {
            removePlayerFromBossBar(bossBar, player);
            bossBar.setVisible(false);
            playerBossBars.remove(player);
        }
    }

    public static void removeAllBossBars(Player player) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar != null) {
            removePlayerFromBossBar(bossBar, player);
            playerBossBars.remove(player);
        }
    }

    public static void disablePluginBossbars() {
        Bukkit.getOnlinePlayers().forEach(BossBarUtil::removeAllBossBars);
    }

    public static void updateBossBar(Player player, String text, BarColor color, BarStyle style, double value) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar != null) {
            bossBar.setTitle(text);
            bossBar.setColor(color);
            bossBar.setStyle(style);
            bossBar.setProgress(value);
        }
    }

    public static BossBar getBossBar(Player player) {
        return playerBossBars.get(player);
    }
}
