package dev.hatopia.core;

import com.github.retrooper.packetevents.PacketEvents;
import dev.hatopia.ServerPlugin;
import dev.hatopia.core.commands.*;
import dev.hatopia.core.configs.TemplateConfig;
import dev.hatopia.global.connections.RedisConnection;
import dev.hatopia.core.databases.CooldownDB;
import dev.hatopia.core.databases.MsgDB;
import dev.hatopia.core.listeners.*;
import dev.hatopia.core.packets.TabCompletionPacket;
import dev.hatopia.core.papi.VanishPlaceholder;
import dev.hatopia.core.runnables.AutoMessageRunnable;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import redis.clients.jedis.JedisPubSub;

import java.io.File;
import java.util.stream.Stream;

import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Bukkit.getScheduler;

public class HookTemplate {

    public static TemplateConfig templateConfig;

    public void listeners() {
        Stream.of(
                new KickListener(),
                new HookPlayerListener(),
                new SuspectListener(),
                new CommandSuggestionListener(),
                new CommandListener(),
                new VanishListener()
        ).forEach(listener -> ServerPlugin.getPlugin().getServer().getPluginManager().registerEvents(listener, ServerPlugin.getPlugin()));
    }

    public void commands() {
        ServerPlugin.getPlugin().getCommand("heal").setExecutor(new HealCmd());
        ServerPlugin.getPlugin().getCommand("invsee").setExecutor(new InvSeeCmd());
        ServerPlugin.getPlugin().getCommand("endersee").setExecutor(new EnderSeeCmd());
        ServerPlugin.getPlugin().getCommand("speed").setExecutor(new SpeedCmd());
        ServerPlugin.getPlugin().getCommand("stp").setExecutor(new StpCmd());
        ServerPlugin.getPlugin().getCommand("gamemode").setExecutor(new GameModeCmd());
        ServerPlugin.getPlugin().getCommand("clear").setExecutor(new ClearCmd());
        ServerPlugin.getPlugin().getCommand("tp").setExecutor(new TpCmd());
        ServerPlugin.getPlugin().getCommand("about").setExecutor(new AboutCmd());
        ServerPlugin.getPlugin().getCommand("message").setExecutor(new MsgMessageCmd());
        ServerPlugin.getPlugin().getCommand("response").setExecutor(new MsgResponseCmd());
        ServerPlugin.getPlugin().getCommand("ignore").setExecutor(new MsgIgnoreCmd());
        ServerPlugin.getPlugin().getCommand("hub").setExecutor(new HubCmd());
        ServerPlugin.getPlugin().getCommand("check").setExecutor(new SuspectCheckCmd());
        ServerPlugin.getPlugin().getCommand("confess").setExecutor(new SuspectConfessCmd());
        ServerPlugin.getPlugin().getCommand("adminchat").setExecutor(new AdminChatCmd());
        ServerPlugin.getPlugin().getCommand("helpop").setExecutor(new HelpOpCmd());
        ServerPlugin.getPlugin().getCommand("re-load").setExecutor(new ReLoadCmd());
        ServerPlugin.getPlugin().getCommand("re-start").setExecutor(new ReStartCmd());
        ServerPlugin.getPlugin().getCommand("vanish").setExecutor(new VanishCmd());
    }

    public void worldRules() {
        World world = Bukkit.getWorld("world");

        if (world != null) {
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);
        }
    }

    public void runnables() {
        getScheduler().scheduleSyncRepeatingTask(ServerPlugin.getPlugin(), new AutoMessageRunnable(), 0, 30 * 20);
    }

    public void packets(boolean load) {
        if (Bukkit.getPluginManager().isPluginEnabled("packetevents")) {
            if (load) {
                PacketEvents.setAPI(SpigotPacketEventsBuilder.build(ServerPlugin.getPlugin()));
                PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                        .checkForUpdates(true)
                        .bStats(false);
                PacketEvents.getAPI().load();
            } else {
                PacketEvents.getAPI().terminate();
            }
        }
    }

    public void packetListeners() {
        if (Bukkit.getPluginManager().isPluginEnabled("packetevents")) {
            PacketEvents.getAPI().getEventManager().registerListener(new TabCompletionPacket());
            PacketEvents.getAPI().init();
        }
    }

    public void playersCache() {
        getOnlinePlayers().forEach(player -> {
            MsgDB msgDB = new MsgDB(player);
            msgDB.putCacheToDatabaseAndUnload();

            CooldownDB cooldownDB = new CooldownDB(player);
            cooldownDB.putCacheToDatabaseAndUnload();
        });
    }

    public void messenger() {
        ServerPlugin.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(ServerPlugin.getPlugin(), "bungeecord:main");

        ServerPlugin.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(ServerPlugin.getPlugin(), "BungeeCord");
    }

    public void placeholders() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new VanishPlaceholder().register();
        }
    }

    public void pubSub() {
        RedisConnection redisConnectionPool = new RedisConnection();
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals("adminchat")) {
                    getOnlinePlayers().stream()
                            .filter(player -> player.hasPermission(ServerPlugin.getTemplateConfig().getPermissionPubSubPrefix() + "adminchat"))
                            .forEach(player -> {
                                player.sendMessage(message);
                            });
                }
                if (channel.equals("helpop")) {
                    getOnlinePlayers().stream()
                            .filter(player -> player.hasPermission(ServerPlugin.getTemplateConfig().getPermissionPubSubPrefix() + "helpop"))
                            .forEach(player -> {
                                player.sendMessage(message);
                            });
                }
            }
        };


        String[] channels = {"adminchat", "helpop"};
        redisConnectionPool.subscribe(channels, jedisPubSub);
    }

    public void config() {
        templateConfig = ConfigManager.create(TemplateConfig.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer());
            it.withBindFile(new File(ServerPlugin.getPlugin().getDataFolder(), "template.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }
}
