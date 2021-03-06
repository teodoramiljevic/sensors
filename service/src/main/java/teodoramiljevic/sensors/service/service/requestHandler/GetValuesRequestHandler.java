package teodoramiljevic.sensors.service.service.requestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorGetValuesRequest;
import teodoramiljevic.sensors.messaging.SensorGetValues.SensorsGetValuesResponse;
import teodoramiljevic.sensors.messaging.SensorValue;
import teodoramiljevic.sensors.service.model.SensorData;
import teodoramiljevic.sensors.service.repository.SensorRepository;

import java.util.Arrays;
import java.util.List;

import static teodoramiljevic.sensors.messaging.MessageStatus.SUCCESS;

@Service
public class GetValuesRequestHandler extends RequestHandler<SensorGetValuesRequest, SensorsGetValuesResponse> {

    private final Logger logger = LogManager.getLogger(GetValuesRequestHandler.class);

    private final SensorRepository repository;
    private final ModelMapper modelMapper;

    public GetValuesRequestHandler(final SensorRepository repository, final ModelMapper modelMapper) {
        super(SensorGetValuesRequest.class);
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SensorsGetValuesResponse handle(final SensorGetValuesRequest request) {
        logger.info("Received [GET VALUES] request.");
        final List<SensorData> sensorValues = repository.getValuesBySensorId(request.getSensorId());
        final List<SensorValue> resultValues = Arrays.asList(modelMapper.map(sensorValues, SensorValue[].class));

        return new SensorsGetValuesResponse(resultValues, SUCCESS, null);
    }
}
