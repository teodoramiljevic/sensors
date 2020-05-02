package teodoramiljevic.sensors.service.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import teodoramiljevic.sensors.service.configuration.MongoDbProperties;
import teodoramiljevic.sensors.service.models.Sensor;
import teodoramiljevic.sensors.service.models.SensorData;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;

@Repository
@Qualifier("default")
public class MongoDbSensorRepository extends MongoDbRepository implements SensorRepository {

    //region Constants
    private final String ID = "id";
    private final String VALUES = "values";
    //endregion

    private final MongoDatabase database;
    private final MongoCollection<Sensor> collection;
    private final MongoDbProperties properties;

    private final Logger logger = LogManager.getLogger(MongoDbSensorRepository.class);

    public MongoDbSensorRepository(final MongoDbProperties properties) {
        super(properties);

        this.properties = properties;
        this.database = mongoClient.getDatabase(properties.getDatabase()).withCodecRegistry(pojoCodecRegistry);
        this.collection = this.database.getCollection(properties.getCollection(), Sensor.class);
    }

    @Override
    public boolean addValue(final String sensorId, final SensorData sensorData) {
        final Bson filter = eq(ID, sensorId);
        final Bson updateOperation = push(VALUES, sensorData);

        final UpdateResult result = collection.updateOne(filter, updateOperation, new UpdateOptions().upsert(true));
        logger.debug("Updating sensor with value of " + sensorData.getValue());

        return documentUpdated(result);
    }
}
