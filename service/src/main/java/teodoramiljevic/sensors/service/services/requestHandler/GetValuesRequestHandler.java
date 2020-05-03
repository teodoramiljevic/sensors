package teodoramiljevic.sensors.service.services.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.MessageStatus;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorGetValuesRequest;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorsGetValuesResponse;
import teodoramiljevic.sensors.messaging.SensorValue;
import teodoramiljevic.sensors.service.models.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class GetValuesRequestHandler extends RequestHandler<SensorGetValuesRequest, SensorsGetValuesResponse> {

    private final SensorRepository repository;
    private final ModelMapper modelMapper;
    private final Logger logger = LogManager.getLogger(GetValuesRequestHandler.class);

    public GetValuesRequestHandler(final SensorRepository repository, final ModelMapper modelMapper) {
        super(SensorGetValuesRequest.class);
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SensorsGetValuesResponse handle(final SensorGetValuesRequest request) {
        logger.debug("Received get values request");
        final List<SensorData> sensorValues = repository.getValues(request.getSensorId());
        final List<SensorValue> resultValues = Arrays.asList(modelMapper.map(sensorValues, SensorValue[].class));

        return new SensorsGetValuesResponse(MessageStatus.SUCCESS, resultValues);
    }
}
