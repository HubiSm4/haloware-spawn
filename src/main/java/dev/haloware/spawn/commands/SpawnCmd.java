package dev.haloware.spawn.commands;

import dev.haloware.ServerPlugin;
import dev.haloware.spawn.configs.SpawnConfig;
import dev.haloware.spawn.tasks.TeleportCountdownTask;
import dev.haloware.spawn.utils.ConfigUtil;
import dev.haloware.spawn.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return true;

        SpawnConfig spawnConfig = ServerPlugin.getSpawnConfig();

        int count;
        if (ServerPlugin.getSpawnConfig().isEnableCountdown()) {
            count = ServerPlugin.getSpawnConfig().getCountdownInterval();
        } else {
            count = 0;
        }

        TeleportCountdownTask teleportCountdownTask = new TeleportCountdownTask(player, ConfigUtil.parseLocation(spawnConfig.getLocationWorld() + ";" + spawnConfig.getLocationX() + ";" + spawnConfig.getLocationY() + ";" + spawnConfig.getLocationZ() + ";" + spawnConfig.getLocationYaw() + ";" + spawnConfig.getLocationPitch()), MessageUtil.fixColors(spawnConfig.getTeleportTitle()), MessageUtil.fixColors(spawnConfig.getTeleportSubtitle()), count);
        teleportCountdownTask.scheduleTask();
        return false;
    }
}
