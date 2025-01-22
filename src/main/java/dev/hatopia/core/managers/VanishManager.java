package dev.hatopia.core.managers;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class VanishManager {

    @Getter
    public static ArrayList<String> hiddenUsernames = new ArrayList<String>();

    public static boolean isVanished(Player player) {
        return hiddenUsernames.contains(player.getName());
    }

    public static void vanishPlayer(Player player) {
        hiddenUsernames.add(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            if (p1 == player) {
                continue;
            }

            p1.hidePlayer(player);
        }
    }

    public static void showPlayer(Player player) {
        hiddenUsernames.remove(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            p1.showPlayer(player);
        }
    }
}
