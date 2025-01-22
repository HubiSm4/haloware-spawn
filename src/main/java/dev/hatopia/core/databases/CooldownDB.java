package dev.hatopia.core.databases;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.MongoCollection;
import dev.hatopia.global.connections.MongoConnection;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CooldownDB {
    private final Player player;
    private Document document;
    private final Document filter;
    private final MongoCollection<Document> coll = new MongoConnection().getCollection("template", "cooldowns");

    private static final Cache<UUID, CooldownDB> dbCache = Caffeine.newBuilder()
            .expireAfterWrite(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
            .build();

    public enum DocumentKey {
        UUID, DATA
    }

    public enum CooldownDocumentKey {
        COOLDOWNS, START_MILLIS, END_MILLIS
    }

    @Getter
    public enum Cooldown {
        CMD(3 * 1000),
        HELP_OP(30 * 1000);

        private final long cooldownDuration;

        Cooldown(long cooldownDuration) {
            this.cooldownDuration = cooldownDuration;
        }
    }

    public CooldownDB(OfflinePlayer offlinePlayer) {
        this.player = offlinePlayer.getPlayer();
        UUID playerUniqueId = player.getUniqueId();
        this.filter = new Document().append(DocumentKey.UUID.name(), playerUniqueId.toString());
        CooldownDB cachedCooldownDB = dbCache.getIfPresent(playerUniqueId);
        if (cachedCooldownDB != null) {
            this.document = cachedCooldownDB.document;
        } else {
            this.document = null;
        }
    }

    public boolean isCached() {
        return document != null;
    }

    public boolean exists() {
        MongoConnection mongoConnection = new MongoConnection();
        mongoConnection.create();
        long count = coll.countDocuments(filter);
        return count > 0;
    }

    public void insertFirstData() {
        if (!exists()) {
            MongoConnection mongoConnection = new MongoConnection();
            mongoConnection.create();

            Document dataDocument = new Document();
            dataDocument
                    .append(CooldownDocumentKey.COOLDOWNS.name(), new Document());

            Document document = new Document();
            document
                    .append(DocumentKey.UUID.name(), player.getUniqueId().toString())
                    .append(DocumentKey.DATA.name(), dataDocument);

            coll.insertOne(document);
            dbCache.put(player.getUniqueId(), this);
        }
    }

    public boolean getBoolFromCache(DocumentKey documentKey, Object key) {
        if (!isCached()) {
            return false;
        }
        Document selectedDocument = (Document) document.get(documentKey.name());
        return selectedDocument.getBoolean(key.toString());
    }

    public void setValueInCache(DocumentKey documentKey, Object key, Object value) {
        if (isCached()) {
            Document selectedDocument = (Document) document.get(documentKey.name());
            selectedDocument.put(key.toString(), value);
            dbCache.put(player.getUniqueId(), this);
        }
    }

    public Object getValueFromCache(DocumentKey documentKey, Object key) {
        if (!isCached()) {
            return null;
        }
        Document selectedDocument = (Document) document.get(documentKey.name());
        return selectedDocument.get(key.toString());
    }

    public boolean isOnline() {
        return player != null;
    }

    public CompletableFuture<Void> loadCacheAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        Thread.startVirtualThread(() -> {
            insertFirstData();
            if (!isCached()) {
                MongoConnection mongoConnection = new MongoConnection();
                mongoConnection.create();
                this.document = coll.find(filter).first();
                dbCache.put(player.getUniqueId(), this);
            }
            future.complete(null);
        });

        return future;
    }

    public CompletableFuture<Void> putCacheToDatabaseAndUnloadAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (isCached()) {
            Thread.startVirtualThread(() -> {
                MongoConnection mongoConnection = new MongoConnection();
                mongoConnection.create();
                coll.updateOne(filter, new Document("$set", document));
                dbCache.invalidate(player.getUniqueId());
                future.complete(null);
            });
        } else {
            future.complete(null);
        }

        return future;
    }

    public void putCacheToDatabaseAndUnload() {
        if (isCached()) {
            MongoConnection mongoConnection = new MongoConnection();
            mongoConnection.create();
            coll.updateOne(filter, new Document("$set", document));
            dbCache.invalidate(player.getUniqueId());
        }
    }

    public void addCooldown(Cooldown cooldown) {
        long cooldownDuration = cooldown.getCooldownDuration();
        Document cooldowns = (Document) getValueFromCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS);
        Document cooldownData = new Document();
        cooldownData.put(CooldownDocumentKey.START_MILLIS.name(), System.currentTimeMillis());
        cooldownData.put(CooldownDocumentKey.END_MILLIS.name(), System.currentTimeMillis() + cooldownDuration);

        cooldowns.put(cooldown.name(), cooldownData);
        setValueInCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS, cooldowns);
    }

    public void removeCooldown(Cooldown cooldown) {
        Document cooldowns = (Document) getValueFromCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS);
        cooldowns.remove(cooldown.name());

        setValueInCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS, cooldowns);
    }

    public boolean isOnCooldown(Cooldown cooldown) {
        Document cooldowns = (Document) getValueFromCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS);
        if (cooldowns == null) {
            return false;
        }

        Document cooldownData = cooldowns.get(cooldown.name(), Document.class);
        if (cooldownData == null) {
            return false;
        }

        long endMillis = cooldownData.getLong(CooldownDocumentKey.END_MILLIS.name());
        if (endMillis <= System.currentTimeMillis()) {
            cooldowns.remove(cooldown.name());
            setValueInCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS, cooldowns);
            return false;
        }

        return true;
    }

    public String getRemainingCooldownTime(Cooldown cooldown) {
        Document cooldowns = (Document) getValueFromCache(DocumentKey.DATA, CooldownDocumentKey.COOLDOWNS);

        Document cooldownData = cooldowns.get(cooldown.name(), Document.class);
        if (cooldownData == null) {
            return "0ms";
        }

        long endMillis = cooldownData.getLong(CooldownDocumentKey.END_MILLIS.name());
        long remainingTime = endMillis - System.currentTimeMillis();

        if (remainingTime <= 0) {
            return "0ms";
        }

        if (remainingTime < 1000) {
            return remainingTime + "ms";
        }

        long days = remainingTime / (1000 * 60 * 60 * 24);
        long hours = (remainingTime / (1000 * 60 * 60)) % 24;
        long minutes = (remainingTime / (1000 * 60)) % 60;
        long seconds = (remainingTime / 1000) % 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0) builder.append(seconds).append("s");

        return builder.toString().trim();
    }
}