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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.push;

@Repository
@Qualifier("default")
public class MongoDbSensorRepository extends MongoDbRepository implements SensorRepository {

    //region Constants
    private final String ID = "sensorId";
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

    @Override
    public Optional<SensorData> getLatestValue(final String sensorId) {
        final Bson filter = eq(ID, sensorId);
        final Bson last = slice("values", -1);

        final Sensor result = collection.find(filter).projection(last).first();

        if (result != null) {
            final List<SensorData> values = result.getValues();
            if (!values.isEmpty()) {
                return Optional.of(values.get(0));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<SensorData> getValues(final String sensorId) {
        final Bson filter = eq(ID, sensorId);

        List<SensorData> values = new ArrayList<>();
        final Sensor result = collection.find(filter).projection(fields(include("values"))).first();
        if (result != null) {
            values = result.getValues();
        }

        return values;
    }
}
