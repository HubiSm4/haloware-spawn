package dev.hatopia.core.managers;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SuspectManager {

    @Getter
    private final Player player;

    @Getter
    private final Player playerAdministrator;

    @Getter
    private final String reason;

    @Getter
    private final boolean abilityToConfess;

    @Getter
    private long startTimeMillis;

    private static final Cache<UUID, SuspectManager> managerCache = Caffeine.newBuilder()
            .expireAfterWrite(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
            .build();

    public SuspectManager(Player player) {
        this.player = player;
        UUID playerUniqueId = player.getUniqueId();
        SuspectManager cachedAntiLogoutManager = managerCache.getIfPresent(playerUniqueId);
        if (cachedAntiLogoutManager != null) {
            this.playerAdministrator = cachedAntiLogoutManager.playerAdministrator;
            this.reason = cachedAntiLogoutManager.reason;
            this.abilityToConfess = cachedAntiLogoutManager.abilityToConfess;
            this.startTimeMillis = cachedAntiLogoutManager.startTimeMillis;
        } else {
            this.playerAdministrator = null;
            this.reason = "";
            this.abilityToConfess = false;
            this.startTimeMillis = System.currentTimeMillis();
        }
    }

    public SuspectManager(Player player, Player playerAdministrator, String reason, boolean abilityToConfess) {
        this.player = player;
        UUID playerUniqueId = player.getUniqueId();
        SuspectManager cachedAntiLogoutManager = managerCache.getIfPresent(playerUniqueId);
        if (cachedAntiLogoutManager != null) {
            this.playerAdministrator = cachedAntiLogoutManager.playerAdministrator;
            this.reason = cachedAntiLogoutManager.reason;
            this.abilityToConfess = cachedAntiLogoutManager.abilityToConfess;
            this.startTimeMillis = cachedAntiLogoutManager.startTimeMillis;
        } else {
            this.playerAdministrator = playerAdministrator;
            this.reason = reason;
            this.abilityToConfess = abilityToConfess;
            this.startTimeMillis = System.currentTimeMillis();
        }
    }

    public void addToSuspect() {
        managerCache.put(player.getUniqueId(), this);
    }

    public void removeFromSuspicion() {
        if (inSuspect()) {
            managerCache.invalidate(player.getUniqueId());
        }
    }

    public boolean inSuspect() {
        return managerCache.getIfPresent(player.getUniqueId()) != null;
    }
}
