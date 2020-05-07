package teodoramiljevic.sensors.api.repository;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teodoramiljevic.sensors.api.configuration.RedisProperties;

import java.io.Closeable;

public class RedisCacheRepository extends Connectible implements Closeable, CacheRepository {

    //region Constants

    private final String REDIS_CONNECTION_KEY = "redis://";

    //endregion Constants

    private final Logger logger = LogManager.getLogger(RedisCacheRepository.class);

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> redisConnection;
    private RedisStringCommands redis;
    private boolean connected;

    RedisCacheRepository(final RedisProperties properties){
        super(new ConnectionProperties(properties.getHost(), properties.getPort()));
    }

    public String get(final String key){
        return connected?(String)redis.get(key) : null;
    }

    public void set(final String key, final String value){
        if(connected){
            final String setit = redis.set(key, value);
        }
    }

    @Override
    public void connect() {
        try {
            this.redisClient = RedisClient.create(REDIS_CONNECTION_KEY + super.properties.getHost());
            this.redisConnection = redisClient.connect();
            this.redis = redisConnection.sync();
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
