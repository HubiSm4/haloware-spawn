package dev.hatopia.core.tasks;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SuspectTask extends BukkitRunnable {
    private final Player player;

    private static final Cache<UUID, SuspectTask> taskCache = Caffeine.newBuilder()
            .expireAfterWrite(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
            .build();

    public SuspectTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        MessageUtil.sendActionBar(player, "&aPrzyznanie się zmniejsza wymiar Twojej kary, wpisz &2/przyznajesie");
    }

    public void scheduleTask() {
        cancelTask();

        SuspectTask task = new SuspectTask(player);
        task.runTaskTimer(ServerPlugin.getPlugin(), 0, 40);

        taskCache.put(player.getUniqueId(), task);
    }

    public void cancelTask() {
        if (taskCache.getIfPresent(player.getUniqueId()) != null) {
            taskCache.getIfPresent(player.getUniqueId()).cancel();
            taskCache.invalidate(player.getUniqueId());

            MessageUtil.sendActionBar(player, " ");
        }
    }
}
