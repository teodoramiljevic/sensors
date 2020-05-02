package teodoramiljevic.sensors.service.repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import teodoramiljevic.sensors.service.configuration.MongoDbProperties;
import teodoramiljevic.sensors.service.models.repository.ConnectionProperties;

import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDbRepository extends Connectible implements AutoCloseable {

    protected MongoClient mongoClient;
    protected CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoDbRepository(final MongoDbProperties properties) {
        super(new ConnectionProperties(properties.getHost(), properties.getPort()));
    }

    protected boolean documentUpdated(final UpdateResult result) {
        return result.getUpsertedId() != null || result.getModifiedCount() != 0;
    }

    @Override
    public void close() throws Exception {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public void connect() {
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(super.connectionProperties.getHost(), super.connectionProperties.getPort()))))
                        .build());
    }
}
