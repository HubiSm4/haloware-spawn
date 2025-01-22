package dev.hatopia.global.connections;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisConnection {

    @Getter
    private static JedisPool jedisPool;

    @Getter
    private static String host;

    @Getter
    private static int port;

    @Getter
    private static String password;

    public RedisConnection(String host, int port, String password) {
        RedisConnection.host = host;
        RedisConnection.port = port;
        RedisConnection.password = password;
        jedisPool = new JedisPool(RedisConnection.host, RedisConnection.port);
    }

    public RedisConnection() {
    }

    private boolean isConnected() {
        return jedisPool != null && !jedisPool.isClosed();
    }

    public void create() {
        if (!isConnected()) {
            jedisPool = new JedisPool(host, port);
        }
    }

    public void publish(String channel, String message) {
        try (Jedis publisher = jedisPool.getResource()) {
            publisher.auth(password);
            publisher.publish(channel, message);
        }
    }

    public void subscribe(String[] channels, JedisPubSub jedisPubSub) {
        Thread.startVirtualThread(() -> {
            try (Jedis jedis = RedisConnection.getJedisPool().getResource()) {
                jedis.auth(RedisConnection.getPassword());
                jedis.subscribe(jedisPubSub, channels);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if (isConnected()) {
            jedisPool.close();
        }
    }
}
