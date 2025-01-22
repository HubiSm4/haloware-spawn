package dev.hatopia;

import dev.hatopia.core.HookTemplate;
import dev.hatopia.core.configs.TemplateConfig;
import dev.hatopia.global.connections.MongoConnection;
import dev.hatopia.global.connections.RedisConnection;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlugin extends JavaPlugin {

    @Getter
    private static ServerPlugin plugin;

    @Getter
    private static TemplateConfig templateConfig;

    @Override
    public void onLoad() {
        plugin = this;

        HookTemplate hookTemplate = new HookTemplate();
        hookTemplate.config();
        hookTemplate.packets(true);

        hookConfigs();
        hookConnections();
    }

    @Override
    public void onEnable() {
        HookTemplate hookTemplate = new HookTemplate();
        hookTemplate.worldRules();
        hookTemplate.listeners();
        hookTemplate.packetListeners();
        hookTemplate.commands();
        hookTemplate.runnables();
        hookTemplate.pubSub();
        hookTemplate.placeholders();
        hookTemplate.messenger();
    }

    @Override
    public void onDisable() {
        HookTemplate hookTemplate = new HookTemplate();
        hookTemplate.playersCache();
        hookTemplate.packets(false);
    }

    private void hookConfigs() {
        templateConfig = HookTemplate.templateConfig;
    }

    private void hookConnections() {
        MongoConnection mongoConnection = new MongoConnection(
                "mongodb://<user>:<password>@<address>:<port>/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.3.2"
        );
        RedisConnection redisConnection = new RedisConnection("<address>", 6379, "<password>");
        mongoConnection.create();
        redisConnection.create();
    }
}


