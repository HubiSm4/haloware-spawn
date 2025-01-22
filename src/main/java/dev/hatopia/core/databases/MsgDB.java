package dev.hatopia.core.databases;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.MongoCollection;
import dev.hatopia.global.connections.MongoConnection;
import org.bson.Document;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MsgDB {
    private final Player player;
    private Document document;
    private final Document filter;
    private final MongoCollection<Document> coll = new MongoConnection().getCollection("global", "msgs");

    private static final Cache<UUID, MsgDB> dbCache = Caffeine.newBuilder()
            .expireAfterWrite(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
            .build();

    public enum DocumentKey {
        UUID, DATA
    }

    public enum MsgDocumentKey {
        LAST_WRITTEN_USER, IGNORED_USERS, IGNORE_ALL
    }

    public MsgDB(OfflinePlayer offlinePlayer) {
        this.player = offlinePlayer.getPlayer();
        UUID playerUniqueId = player.getUniqueId();
        this.filter = new Document().append(DocumentKey.UUID.name(), playerUniqueId.toString());
        MsgDB cachedMsgDB = dbCache.getIfPresent(playerUniqueId);
        if (cachedMsgDB != null) {
            this.document = cachedMsgDB.document;
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
                    .append(MsgDocumentKey.LAST_WRITTEN_USER.name(), null)
                    .append(MsgDocumentKey.IGNORED_USERS.name(), new ArrayList<>())
                    .append(MsgDocumentKey.IGNORE_ALL.name(), false);

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
}