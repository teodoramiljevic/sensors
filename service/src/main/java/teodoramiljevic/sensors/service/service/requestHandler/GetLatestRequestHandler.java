package teodoramiljevic.sensors.service.service.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestRequest;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestResponse;
import teodoramiljevic.sensors.messaging.SensorValue;
import teodoramiljevic.sensors.service.exception.SensorNotFoundException;
import teodoramiljevic.sensors.service.model.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import java.util.Optional;

import static teodoramiljevic.sensors.messaging.MessageKeys.SENSOR_NOT_FOUND;
import static teodoramiljevic.sensors.messaging.MessageKeys.VALUE_NOT_FOUND;
import static teodoramiljevic.sensors.messaging.MessageStatus.NOT_FOUND;
import static teodoramiljevic.sensors.messaging.MessageStatus.SUCCESS;

@Service
public class GetLatestRequestHandler extends RequestHandler<SensorGetLatestRequest, SensorGetLatestResponse> {

    private final Logger logger = LogManager.getLogger(GetLatestRequestHandler.class);

    private final SensorRepository repository;
    private final ModelMapper modelMapper;

    public GetLatestRequestHandler(final SensorRepository repository, final ModelMapper modelMapper) {
        super(SensorGetLatestRequest.class);
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SensorGetLatestResponse handle(final SensorGetLatestRequest request) {
        try {
            final Optional<SensorData> sensorData = repository.getLatestValueBySensorId(request.getSensorId());
            if (sensorData.isPresent()) {
                logger.debug("Value found.");
                final SensorValue latestValue = modelMapper.map(sensorData.get(), SensorValue.class);
                return new SensorGetLatestResponse(latestValue, SUCCESS, null);
            }

            logger.debug("Sensor doesn't have any values.");
            return new SensorGetLatestResponse(null, NOT_FOUND, VALUE_NOT_FOUND);
        } catch (final SensorNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new SensorGetLatestResponse(null, NOT_FOUND, SENSOR_NOT_FOUND);
        }
    }
}
