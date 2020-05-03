package teodoramiljevic.sensors.service.services.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueRequest;
import teodoramiljevic.sensors.messaging.SensorAddValue.SensorAddValueResponse;
import teodoramiljevic.sensors.service.models.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

@Service
public class AddValueRequestHandler extends RequestHandler<SensorAddValueRequest, SensorAddValueResponse> {

    Logger logger = LogManager.getLogger(AddValueRequestHandler.class);
    SensorRepository repository;
    ModelMapper modelMapper;

    public AddValueRequestHandler(final SensorRepository repository, final ModelMapper modelMapper) {
        super(SensorAddValueRequest.class);
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SensorAddValueResponse handle(final SensorAddValueRequest request) {
        final boolean valueAdded = repository.addValue(request.getSensorId(), modelMapper.map(request.getValue(), SensorData.class));

        if (valueAdded) {
            logger.debug("Value " + request.getValue().getValue() + "successfully added to sensor " + request.getSensorId());
            final SensorAddValueResponse sensorAddValueResponse = modelMapper.map(request, SensorAddValueResponse.class);
            sensorAddValueResponse.setStatus(MessageStatus.SUCCESS);

            return sensorAddValueResponse;
        }

        logger.debug("Value " + request.getValue().getValue() + " not added to sensor " + request.getSensorId());
        return new SensorAddValueResponse(null, MessageStatus.INTERNAL_ERROR);
    }

}
