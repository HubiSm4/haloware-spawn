package dev.hatopia.global.utils;

import org.bukkit.entity.Player;
import pl.goxy.minecraft.api.GoxyApi;
import pl.goxy.minecraft.api.network.GoxyServer;
import pl.goxy.mojang.authlib.profile.GameProfile;

public class GoxyUtil {

    public static void sendPlayerToServer(Player p, String server) {
        GoxyApi.getPlayerStorage().getPlayer(p.getUniqueId()).connect(GoxyApi.getNetworkManager().getServer(server));
    }

    public static boolean isPremium(Player p) {
        return GoxyApi.getPlayerStorage().getPlayer(p.getUniqueId()).isPremium();
    }

    public static GoxyServer getPreviousServer(Player p) {
        return GoxyApi.getPlayerStorage().getPlayer(p.getUniqueId()).getPreviousServer();
    }

    public static boolean getExists(String pName) {
        GameProfile profile = GoxyApi.getUserCache().getProfileIfCached(pName);
        if (profile == null) {
            return false;
        }
        return profile.isComplete();
    }

    public static int getOnlinePlayers(String server) {
        return GoxyApi.getNetworkManager().getServer(server).getOnlinePlayers();
    }

    public static int getMaxPlayers(String server) {
        return GoxyApi.getNetworkManager().getServer(server).getMaxPlayers();
    }

    public static int getFreeServerSlots(String server) {
        return getMaxPlayers(server) - getOnlinePlayers(server);
    }

    public static int getOnlinePlayersInContainer(String container) {
        return GoxyApi.getNetworkManager().getContainer(container).getOnlinePlayers();
    }

    public static int getMaxPlayersInContainer(String container) {
        return GoxyApi.getNetworkManager().getContainer(container).getMaxPlayers();
    }

    public static int getFreeContainerSlots(String container) {
        return getMaxPlayersInContainer(container) - getOnlinePlayersInContainer(container);
    }

    public static String getServerName() {
        return GoxyApi.getNetworkManager().getServer().getName();
    }
}
