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
import teodoramiljevic.sensors.service.exception.SensorNotFoundException;
import teodoramiljevic.sensors.service.model.Sensor;
import teodoramiljevic.sensors.service.model.SensorData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.slice;
import static com.mongodb.client.model.Updates.push;

@Repository
@Qualifier("default")
public class MongoDbSensorRepository extends MongoDbRepository implements SensorRepository {

    //region Constants
    private final String ID = "sensorId";
    private final String VALUES = "values";
    private final int LATEST_SLICE = -1;
    //endregion

    private final Logger logger = LogManager.getLogger(MongoDbSensorRepository.class);

    private final MongoDatabase database;
    private final MongoCollection<Sensor> collection;

    public MongoDbSensorRepository(final MongoDbProperties properties) {
        super(properties);
        this.database = mongoClient.getDatabase(properties.getDatabase()).withCodecRegistry(pojoCodecRegistry);
        this.collection = this.database.getCollection(properties.getCollection(), Sensor.class);
    }

    @Override
    public boolean saveValue(final String sensorId, final SensorData sensorData) {
        final Bson filter = eq(ID, sensorId);
        final Bson updateOperation = push(VALUES, sensorData);

        final UpdateResult result = collection.updateOne(filter, updateOperation, new UpdateOptions().upsert(true));
        logger.info("Updating sensor " + sensorId + " with value of " + sensorData.getValue());

        return documentUpdated(result);
    }

    @Override
    public Optional<SensorData> getLatestValueBySensorId(final String sensorId) throws SensorNotFoundException {
        final Bson filter = eq(ID, sensorId);
        final Bson last = slice(VALUES, LATEST_SLICE);

        final Sensor result = collection.find(filter).projection(last).first();
        if (result != null) {
            final List<SensorData> values = result.getValues();
            if (!values.isEmpty()) {
                return Optional.of(values.get(0));
            }

            logger.info("There are no values for sensor " + sensorId);
            return Optional.empty();

        }

        logger.info("There is no sensor with sensor id of " + sensorId);
        throw new SensorNotFoundException(sensorId);
    }

    @Override
    public List<SensorData> getValuesBySensorId(final String sensorId) {
        final Bson filter = eq(ID, sensorId);

        List<SensorData> values = new ArrayList<>();
        final Sensor result = collection.find(filter).projection(fields(include(VALUES))).first();
        if (result != null) {
            values = result.getValues();
        }

        return values;
    }
}
