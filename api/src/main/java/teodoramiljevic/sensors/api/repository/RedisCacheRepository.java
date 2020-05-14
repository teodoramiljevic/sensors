package teodoramiljevic.sensors.api.repository;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.api.configuration.RedisProperties;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * Redis implementation of cache repository.
 * Connects to an instance of Redis.
 * Responsible of closing the connection to Redis.
 * Implements async versions of get/set cache actions.
 */
public class RedisCacheRepository extends Connectible implements Closeable, CacheRepository {

    //region Constants

    private final String REDIS_CONNECTION_KEY = "redis://";

    //endregion Constants

    private final Logger logger = LogManager.getLogger(RedisCacheRepository.class);

    private final RedisProperties properties;

    private RedisClient redisClient;
    private StatefulRedisConnection redisConnection;
    private RedisAsyncCommands<String, String> redis;
    private boolean connected;

    RedisCacheRepository(final RedisProperties properties){
        super(new ConnectionProperties(properties.getHost(), properties.getPort()));
        this.properties = properties;
    }

    public String get(final String key){
        String result = null;
        if(connected){
            final RedisFuture<String> future = redis.get(key);
            try{
                result = future.get(properties.getTimeout(), TimeUnit.MILLISECONDS);
            }
            catch (final Exception ex){
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public void set(final String key, final String value){
        if(connected){
            redis.set(key, value);
        }
    }

    @Override
    public void connect() {
        try {
            this.redisClient = RedisClient.create(REDIS_CONNECTION_KEY + super.properties.getHost());
            this.redisConnection = redisClient.connect();
            this.redis = redisConnection.async();
            this.connected = true;
        }
        catch (final Exception ex){
            logger.error(ex.getMessage(), ex);
            connected = false;
        }
    }

    public void close(){
       if(redisConnection != null){
           redisConnection.close();
       }
    }
}
