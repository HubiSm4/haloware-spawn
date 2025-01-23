package dev.haloware.spawn.tasks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.haloware.ServerPlugin;
import dev.haloware.spawn.configs.SpawnConfig;
import dev.haloware.spawn.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportCountdownTask extends BukkitRunnable {
    private final Player player;
    private final Location location;
    private final String titleS1;
    private final String titleS2;
    private int count;

    SpawnConfig spawnConfig = ServerPlugin.getSpawnConfig();

    private static final Cache<UUID, TeleportCountdownTask> taskCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public TeleportCountdownTask(Player player, Location location, String titleS1, String titleS2, int count) {
        this.player = player;
        this.location = location;
        this.titleS1 = titleS1;
        this.titleS2 = titleS2;
        this.count = count;
    }

    public TeleportCountdownTask(Player player) {
        this.player = player;
        UUID playerUniqueId = player.getUniqueId();
        TeleportCountdownTask cachedTeleportCountdownTask = taskCache.getIfPresent(playerUniqueId);
        if (cachedTeleportCountdownTask != null) {
            this.location = cachedTeleportCountdownTask.location;
            this.titleS1 = cachedTeleportCountdownTask.titleS1;
            this.titleS2 = cachedTeleportCountdownTask.titleS2;
            this.count = cachedTeleportCountdownTask.count;
        } else {
            this.location = null;
            this.titleS1 = null;
            this.titleS2 = null;
            this.count = 0;
        }
    }

    @Override
    public void run() {
        if (count > 0) {
            player.sendTitle(MessageUtil.fixColors(spawnConfig.getCountdownTitle()), MessageUtil.fixColors(spawnConfig.getCountdownSubtitle().replaceAll("%countdown%", String.valueOf(count))));
            count--;
        } else {
            taskCache.invalidate(player.getUniqueId());

            player.teleport(location);

            player.sendMessage(MessageUtil.fixColors(ServerPlugin.getSpawnConfig().getMessagePrefix() + spawnConfig.getTeleportMessage()));
            player.sendTitle(MessageUtil.fixColors(titleS1), MessageUtil.fixColors(titleS2));

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            cancel();
        }
    }

    public void scheduleTask() {
        cancelTask();

        TeleportCountdownTask task = new TeleportCountdownTask(player, location, titleS1, titleS2, count);
        task.runTaskTimer(ServerPlugin.getPlugin(), 0, 20);

        taskCache.put(player.getUniqueId(), task);
    }

    public void cancelTask() {
        if (taskCache.getIfPresent(player.getUniqueId()) != null) {
            taskCache.getIfPresent(player.getUniqueId()).cancel();
            taskCache.invalidate(player.getUniqueId());

            if (player.isOnline()) {
                player.sendTitle(" ", " ");
            }
        }
    }

    public boolean inCountdown() {
        return taskCache.getIfPresent(player.getUniqueId()) != null;
    }

}