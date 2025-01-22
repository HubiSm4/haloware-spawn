package dev.hatopia.core.papi;

import dev.hatopia.ServerPlugin;
import dev.hatopia.core.managers.VanishManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishPlaceholder extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return String.join(", ", ServerPlugin.getPlugin().getDescription().getAuthors());
    }

    @Override
    public String getIdentifier() {
        return "vanish";
    }

    @Override
    public String getVersion() {
        return ServerPlugin.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (VanishManager.isVanished(player)) {
            return "&8(&cVANISH&8)";
        }

        return "";
    }
}