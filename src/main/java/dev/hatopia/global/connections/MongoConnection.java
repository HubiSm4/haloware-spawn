package dev.hatopia.global.connections;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

public class MongoConnection {

    @Getter
    private static MongoClient mongoClient;

    @Getter
    private static ConnectionString connectionString;

    public MongoConnection(String connectionString) {
        MongoConnection.connectionString = new ConnectionString(
                connectionString
        );
    }

    public MongoConnection() {
    }

    private boolean isConnected() {
        return mongoClient != null;
    }

    public void create() {
        if (!isConnected()) {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            mongoClient = MongoClients.create(settings);
        }
    }

    public void close() {
        if (isConnected()) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        if (isConnected()) {
            return mongoClient.getDatabase(databaseName).getCollection(collectionName);
        } else {
            throw new IllegalStateException("Connection pool has not been created.");
        }
    }
}
