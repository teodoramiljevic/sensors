package teodoramiljevic.sensors.service.services.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestRequest;
import teodoramiljevic.sensors.messaging.SensorGetLatest.SensorGetLatestResponse;
import teodoramiljevic.sensors.messaging.SensorValue;
import teodoramiljevic.sensors.service.models.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import java.util.Optional;

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
        logger.debug("Received get latest request");
        final Optional<SensorData> sensorData = repository.getLatestValue(request.getSensorId());

        //TODO: Add message key so that it can be properly handled

        if (sensorData.isPresent()) {
            logger.debug("Latest value found");
            final SensorValue latestValue = modelMapper.map(sensorData.get(), SensorValue.class);
            return new SensorGetLatestResponse(latestValue, MessageStatus.SUCCESS);
        }

        logger.debug("Latest value not found - no values");
        return new SensorGetLatestResponse(null, MessageStatus.INTERNAL_ERROR);
    }
}
