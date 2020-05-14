package teodoramiljevic.sensors.service.service.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueRequest;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueResponse;
import teodoramiljevic.sensors.service.model.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import static teodoramiljevic.sensors.messaging.MessageKeys.VALUE_NOT_SAVED;
import static teodoramiljevic.sensors.messaging.MessageStatus.INTERNAL_SERVER_ERROR;
import static teodoramiljevic.sensors.messaging.MessageStatus.SUCCESS;

@Service
public class AddValueRequestHandler extends RequestHandler<SensorAddValueRequest, SensorAddValueResponse> {

    private final Logger logger = LogManager.getLogger(AddValueRequestHandler.class);

    private final SensorRepository repository;
    private final ModelMapper modelMapper;

    public AddValueRequestHandler(final SensorRepository repository, final ModelMapper modelMapper) {
        super(SensorAddValueRequest.class);
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SensorAddValueResponse handle(final SensorAddValueRequest request) {
        logger.info("Received [ADD VALUE] request");
        final boolean valueAdded = repository.saveValue(request.getSensorId(), modelMapper.map(request.getValue(), SensorData.class));

        if (valueAdded) {
            logger.info("Value " + request.getValue().getValue() + " successfully added to sensor " + request.getSensorId());
            final SensorAddValueResponse sensorAddValueResponse = modelMapper.map(request, SensorAddValueResponse.class);
            sensorAddValueResponse.setStatus(SUCCESS);

            return sensorAddValueResponse;
        }

        logger.info("Value " + request.getValue().getValue() + " not added to sensor " + request.getSensorId());
        return new SensorAddValueResponse(null, INTERNAL_SERVER_ERROR, VALUE_NOT_SAVED);
    }

}
